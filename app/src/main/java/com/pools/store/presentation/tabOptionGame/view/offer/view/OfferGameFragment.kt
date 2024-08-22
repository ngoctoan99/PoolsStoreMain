package com.pools.store.presentation.tabOptionGame.view.offer.view

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
import com.pools.store.databinding.FragmentOfferGameBinding
import com.pools.store.presentation.game.view.GameFragment
import com.pools.store.presentation.game.viewModel.GameViewModel
import com.pools.store.presentation.game.viewModel.GetListAppState
import com.pools.store.presentation.home.adapter.AppAdapter
import com.pools.store.presentation.tabOption.offerApp.adapter.OfferAdapter
import com.pools.store.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class OfferGameFragment : BaseFragment<FragmentOfferGameBinding, GameViewModel>() {

    override fun getViewBinding() = FragmentOfferGameBinding.inflate(layoutInflater)
    override val viewModel: GameViewModel by viewModels()

    @Inject
    lateinit var offerAdapter:OfferAdapter

    @Inject
    lateinit var trendingAdapter: AppAdapter

    @Inject
    lateinit var newGameAdapter: AppAdapter

    @Inject
    lateinit var playToEarnAdapter: AppAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.root.post {
//            val height = binding.root.height
//            (parentFragment as? GameFragment)?.updateFragmentHeight(2, height)
//        }

        initView()
        initData()
    }

    private fun initView() {

        binding.apply {
            rvOffer.apply {
                adapter = offerAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            }

            rvTrending.apply {
                adapter = trendingAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            rvNewGame.apply {
                adapter = newGameAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            rvPlayToEarn.apply {
                adapter = playToEarnAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }

            /**
             * event listener
             * */

            offerAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }

            trendingAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }
            newGameAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }
            playToEarnAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }

            tvSeeMoreTrending.setOnClickListener{
                val bundle = Bundle().apply {
                    putString("keySeeMoreTitle",Constant.TITLE_TRENDING)
                    putString("keySeeMoreTag", Constant.TRENDING)
                }
                findNavController().navigate(R.id.action_seeMore_to_seeMoreFragment, bundle)
            }
            tvSeeMoreNewGame.setOnClickListener{
                val bundle = Bundle().apply {
                    putString("keySeeMoreTitle",Constant.TITLE_NEW_GAMES)
                    putString("keySeeMoreTag", Constant.NEW_GAMES)
                }
                findNavController().navigate(R.id.action_seeMore_to_seeMoreFragment, bundle)
            }
            tvSeeMorePlayToEarn.setOnClickListener{
                val bundle = Bundle().apply {
                    putString("keySeeMoreTitle",Constant.TITLE_PLAY_TO_EARN)
                    putString("keySeeMoreTag", Constant.PLAY_TO_EARN)
                }
                findNavController().navigate(R.id.action_seeMore_to_seeMoreFragment, bundle)
            }
        }
    }

    private fun initData() {
        viewModel.getListByTwoParamsState.mapNotNull { it }.onEach (this::onViewStateListAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListByTwoParams(CachePreferencesHelper(requireContext()).accessToken, Constant.OFFER, Constant.GAME)

        viewModel.getListTrendingAppState.mapNotNull { it }.onEach(this::onViewStateListTrendingAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListApp(CachePreferencesHelper(requireContext()).accessToken, Constant.TRENDING)

        viewModel.getListNewGameAppState.mapNotNull { it }.onEach(this::onViewStateNewGameAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListApp(CachePreferencesHelper(requireContext()).accessToken, Constant.NEW_GAMES)

        viewModel.getListPlayToEarnAppState.mapNotNull { it }.onEach(this::onViewStatePlayToEarnAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListApp(CachePreferencesHelper(requireContext()).accessToken, Constant.PLAY_TO_EARN)
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

            }
        }
    }

    private fun onViewStateListTrendingAppChange(event: GetListAppState) {
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
                        rvTrending.visibility = View.VISIBLE
                        tvSeeMoreTrending.visibility = View.VISIBLE
                        tvNoDataTrending.visibility = View.GONE

                        trendingAdapter.list = event.data.items
                        trendingAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun onViewStateNewGameAppChange(event: GetListAppState) {
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
                        rvNewGame.visibility = View.VISIBLE
                        tvSeeMoreNewGame.visibility = View.VISIBLE
                        tvNoDataNewGame.visibility = View.GONE

                        newGameAdapter.list = event.data.items
                        newGameAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun onViewStatePlayToEarnAppChange(event: GetListAppState) {
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
                        rvPlayToEarn.visibility = View.VISIBLE
                        tvSeeMorePlayToEarn.visibility = View.VISIBLE
                        tvNoDataPlayToEarn.visibility = View.GONE

                        playToEarnAdapter.list = event.data.items
                        playToEarnAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}