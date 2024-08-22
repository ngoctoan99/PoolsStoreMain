package com.pools.store.presentation.managerApp.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.R
import com.pools.store.base.BaseFragment
import com.pools.store.base.PagingLoadStateAdapter
import com.pools.store.core.DialogClaimSuccess
import com.pools.store.databinding.FragmentGiftBinding
import com.pools.store.databinding.FragmentManagerAppBinding
import com.pools.store.presentation.gift.adapter.GiftAdapter
import com.pools.store.presentation.main.MainViewModel
import com.pools.store.presentation.managerApp.viewModel.ManagerAppViewModel
import com.pools.store.presentation.search.view.SearchPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ManagerAppFragment : BaseFragment<FragmentManagerAppBinding, ManagerAppViewModel>() {

    override fun getViewBinding() = FragmentManagerAppBinding.inflate(layoutInflater)
    override val viewModel: ManagerAppViewModel by viewModels()

    @Inject
    lateinit var managerAppPagingAdapter: ManagerAppPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPaging()
        initViewStateChange()
        initView()
    }

    override fun onBackFragment() {
        findNavController().navigateUp()
    }

    private fun initView() {
        binding.apply {
            rvDownloadHistory.apply {
                adapter = managerAppPagingAdapter
                layoutManager = GridLayoutManager(context, 2)
                adapter = managerAppPagingAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(managerAppPagingAdapter),
                    footer = PagingLoadStateAdapter(managerAppPagingAdapter)
                )

                managerAppPagingAdapter.setItemClickListener {
                    val bundle = bundleOf("dataApp" to it.apk)
                    findNavController().navigate(R.id.action_managerAppFragment_to_detailGameFragment,bundle)
                }
            }

            ivLeft.setOnClickListener {
                onBackFragment()
            }
        }


    }

    private fun initViewStateChange() {

        viewModel.isHaveData.observe(viewLifecycleOwner) { isChange ->
            binding.apply {
                tvEmpty.visibility = if (isChange) View.GONE else View.VISIBLE
                rvDownloadHistory.visibility = if (isChange) View.VISIBLE else View.GONE
            }
        }
    }


    private fun initPaging() {
        viewModel.getDownloadHistory()
        lifecycleScope.launch {
            with(viewModel) {
                managerAppPagingFlow.collectLatest { pagingData ->
                    managerAppPagingAdapter.submitData(pagingData)
                }
                managerAppPagingAdapter.loadStateFlow.collectLatest {
//                    binding.swipeRefreshMyTip.isRefreshing = it.refresh is LoadState.Loading
                }
                managerAppPagingAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.rvDownloadHistory.scrollToPosition(0) }


            }

        }
    }
}