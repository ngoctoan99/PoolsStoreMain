package com.pools.store.presentation.game.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.playground.pools_sso.AppUtils
import com.pools.store.R
import com.pools.store.ads.AdmobHelp
import com.pools.store.base.BaseFragment
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.FragmentGameBinding
import com.pools.store.extension.formatNumber
import com.pools.store.presentation.ShareViewModel
import com.pools.store.presentation.app.viewModel.AppViewModel
import com.pools.store.presentation.game.viewModel.GameViewModel
import com.pools.store.presentation.game.viewModel.GetBannerState
import com.pools.store.presentation.home.adapter.AppAdapter
import com.pools.store.presentation.home.adapter.NewPageAdapter
import com.pools.store.presentation.tabOptionGame.adapter.TabOptionGameAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@AndroidEntryPoint
class GameFragment : BaseFragment<FragmentGameBinding, GameViewModel>() {

    override fun getViewBinding() = FragmentGameBinding.inflate(layoutInflater)
    override val viewModel: GameViewModel by viewModels()
    private var newPageAdapter: NewPageAdapter? = null
    var timer: Timer? = null

    @Inject
    lateinit var forYouAdapter: AppAdapter


    private val sharedViewModel: ShareViewModel by activityViewModels()

    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper

    private val fragmentHeights = mutableMapOf<Int, Int>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
        initAdMob()
        initShareViewModelChange()
    }


    private fun initAdMob() {
        AdmobHelp.instance?.loadAdsLargerBanner(requireActivity(), binding.frmAdsBanner)
    }
    private fun initView() {
        binding.apply {


            /**
             * nav header
             * */

            binding.navHeaderLayout.ivGift.setOnClickListener {
//                findNavController().navigate(R.id.action_homeFragment_to_giftFragment)
            }
            binding.navHeaderLayout.llCoin.setOnClickListener {
                AppUtils.openAppPassDataIsPackageName(
                    requireContext(),
                    AppUtils.POOL_DASHBOARD_PACKAGE_NAME
                )
            }

            binding.navHeaderLayout.edtSearch.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }
            binding.navHeaderLayout.edtSearch.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
                }
            }
        }
        setDataTabOptionViewPaper2()
    }

    private fun initData() {
        viewModel.getBannerState.mapNotNull { it }.onEach(this::onViewStateBannerChange)
            .launchIn(lifecycleScope)
        viewModel.getBanner(CachePreferencesHelper(requireContext()).accessToken)

    }

    private fun initShareViewModelChange() {
        sharedViewModel.isProfileChange.observe(viewLifecycleOwner) { isChange ->
            if (isChange) {
                binding.navHeaderLayout.tvCoin.text = formatNumber(cachePreferencesHelper.pointUser)
            }
        }
    }


    private fun setDataTabOptionViewPaper2() {
        binding.apply {
            viewPaperOptionChoseGame.adapter = TabOptionGameAdapter(requireActivity())
            viewPaperOptionChoseGame.isUserInputEnabled = false
            TabLayoutMediator(tabLayoutGame, viewPaperOptionChoseGame) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.title_tab_leader_board)
                    1 -> getString(R.string.title_tab_offer)
                    2 -> getString(R.string.title_tab_category)
                    else -> ""
                }
            }.attach()
        }
//        binding.viewPaperOptionChoseGame.registerOnPageChangeCallback(object :
//            ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                updateViewPagerHeight(position)
//            }
//        })
//        binding.viewPaperOptionChoseGame.post {
//            updateViewPagerHeight(binding.viewPaperOptionChoseGame.currentItem)
//        }
    }

//    private fun setDataSlidePhotoAdapter(listNewDomain : List<String>) {
//        newPageAdapter = NewPageAdapter(listNewDomain, binding.root.context)
//        binding.viewPaperNew.setAdapter(newPageAdapter)
//        binding.circle.setViewPager(binding.viewPaperNew)
//        newPageAdapter!!.registerDataSetObserver(binding.circle.getDataSetObserver());
//        autoTransation(listNewDomain)
//    }
//
//    private fun autoTransation(listNewDomain: List<String>) {
//        if (listNewDomain == null || listNewDomain.isEmpty()) {
//            return
//        }
//        if (timer == null) {
//            timer = Timer()
//        }else {
//            timer!!.cancel()
//            timer = Timer()
//        }
//        timer!!.schedule(object : TimerTask() {
//            override fun run() {
//                Handler(Looper.getMainLooper()).post {
//                    var currentItem = binding.viewPaperNew.currentItem
//                    val totalItem = listNewDomain.size - 1
//                    if (currentItem < totalItem) {
//                        currentItem++
//                        binding.viewPaperNew.setCurrentItem(currentItem)
//                    } else {
//                        binding.viewPaperNew.setCurrentItem(0)
//                    }
//                }
//            }
//
//        }, 500, 3000)
//    }


    private fun onViewStateBannerChange(event: GetBannerState) {
        when (event) {
            is GetBannerState.Error -> {
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)
                Log.d("TTT onViewStateBannerChange Error:", " ${event.error} ${event.statusCode}")

            }

            is GetBannerState.Loading -> {
                handleLoading(event.isLoading)
            }

            is GetBannerState.Success -> {
                handleLoading(false)
                Log.d("TTT onViewStateBannerChange Success:"," ${event.data}")
//                setDataSlidePhotoAdapter(event.data.items[0].imageUrls)
            }
        }
    }

//    private fun onViewStateListAppChange(event: GetListAppState) {
//        when (event) {
//            is GetListAppState.Error -> {
//                handleLoading(false)
//                handleErrorMessage(message = event.error, statusCode = event.statusCode)
//                Log.d("GAME FRAGMENT onViewStateListAppChange Error:", " ${event.error} ${event.statusCode}")
//
//            }
//
//            is GetListAppState.Loading -> {
//                handleLoading(event.isLoading)
//            }
//
//            is GetListAppState.Success -> {
//                handleLoading(false)
//                Log.d("GAME FRAGMENT onViewStateListAppChange Success:", " ${event.data}")
//
//                forYouAdapter.list = event.data.items
//                forYouAdapter.notifyDataSetChanged()
//
//            }
//        }
//    }

}