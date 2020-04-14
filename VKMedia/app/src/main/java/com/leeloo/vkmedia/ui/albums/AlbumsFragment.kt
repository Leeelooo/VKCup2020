package com.leeloo.vkmedia.ui.albums

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.leeloo.vkmedia.R
import com.leeloo.vkmedia.ui.RecyclerState
import com.leeloo.vkmedia.utils.Coordinator
import com.leeloo.vkmedia.utils.isVisible
import com.leeloo.vkmedia.viewmodel.AlbumsViewModel
import com.leeloo.vkmedia.vo.VKAlbum
import kotlinx.android.synthetic.main.fragment_albums.*
import kotlinx.android.synthetic.main.toolbar_fragment_albums.*

class AlbumsFragment : Fragment() {
    private lateinit var viewModel: AlbumsViewModel
    private lateinit var adapter: AlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_albums,
        container,
        false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)
            .get(AlbumsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewModel.viewStateData.observe(this.viewLifecycleOwner, Observer {
            val state = when {
                it.isAlbumLoading -> RecyclerState.LOADING
                it.errorLoadingAlbums != null -> RecyclerState.ERROR
                it.albums.isEmpty() -> RecyclerState.EMPTY
                else -> RecyclerState.ITEM
            }
            val isRefreshable = !(it.isAlbumLoading || it.errorLoadingAlbums != null)

            logout.isVisible = !it.isEditMode
            toolbar_title.isVisible = !it.isEditMode
            add_album.isVisible = !it.isEditMode
            edit_mode.isVisible = !it.isEditMode

            exit_edit_mode.isVisible = it.isEditMode
            edit_text_title.isVisible = it.isEditMode

            refresher.isEnabled = isRefreshable
            refresher.isRefreshing = it.isRefreshLoading
            adapter.setItems(it.albums, state)
            adapter.setMode(it.isEditMode)

            if (it.errorRefreshLoading != null)
                Snackbar.make(
                    fragment_albums,
                    it.errorRefreshLoading.localizedMessage ?: "",
                    Snackbar.LENGTH_SHORT
                ).show()
        })
    }

    private fun initViews() {
        refresher.setOnRefreshListener { viewModel.onRefresh() }
        refresher.setColorSchemeResources(R.color.colorAccent)

        add_album.setOnClickListener {
            buildAlbumCreationDialog()
        }
        edit_mode.setOnClickListener {
            viewModel.onEnterEditMode()
        }
        logout.setOnClickListener {
            Coordinator.onLogout(requireActivity().supportFragmentManager)
        }
        exit_edit_mode.setOnClickListener {
            viewModel.onExitEditMode()
        }

        adapter = AlbumsAdapter(
            { viewModel.onRetry() },
            { Coordinator.onAlbumClicked(requireActivity().supportFragmentManager, it) },
            { buildDeleteDialog(it) },
            { viewModel.onLongTap() }
        )
        adapter.setHasStableIds(true)

        albums_recycler.adapter = adapter
        albums_recycler.addItemDecoration(
            AlbumsItemDecorator(
                resources.getDimensionPixelSize(R.dimen.gutter_default)
            )
        )
        albums_recycler.layoutManager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL
        )
    }

    private fun buildDeleteDialog(album: VKAlbum) {
        val dialog = AlertDialog.Builder(this.context)
            .setTitle(R.string.album_delete_title)
            .setMessage(R.string.album_delete_message)
            .setPositiveButton(R.string.album_option_delete) { _, _ ->
                viewModel.onDeleteAlbum(album.id)
            }
            .setNegativeButton(R.string.album_option_cancel, null)
            .create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(this.requireContext(), R.color.colorAccent))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(this.requireContext(), R.color.colorAccent))
        }
        dialog.show()
    }

    private fun buildAlbumCreationDialog() {
        val editTextLayout = layoutInflater.inflate(R.layout.dialog_rename_view, null)

        val dialog = AlertDialog.Builder(this.context)
            .setTitle(R.string.album_create_title)
            .setMessage(R.string.album_create_message)
            .setPositiveButton(R.string.album_create_btn) { _, _ ->
                viewModel.onCreateAlbum(
                    editTextLayout.findViewById<TextInputEditText>(R.id.edit_text).text.toString().trim()
                )
            }
            .setNegativeButton(R.string.album_option_cancel, null)
            .setView(editTextLayout)
            .create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(this.requireContext(), R.color.colorAccent))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(this.requireContext(), R.color.colorAccent))
        }
        dialog.show()
    }

    companion object {
        fun newInstance() = AlbumsFragment()
    }

}