package com.leeloo.vkunsub.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.leeloo.vkunsub.R
import com.leeloo.vkunsub.utils.isVisible
import com.leeloo.vkunsub.utils.reduce
import com.leeloo.vkunsub.viewmodel.GroupViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_info.*
import kotlinx.android.synthetic.main.toolbar_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: GroupViewModel

    private lateinit var bottomSheet: BottomSheetDialog
    private lateinit var adapter: GroupAdapter

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            viewModel.onLogout()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        viewModel = ViewModelProvider(this)
            .get(GroupViewModel::class.java)

        if (savedInstanceState == null) {
            if (VK.isLoggedIn())
                viewModel.onLogin()
            else
                viewModel.onLogout()
        }
        VK.addTokenExpiredHandler(tokenTracker)

        viewModel.viewState.observe(this, Observer {
            val state = when {
                !it.isLoggedIn -> RecyclerState.LOGIN
                it.isGroupsLoading -> RecyclerState.LOADING
                it.groupLoadingError != null -> RecyclerState.ERROR
                it.groups.isEmpty() -> RecyclerState.EMPTY
                else -> RecyclerState.ITEM
            }
            val isRefreshable = !(it.isGroupsLoading || it.groupLoadingError != null)

            refresher.isEnabled = isRefreshable
            refresher.isRefreshing = it.isRefreshLoading

            adapter.setItems(it.groups, state, it.selected)
            unsub_button_layout.isVisible = it.selected.isNotEmpty()
            unsub_button.text = resources.getString(R.string.unsub_button_text, it.selected.size)

            if (it.groupInfo != null) {
                bottomSheet.bottom_sheet_open_group.setOnClickListener { _ ->
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://vk.com/public${it.groupInfo.id}")
                    )
                    startActivity(intent)
                }
                bottomSheet.bottom_sheet_subs.text = resources.getQuantityString(
                    R.plurals.subs_count,
                    it.groupInfo.membersCount.toInt(),
                    reduce(it.groupInfo.membersCount)
                )
                bottomSheet.bottom_sheet_title.text = it.groupInfo.name
                bottomSheet.bottom_sheet_description.text = it.groupInfo.description
                bottomSheet.bottom_sheet_description.isVisible =
                    it.groupInfo.description.isNotEmpty()
                bottomSheet.bottom_sheet_descr_icon.isVisible =
                    it.groupInfo.description.isNotEmpty()
                bottomSheet.bottom_sheet_last_post.text = resources.getString(
                    R.string.last_post_date,
                    DateUtils.getRelativeTimeSpanString(
                        it.groupInfo.lastTimestamp,
                        System.currentTimeMillis(),
                        DateUtils.DAY_IN_MILLIS
                    )
                )
                bottomSheet.show()
            } else
                bottomSheet.dismiss()

            if (it.refreshLoadingError != null)
                Snackbar.make(
                    main_activity,
                    R.string.error_snackbar,
                    Snackbar.LENGTH_SHORT
                ).show()
        })
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        unsub_button.setOnClickListener {
            viewModel.onUnsubscribeClick()
        }

        refresher.setColorSchemeResources(R.color.colorAccent)
        refresher.setOnRefreshListener { viewModel.onRefresh() }

        adapter = GroupAdapter(
            { viewModel.onClick(it) },
            { viewModel.onLongClick(it) },
            { VK.login(this, listOf(VKScope.GROUPS)) },
            { viewModel.onRetry() }
        )
        group_recycler.adapter = adapter
        group_recycler.addItemDecoration(
            GroupItemDecorator(
                resources.getDimensionPixelSize(R.dimen.margin_default)
            )
        )
        group_recycler.layoutManager = StaggeredGridLayoutManager(
            3, StaggeredGridLayoutManager.VERTICAL
        )
        group_recycler.setHasFixedSize(true)

        bottomSheet = BottomSheetDialog(this)
        bottomSheet.setContentView(R.layout.bottom_sheet_info)
        bottomSheet.dismissWithAnimation = true

        bottomSheet.bottom_sheet_dismiss.setOnClickListener {
            bottomSheet.dismiss()
        }
        bottomSheet.setOnDismissListener {
            viewModel.onDismiss()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                viewModel.onLogin()
            }

            override fun onLoginFailed(errorCode: Int) {

            }
        }
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
