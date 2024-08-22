package com.pools.store.presentation.tabOption.leaderboard.view

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
import com.pools.store.databinding.FragmentLeaderBoardBinding
import com.pools.store.presentation.app.viewModel.AppViewModel
import com.pools.store.presentation.app.viewModel.GetListAppState
import com.pools.store.presentation.app.viewModel.GetListCategoryState
import com.pools.store.presentation.detailGame.adapter.TagAdapter
import com.pools.store.presentation.tabOption.leaderboard.adapter.LeaderBoardAdapter
import com.pools.store.presentation.tabOption.leaderboard.adapter.TagCategoryAdapter
import com.pools.store.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LeaderBoardFragment : BaseFragment<FragmentLeaderBoardBinding, AppViewModel>() {

    override fun getViewBinding() = FragmentLeaderBoardBinding.inflate(layoutInflater)
    override val viewModel: AppViewModel by viewModels()

    @Inject
    lateinit var leaderBoardAdapter:LeaderBoardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }



    private fun initView() {
        binding.apply {
            rvLeaderBoard.apply {
                adapter = leaderBoardAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }

            leaderBoardAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }
        }
    }

    private fun initData() {
        viewModel.getListAppState.mapNotNull { it }.onEach (this::onViewStateListAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListAppByTwoParams(CachePreferencesHelper(requireContext()).accessToken, Constant.LEADER_BOARD, Constant.APPLICATION)
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
                leaderBoardAdapter.list = event.data.items
                leaderBoardAdapter.notifyDataSetChanged()

            }
        }
    }
}