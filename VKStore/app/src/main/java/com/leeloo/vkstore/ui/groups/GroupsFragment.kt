package com.leeloo.vkstore.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.leeloo.vkstore.R
import com.leeloo.vkstore.ui.RecyclerState
import com.leeloo.vkstore.utils.Coordinator
import com.leeloo.vkstore.utils.isVisible
import com.leeloo.vkstore.viewmodel.GroupsViewModel
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.toolbar_fragment_groups.*

class GroupsFragment : Fragment() {
    private lateinit var viewModel: GroupsViewModel
    private lateinit var bottomSheet: BottomSheetDialog

    private lateinit var cityAdapter: CityAdapter
    private lateinit var groupsAdapter: GroupsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_groups,
        container,
        false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(GroupsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.viewState.observe(this.viewLifecycleOwner, Observer {
            val citiesState = when {
                it.isCitiesLoading -> RecyclerState.LOADING
                it.errorLoadingCities != null -> RecyclerState.ERROR
                it.cities.isEmpty() -> RecyclerState.EMPTY
                else -> RecyclerState.ITEM
            }
            val groupState = when {
                it.isGroupLoading -> RecyclerState.LOADING
                it.errorLoadingGroups != null -> RecyclerState.ERROR
                it.groups.isEmpty() -> RecyclerState.EMPTY
                else -> RecyclerState.ITEM
            }
            val isRefreshable = !(it.isGroupLoading || it.errorLoadingGroups != null
                    || it.isCitiesLoading || it.errorLoadingCities != null || it.queueCity == null)

            refresher.isRefreshing = it.isRefreshLoading
            refresher.isEnabled = isRefreshable
            refresher.isVisible = !it.isBackdropOpen

            product_title_toolbar.text =
                if (it.queueCity != null)
                    resources.getString(R.string.title_groups, it.queueCity.name)
                else
                    resources.getString(R.string.title_groups_no_city)

            if (it.isBackdropOpen)
                bottomSheet.show()
            else
                bottomSheet.dismiss()

            cityAdapter.setItems(it.cities, citiesState, it.queueCity)
            groupsAdapter.setItems(it.groups, groupState)

            if (it.errorRefreshLoading != null)
                Snackbar.make(
                    fragment_groups,
                    it.errorRefreshLoading.localizedMessage ?: "",
                    Snackbar.LENGTH_SHORT
                ).show()
        })
    }

    private fun initViews() {
        dropdown_city.setOnClickListener { viewModel.onDropdownClicked() }

        refresher.setOnRefreshListener { viewModel.onRefresh() }
        refresher.setColorSchemeResources(R.color.colorAccent)

        cityAdapter = CityAdapter(
            { viewModel.onCityChoosed(it) },
            { viewModel.onCitiesRetry() }
        )

        groupsAdapter = GroupsAdapter(
            { Coordinator.onGroupClicked(requireActivity().supportFragmentManager, it) },
            { viewModel.onGroupsRetry() }
        )
        groupsAdapter.setHasStableIds(true)

        groups_recycler.adapter = groupsAdapter
        groups_recycler.layoutManager = LinearLayoutManager(this.requireContext())
        groups_recycler.setHasFixedSize(true)

        bottomSheet = BottomSheetDialog(this.requireContext())
        bottomSheet.setContentView(R.layout.bottom_sheet)
        bottomSheet.dismissWithAnimation = true

        bottomSheet.exit_city_mode.setOnClickListener { bottomSheet.dismiss() }

        bottomSheet.cities_recycler.adapter = cityAdapter
        bottomSheet.cities_recycler.layoutManager = LinearLayoutManager(this.requireContext())
        bottomSheet.cities_recycler.setHasFixedSize(true)

        bottomSheet.setOnDismissListener {
            viewModel.onExitDropdownClicked()
        }
    }

    companion object {
        fun newInstance() = GroupsFragment()
    }

}