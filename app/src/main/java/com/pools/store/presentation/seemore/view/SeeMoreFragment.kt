package com.pools.store.presentation.seemore.view

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
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.pools.store.R
import com.pools.store.base.BaseFragment
import com.pools.store.base.PagingLoadStateAdapter
import com.pools.store.databinding.FragmentSeeMoreBinding
import com.pools.store.presentation.seemore.adapter.SeeMorePagingAdapter
import com.pools.store.presentation.seemore.viewModel.SeeMoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeeMoreFragment : BaseFragment<FragmentSeeMoreBinding, SeeMoreViewModel>() {

    override fun getViewBinding() = FragmentSeeMoreBinding.inflate(layoutInflater)
    override val viewModel: SeeMoreViewModel by viewModels()

    @Inject
    lateinit var seeMorePagingAdapter:SeeMorePagingAdapter

    val args: SeeMoreFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
//        initData()
        initPaging()
    }

    override fun onBackFragment() {
        super.onBackFragment()
        findNavController().navigateUp()
    }

    private fun initData() {
        viewModel.isSeeMoreHaveData.observe(viewLifecycleOwner) { isChange ->
            binding.apply {
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initView() {
        binding.apply {
            rvSeeMore.apply {
                adapter = seeMorePagingAdapter
                layoutManager = GridLayoutManager(context, 2)
                adapter = seeMorePagingAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(seeMorePagingAdapter),
                    footer = PagingLoadStateAdapter(seeMorePagingAdapter)
                )
                seeMorePagingAdapter.setItemClickListener {
                }
            }
            ivLeft.setOnClickListener {
                onBackFragment()
            }
            tvSeeMoreTitle.text =  args.keySeeMoreTitle


            seeMorePagingAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment, bundle)
            }
        }
    }

    private fun initPaging() {
        viewModel.getListSeeMoreApp(args.keySeeMoreTag)
        lifecycleScope.launch {
            with(viewModel) {
                seeMoreAppPagingFlow.collectLatest { pagingData ->
                    seeMorePagingAdapter.submitData(pagingData)
                }
                seeMorePagingAdapter.loadStateFlow.collectLatest {
                }
                seeMorePagingAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.rvSeeMore.scrollToPosition(0) }
            }

        }
    }
}