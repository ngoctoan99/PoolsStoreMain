package com.pools.store.presentation.tabOption.category.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.R
import com.pools.store.base.BaseFragment
import com.pools.store.base.PagingLoadStateAdapter
import com.pools.store.databinding.FragmentCategoryBinding
import com.pools.store.presentation.tabOption.category.adapter.CategoryPagingAdapter
import com.pools.store.presentation.tabOption.category.viewModel.CategoryViewModel
import com.pools.store.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding, CategoryViewModel>() {

    override fun getViewBinding() = FragmentCategoryBinding.inflate(layoutInflater)
    override val viewModel: CategoryViewModel by viewModels()

    @Inject
    lateinit var categoryPagingAdapter:CategoryPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initPaging()
    }
    @SuppressLint("RestrictedApi")
    private fun initView() {
        binding.apply {
            rvCategory.apply {
                adapter = categoryPagingAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = categoryPagingAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(categoryPagingAdapter),
                    footer = PagingLoadStateAdapter(categoryPagingAdapter)
                )
                categoryPagingAdapter.setItemClickListener {
                }
            }

            categoryPagingAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_game_to_group, bundle)
            }
        }
    }

    private fun initPaging() {
        viewModel.getListCategory(Constant.APPLICATION)
        lifecycleScope.launch {
            with(viewModel) {
                groupPagingFlow.collectLatest { pagingData ->
                    categoryPagingAdapter.submitData(pagingData)
                }
                categoryPagingAdapter.loadStateFlow.collectLatest {
                }
                categoryPagingAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.rvCategory.scrollToPosition(0) }
            }

        }
    }
}