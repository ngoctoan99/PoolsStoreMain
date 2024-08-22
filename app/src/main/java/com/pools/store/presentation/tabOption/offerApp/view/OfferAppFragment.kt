package com.pools.store.presentation.tabOption.offerApp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.R
import com.pools.store.base.BaseFragment
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.FragmentOfferAppBinding
import com.pools.store.presentation.app.viewModel.AppViewModel
import com.pools.store.presentation.app.viewModel.GetListAppState
import com.pools.store.presentation.home.adapter.AppAdapter
import com.pools.store.presentation.tabOption.offerApp.adapter.OfferAdapter
import com.pools.store.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class OfferAppFragment : BaseFragment<FragmentOfferAppBinding, AppViewModel>() {

    override fun getViewBinding() = FragmentOfferAppBinding.inflate(layoutInflater)
    override val viewModel: AppViewModel by viewModels()

    @Inject
    lateinit var offerAdapter:OfferAdapter

    @Inject
    lateinit var hostDiscountAdapter: AppAdapter

    @Inject
    lateinit var recommendAdapter: AppAdapter

    @Inject
    lateinit var noInternetAdapter: AppAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        binding.apply {
            rvOffer.apply {
                adapter = offerAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            }

            rvHostDiscount.apply {
                adapter = hostDiscountAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            rvRecommend.apply {
                adapter = recommendAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            rvNoInternet.apply {
                adapter = noInternetAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }

            tvSeeMoreHostDiscount.setOnClickListener{
                val bundle = Bundle().apply {
                    putString("keySeeMoreTitle",Constant.TITLE_HOT_DISCOUNT)
                    putString("keySeeMoreTag", Constant.HOT_DISCOUNT)
                }
                findNavController().navigate(R.id.action_seeMore_to_seeMoreFragment, bundle)
            }
            tvSeeMoreRecommend.setOnClickListener{
                val bundle = Bundle().apply {
                    putString("keySeeMoreTitle",Constant.TITLE_RECOMMEND)
                    putString("keySeeMoreTag", Constant.RECOMMEND)
                }
                findNavController().navigate(R.id.action_seeMore_to_seeMoreFragment, bundle)
            }
            tvSeeMoreNoInternet.setOnClickListener{
                val bundle = Bundle().apply {
                    putString("keySeeMoreTitle",Constant.TITLE_NO_INTERNET)
                    putString("keySeeMoreTag", Constant.NO_INTERNET)
                }
                findNavController().navigate(R.id.action_seeMore_to_seeMoreFragment, bundle)
            }

            /**
             * event listener
             * */
            hostDiscountAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }
            recommendAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }
            noInternetAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }

            offerAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }

        }
    }

    private fun initData() {
        viewModel.getListAppState.mapNotNull { it }.onEach (this::onViewStateListAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListAppByTwoParams(CachePreferencesHelper(requireContext()).accessToken, Constant.OFFER, Constant.APPLICATION)

        viewModel.getListHostDiscountAppState.mapNotNull { it }.onEach(this::onViewStateListHostDiscountAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListApp(CachePreferencesHelper(requireContext()).accessToken, Constant.HOT_DISCOUNT)

        viewModel.getListRecommendAppState.mapNotNull { it }.onEach(this::onViewStateRecommendAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListApp(CachePreferencesHelper(requireContext()).accessToken, Constant.RECOMMEND)

        viewModel.getListNoInternetAppState.mapNotNull { it }.onEach(this::onViewStateNoInternetAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListApp(CachePreferencesHelper(requireContext()).accessToken, Constant.NO_INTERNET)
    }

    private fun onViewStateListAppChange(event : GetListAppState){
        when(event){

            is GetListAppState.Error ->{
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)
                Log.d("LeaderBoardFragment onViewStateListCategoryChange Error:"," ${event.error} ${event.statusCode}")
            }
            is GetListAppState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetListAppState.Success ->{
                handleLoading(false)
                Log.d("LeaderBoardFragment onViewStateListLeaderBoardChange Success:"," ${event.data}")
                offerAdapter.list = event.data.items
                offerAdapter.notifyDataSetChanged()

//                recommendAdapter.list = event.data.items
//                recommendAdapter.notifyDataSetChanged()
//
//                noInternetAdapter.list = event.data.items
//                noInternetAdapter.notifyDataSetChanged()


            }
        }
    }

    private fun onViewStateListHostDiscountAppChange(event: GetListAppState) {
        when (event) {
            is GetListAppState.Error -> {
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)

            }
            is GetListAppState.Loading -> {
                handleLoading(event.isLoading)
            }
            is GetListAppState.Success -> {
                handleLoading(false)
                if(event.data.items.isNotEmpty()){
                    binding.apply {
                        rvHostDiscount.visibility = View.VISIBLE
                        tvSeeMoreHostDiscount.visibility = View.VISIBLE
                        tvNoDataHostDiscount.visibility = View.GONE
                        hostDiscountAdapter.list = event.data.items
                        hostDiscountAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun onViewStateRecommendAppChange(event: GetListAppState) {
        when (event) {
            is GetListAppState.Error -> {
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)
            }
            is GetListAppState.Loading -> {
                handleLoading(event.isLoading)
            }
            is GetListAppState.Success -> {
                handleLoading(false)
                if(event.data.items.isNotEmpty()){
                    binding.apply {
                        rvRecommend.visibility = View.VISIBLE
                        tvSeeMoreRecommend.visibility = View.VISIBLE
                        tvNoDataRecommend.visibility = View.GONE

                        recommendAdapter.list = event.data.items
                        recommendAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun onViewStateNoInternetAppChange(event: GetListAppState) {
        when (event) {
            is GetListAppState.Error -> {
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)
            }
            is GetListAppState.Loading -> {
                handleLoading(event.isLoading)
            }
            is GetListAppState.Success -> {
                handleLoading(false)
                if(event.data.items.isNotEmpty()){
                    binding.apply {
                        rvNoInternet.visibility = View.VISIBLE
                        tvSeeMoreNoInternet.visibility = View.VISIBLE
                        tvNoDataNoInternet.visibility = View.GONE
                        noInternetAdapter.list = event.data.items
                        noInternetAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}