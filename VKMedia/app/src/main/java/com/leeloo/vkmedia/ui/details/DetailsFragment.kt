package com.leeloo.vkmedia.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.leeloo.vkmedia.R
import com.leeloo.vkmedia.ui.RecyclerState
import com.leeloo.vkmedia.utils.Coordinator
import com.leeloo.vkmedia.utils.getPath
import com.leeloo.vkmedia.utils.isVisible
import com.leeloo.vkmedia.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_album_details.*
import kotlinx.android.synthetic.main.fragment_albums.refresher
import kotlinx.android.synthetic.main.toolbar_fragment_details.*

class DetailsFragment : Fragment() {
    private lateinit var viewModel: DetailsViewModel
    private lateinit var adapter: DetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_album_details,
        container,
        false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(DetailsViewModel::class.java)
        viewModel.onRetry(arguments?.getLong("id") ?: 0L)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewModel.viewStateData.observe(this.viewLifecycleOwner, Observer {
            val state = when {
                it.isPhotosLoading -> RecyclerState.LOADING
                it.errorLoadingPhotos != null -> RecyclerState.ERROR
                it.photos.isEmpty() -> RecyclerState.EMPTY
                else -> RecyclerState.ITEM
            }
            val isRefreshable = !(it.isPhotosLoading || it.errorLoadingPhotos != null)

            refresher.isEnabled = isRefreshable
            refresher.isRefreshing = it.isRefreshLoading
            adapter.setItems(it.photos, state)

            if (it.errorRefreshLoading != null)
                Snackbar.make(
                    fragment_details,
                    it.errorRefreshLoading.localizedMessage ?: "",
                    Snackbar.LENGTH_SHORT
                ).show()
        })
    }

    private fun initViews() {
        arrow_back.setOnClickListener {
            Coordinator.onDetailsPop(requireActivity().supportFragmentManager)
        }
        add_photo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        if (arguments?.getLong("id") ?: 0L < 1L)
            add_photo.isVisible = false

        refresher.setOnRefreshListener { viewModel.onRefresh(arguments?.getLong("id") ?: 0L) }
        refresher.setColorSchemeResources(R.color.colorAccent)

        adapter = DetailsAdapter(
            { viewModel.onRetry(arguments?.getLong("id") ?: 0L) },
            arguments?.getString("title") ?: ""
        )

        photos_recycler.adapter = adapter
        photos_recycler.addItemDecoration(
            DetailsItemDecorator(
                resources.getDimensionPixelSize(R.dimen.gutter_default)
            )
        )
        photos_recycler.layoutManager = StaggeredGridLayoutManager(
            3, StaggeredGridLayoutManager.VERTICAL
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK)
                viewModel.onAddPhoto(
                    getPath(requireContext(), data?.data),
                    this.arguments?.getLong("id") ?: 0L
                )
        }
    }

    companion object {
        fun newInstance(id: Long, title: String): Fragment {
            val fragment = DetailsFragment()
            val args = Bundle().apply {
                putLong("id", id)
                putString("title", title)
            }
            return fragment.apply { arguments = args }
        }
    }

}