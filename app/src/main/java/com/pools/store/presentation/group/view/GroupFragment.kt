package com.pools.store.presentation.group.view


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.pools.store.R
import com.pools.store.base.BaseFragment
import com.pools.store.base.PagingLoadStateAdapter
import com.pools.store.databinding.FragmentGroupBinding
import com.pools.store.presentation.group.adapter.GroupPagingAdapter

import com.pools.store.presentation.group.viewModel.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class GroupFragment : BaseFragment<FragmentGroupBinding, GroupViewModel>() {
    override fun getViewBinding() = FragmentGroupBinding.inflate(layoutInflater)
    override val viewModel: GroupViewModel by viewModels()

    @Inject
    lateinit var groupPagingAdapter: GroupPagingAdapter

    val args: GroupFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initPaging()
    }

    override fun onBackFragment() {
        super.onBackFragment()
        findNavController().navigateUp()
    }

    @SuppressLint("RestrictedApi")
    private fun initView() {
        binding.apply {
            rvGroup.apply {
                adapter = groupPagingAdapter
                layoutManager = GridLayoutManager(context, 2)
                adapter = groupPagingAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(groupPagingAdapter),
                    footer = PagingLoadStateAdapter(groupPagingAdapter)
                )
                groupPagingAdapter.setItemClickListener {
                }
            }
            ivLeft.setOnClickListener {
                onBackFragment()
            }
            tvNameCategory.text = args.dataApp.name


            groupPagingAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }
        }
    }

    private fun initPaging() {
        viewModel.getListGroup(args.dataApp.id)
        lifecycleScope.launch {
            with(viewModel) {
                groupPagingFlow.collectLatest { pagingData ->
                    groupPagingAdapter.submitData(pagingData)
                }
                groupPagingAdapter.loadStateFlow.collectLatest {
                }
                groupPagingAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.rvGroup.scrollToPosition(0) }
            }

        }
    }

}