package com.leeloo.vkstore.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.leeloo.vkstore.R
import com.leeloo.vkstore.ui.RecyclerState
import com.leeloo.vkstore.utils.Coordinator
import com.leeloo.vkstore.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.fragment_products.*
import kotlinx.android.synthetic.main.toolbar_fragment_products.*

class ProductFragment : Fragment() {
    private lateinit var viewModel: ProductsViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_products,
        container,
        false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(ProductsViewModel::class.java)
        viewModel.onRetry(arguments?.getLong("id") ?: 0L)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.viewState.observe(this.viewLifecycleOwner, Observer {
            val state = when {
                it.isProductsLoading -> RecyclerState.LOADING
                it.errorLoadingProducts != null -> RecyclerState.ERROR
                it.products.isEmpty() -> RecyclerState.EMPTY
                else -> RecyclerState.ITEM
            }
            val isRefreshable = !(it.isProductsLoading || it.errorLoadingProducts != null)

            refresher.isEnabled = isRefreshable
            refresher.isRefreshing = it.isRefreshLoading
            adapter.setItems(it.products, state)

            if (it.errorRefreshLoading != null)
                Snackbar.make(
                    fragment_products,
                    it.errorRefreshLoading.localizedMessage ?: "",
                    Snackbar.LENGTH_SHORT
                ).show()
        })
    }

    private fun initViews() {
        arrow_back.setOnClickListener {
            Coordinator.onPopBackstack(this.requireActivity().supportFragmentManager)
        }
        product_title_toolbar.text = resources.getString(
            R.string.title_products, arguments?.getString("title")
        )

        refresher.setOnRefreshListener {
            viewModel.onRefresh(arguments?.getLong("id") ?: 0L)
        }
        refresher.setColorSchemeResources(R.color.colorAccent)

        adapter = ProductsAdapter(
            { Coordinator.onProductClicked(this.requireActivity().supportFragmentManager, it) },
            { viewModel.onRetry(arguments?.getLong("id") ?: 0L) }
        )
        adapter.setHasStableIds(true)

        products_recycler.adapter = adapter
        products_recycler.layoutManager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL
        )
        products_recycler.addItemDecoration(ProductsItemDecorator(
            resources.getDimensionPixelSize(R.dimen.margin_default)
        ))
        products_recycler.setHasFixedSize(true)
    }

    companion object {
        fun newInstance(id: Long, title: String): ProductFragment {
            val fragment = ProductFragment()
            val args = Bundle().apply {
                putLong("id", id)
                putString("title", title)
            }
            return fragment.apply { arguments = args }
        }
    }

}