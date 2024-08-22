package com.pools.store.presentation.noti.tabLayout.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.base.BaseFragment
import com.pools.store.base.BaseViewModel
import com.pools.store.base.PagingLoadStateAdapter
import com.pools.store.databinding.FragmentNotiSystemBinding
import com.pools.store.presentation.main.MainActivity
import com.pools.store.presentation.noti.tabLayout.adapter.NotiPagingAdapter
import com.pools.store.presentation.noti.tabLayout.viewModel.SystemNotiChildTabViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SystemNotiChildTabFragment : BaseFragment<FragmentNotiSystemBinding, SystemNotiChildTabViewModel>() {


    override val viewModel: SystemNotiChildTabViewModel by viewModels()

    @Inject
    lateinit var notiPagingAdapter: NotiPagingAdapter


    override fun getViewBinding(): FragmentNotiSystemBinding =
        FragmentNotiSystemBinding.inflate(layoutInflater)

    override fun onBackFragment() {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewStateChange()
        initPaging()
    }


    private fun initViewStateChange() {

        viewModel.isHaveData.observe(viewLifecycleOwner) { isChange ->
            binding.apply {
                tvEmpty.visibility = if (isChange) View.GONE else View.VISIBLE
                rvSystemNoti.visibility = if (isChange) View.VISIBLE else View.GONE
            }
        }
    }


    private fun initPaging() {
        viewModel.getListNoti()
        lifecycleScope.launch {
            with(viewModel) {
                notiPagingFlow.collectLatest { pagingData ->
                    notiPagingAdapter.submitData(pagingData)
                }
                notiPagingAdapter.loadStateFlow.collectLatest {
//                    binding.swipeRefreshMyTip.isRefreshing = it.refresh is LoadState.Loading
                }
                notiPagingAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.rvSystemNoti.scrollToPosition(0) }


            }

        }
    }

    private fun initView() {
        binding.apply {
            rvSystemNoti.apply {
                adapter = notiPagingAdapter
                layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
                adapter = notiPagingAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(notiPagingAdapter),
                    footer = PagingLoadStateAdapter(notiPagingAdapter)
                )
            }


        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).checkInternet()
    }


}