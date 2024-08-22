package com.pools.store.presentation.search.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.internal.ViewUtils
import com.pools.store.R
import com.pools.store.base.BaseFragment
import com.pools.store.base.PagingLoadStateAdapter
import com.pools.store.databinding.FragmentSearchBinding
import com.pools.store.extension.hideKeyboard
import com.pools.store.presentation.main.MainActivity
import com.pools.store.presentation.search.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {
    override fun getViewBinding() = FragmentSearchBinding.inflate(layoutInflater)
    override val viewModel: SearchViewModel by viewModels()
    @Inject
    lateinit var searchPagingAdapter: SearchPagingAdapter
    private var searchJob: Job? = null

    private var isKeyboardShowing = false

    override fun onBackFragment() {
        super.onBackFragment()
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewStateChange()
        initPaging()
    }

    private fun initViewStateChange() {

        viewModel.isSearchHaveData.observe(viewLifecycleOwner) { isChange ->
            binding.apply {
                tvEmpty.visibility = if (isChange) View.GONE else View.VISIBLE
                rvSearch.visibility = if (isChange) View.VISIBLE else View.GONE
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initView() {
        binding.apply {

            binding.cltSearch.viewTreeObserver.addOnGlobalLayoutListener {   // detect to hide show bottomView when keyboard show
                try {
                    Timber.i("addOnGlobalLayoutListener")
                    val heightDiff =
                        binding.cltSearch.rootView.height - binding.cltSearch.height
                    if (heightDiff > ViewUtils.dpToPx(requireContext(), 200) && !isKeyboardShowing) {
                        // Keyboard is showing, adjust BottomNavigationView position
                        isKeyboardShowing = true
                        Timber.i("isKeyboardShowing = true")
                        binding.viewBottom.visibility = View.GONE
                        (requireActivity() as MainActivity).hideBottomView()
                        //  binding.bottomNavigationViewMain.translationY = heightDiff.toFloat()
                    } else if (heightDiff < ViewUtils.dpToPx(requireContext(), 200) && isKeyboardShowing) {
                        // Keyboard is hidden, reset BottomNavigationView position
                        isKeyboardShowing = false
                        Timber.i("isKeyboardShowing = false")
                        binding.viewBottom.visibility = View.VISIBLE
                        (requireActivity() as MainActivity).showBottomView()
                        // binding.bottomNavigationViewMain.translationY = 0f
                    }
                } catch (_: Exception) {
                }
            }
            rvSearch.apply {
                adapter = searchPagingAdapter
                layoutManager = GridLayoutManager(context, 2)
                adapter = searchPagingAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(searchPagingAdapter),
                    footer = PagingLoadStateAdapter(searchPagingAdapter)
                )

                searchPagingAdapter.setItemClickListener {

                }
            }

            edtSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch()
                }
                true
            }

            edtSearch.doAfterTextChanged {
                viewModel.searchString.value = it.toString()

                searchJob?.cancel() // Cancel the previous search job if it exists
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(2000) // Delay for 2 seconds
                    performSearch()
                }
            }

            ivLeft.setOnClickListener {
                onBackFragment()
            }


            searchPagingAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_searchFragment_to_detailGameFragment,bundle)
            }

        }



    }


    private fun initPaging() {
        viewModel.getListSearchApp()
        lifecycleScope.launch {
            with(viewModel) {
                searchAppPagingFlow.collectLatest { pagingData ->
                    searchPagingAdapter.submitData(pagingData)
                }
                searchPagingAdapter.loadStateFlow.collectLatest {
//                    binding.swipeRefreshMyTip.isRefreshing = it.refresh is LoadState.Loading
                }
                searchPagingAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.rvSearch.scrollToPosition(0) }


            }

        }
    }

    private fun performSearch() {
        searchPagingAdapter.refresh()
        if(activity != null){
            requireActivity().hideKeyboard()
        }
    }

}