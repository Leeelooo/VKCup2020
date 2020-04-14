package com.leeloo.vkdocuments.ui

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.leeloo.vkdocuments.R
import com.leeloo.vkdocuments.utils.CircularOutlineProvider
import com.leeloo.vkdocuments.viewmodel.DocumentViewModel
import com.leeloo.vkdocuments.vo.VKDocument
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: DocumentAdapter
    private lateinit var viewModel: DocumentViewModel

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            viewModel.onUserLoggedOut()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VK.addTokenExpiredHandler(tokenTracker)

        viewModel = ViewModelProvider(this).get(DocumentViewModel::class.java)
        if (VK.isLoggedIn() && savedInstanceState == null)
            viewModel.onUserLoggedIn()
        initViews()

        viewModel.viewStateData.observe(this, Observer {
            val state = when {
                !it.isUserLoggedIn -> RecyclerState.NOT_LOGGED
                it.isDocsLoading -> RecyclerState.LOADING
                it.errorLoadingDocs != null -> RecyclerState.ERROR
                it.docs.isEmpty() -> RecyclerState.EMPTY
                else -> RecyclerState.DOCS
            }
            val isRefreshable =
                !(!it.isUserLoggedIn || it.isDocsLoading || it.errorLoadingDocs != null)

            adapter.updateData(it.docs, state)
            Glide.with(main_profile)
                .load(it.user?.photoURL)
                .fallback(R.drawable.ic_profile)
                .into(main_profile)
            refresher.isRefreshing = it.isRefreshLoading
            refresher.isEnabled = isRefreshable

            if (it.errorLoadingUser != null)
                Snackbar.make(
                    main_activity,
                    it.errorLoadingUser.localizedMessage ?: "",
                    Snackbar.LENGTH_SHORT
                ).show()
            if (it.errorRefreshLoading != null)
                Snackbar.make(
                    main_activity,
                    it.errorRefreshLoading.localizedMessage ?: "",
                    Snackbar.LENGTH_SHORT
                ).show()
        })
    }

    private fun initViews() {
        main_profile.setOnClickListener {
            if (VK.isLoggedIn()) {
                VK.clearAccessToken(this)
                viewModel.onUserLoggedOut()
            } else
                VK.login(this, arrayListOf(VKScope.DOCS))
        }
        main_profile.outlineProvider = CircularOutlineProvider
        main_profile.clipToOutline = true

        refresher.setOnRefreshListener { viewModel.onRefresh() }

        adapter = DocumentAdapter(
            { itemId, docId ->
                when (itemId) {
                    R.id.item_menu_rename -> buildRenameDialog(docId)
                    R.id.item_menu_delete -> buildDeleteDialog(docId)
                }
                false
            },
            { VK.login(this, arrayListOf(VKScope.DOCS)) },
            { viewModel.onUserLoggedIn() },
            {
                if (it.type in 3..4)
                    startActivity(
                        Intent(this, DetailsActivity::class.java).apply {
                            val b = Bundle()
                            b.putBoolean("isGif", it.type == 3)
                            b.putString("url", it.url)
                            putExtras(b)
                        }
                    )
                else
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(it.url)
                        )
                    )
            }
        )
        adapter.setHasStableIds(true)

        documents_recycler.adapter = adapter
        documents_recycler.layoutManager = LinearLayoutManager(this)
        documents_recycler.setHasFixedSize(true)
    }

    private fun buildRenameDialog(document: VKDocument) {
        val editTextLayout = layoutInflater.inflate(R.layout.dialog_rename_view, null)

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.document_rename_title)
            .setMessage(R.string.document_rename_message)
            .setPositiveButton(R.string.document_option_rename) { _, _ ->
                viewModel.onDocumentRename(
                    document,
                    editTextLayout.findViewById<TextInputEditText>(R.id.edit_text).text.toString().trim()
                )
            }
            .setNegativeButton(R.string.document_option_cancel, null)
            .setView(editTextLayout)
            .create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(this, R.color.colorOnPrimaryMedium)
            )
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(this, R.color.colorOnPrimaryMedium)
            )
        }
        dialog.show()
    }

    private fun buildDeleteDialog(document: VKDocument) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.document_delete_title)
            .setMessage(R.string.document_delete_message)
            .setPositiveButton(R.string.document_option_delete) { _, _ ->
                viewModel.onDocumentDelete(document)
            }
            .setNegativeButton(R.string.document_option_cancel, null)
            .create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(this, R.color.colorOnPrimaryMedium)
            )
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(this, R.color.colorOnPrimaryMedium)
            )
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                viewModel.onUserLoggedIn()
            }

            override fun onLoginFailed(errorCode: Int) {
                Snackbar.make(
                    main_activity,
                    errorCode.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        if (!VK.onActivityResult(requestCode, resultCode, data, callback))
            super.onActivityResult(requestCode, resultCode, data)
    }

}
