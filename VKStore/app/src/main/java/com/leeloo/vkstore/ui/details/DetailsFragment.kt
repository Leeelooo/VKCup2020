package com.leeloo.vkstore.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.leeloo.vkstore.R
import com.leeloo.vkstore.utils.Coordinator
import com.leeloo.vkstore.utils.isVisible
import com.leeloo.vkstore.viewmodel.DetailsViewModel
import com.leeloo.vkstore.vo.VKProduct
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.toolbar_fragment_details.*

class DetailsFragment : Fragment() {
    private lateinit var viewModel: DetailsViewModel

    private val product: VKProduct?
        get() = arguments?.getSerializable("product") as VKProduct

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_product_details,
        container,
        false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(DetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.faveState.observe(this.viewLifecycleOwner, Observer {
            add_fave.isVisible = !it
            remove_fave.isVisible = it
            product?.isFavorite = it
        })
    }

    private fun initViews() {
        arrow_back.setOnClickListener {
            Coordinator.onPopBackstack(this.requireActivity().supportFragmentManager)
        }
        add_fave.setOnClickListener {
            viewModel.onAddToFavorites(product!!.ownerId, product!!.id)
        }
        remove_fave.setOnClickListener {
            viewModel.onRemoveFromFavorites(product!!.ownerId, product!!.id)
        }

        product_title_toolbar.text = product?.title
        Glide.with(product_details_photo)
            .applyDefaultRequestOptions(
                RequestOptions().placeholder(R.color.colorAccent)
            )
            .load(product?.photoUrl)
            .into(product_details_photo)
        details_product_title.text = product?.title
        details_product_price.text = product?.price
        details_product_description.text = product?.description
        viewModel.setDefaultValue(product?.isFavorite ?: false)
    }

    companion object {
        fun newInstance(product: VKProduct): DetailsFragment {
            val fragment = DetailsFragment()
            val bundle = Bundle().apply {
                putSerializable("product", product)
            }
            return fragment.apply { arguments = bundle }
        }
    }

}