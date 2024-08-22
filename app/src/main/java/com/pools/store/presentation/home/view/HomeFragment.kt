package com.pools.store.presentation.home.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.dpToPx
import com.playground.pools_sso.AppUtils
import com.pools.store.R
import com.pools.store.ads.AdmobHelp
import com.pools.store.base.BaseFragment
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.FragmentHomeBinding
import com.pools.store.extension.formatNumber
import com.pools.store.extension.textGradientColor
import com.pools.store.presentation.ShareViewModel
import com.pools.store.presentation.home.adapter.AppAdapter
import com.pools.store.presentation.home.adapter.NewPageAdapter
import com.pools.store.presentation.home.viewModel.GetBannerState
import com.pools.store.presentation.home.viewModel.GetListAppState
import com.pools.store.presentation.home.viewModel.HomeViewModel
import com.pools.store.presentation.main.MainActivity
import com.pools.store.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)
    override val viewModel: HomeViewModel by viewModels()
    private var newPageAdapter: NewPageAdapter? = null
    var timer: Timer? = null

    @Inject
    lateinit var appForYouAdapter: AppAdapter

    @Inject
    lateinit var appTrendingAdapter: AppAdapter

    @Inject
    lateinit var appTotalSupportAdapter: AppAdapter

    @Inject
    lateinit var appPoolCollectionAdapter: AppAdapter



    private var isKeyboardShowing = false

    private val sharedViewModel: ShareViewModel by activityViewModels()

    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdMob()
        initViewStateChange()
        initView()
        initShareViewModelChange()

    }

    private fun initShareViewModelChange() {
        sharedViewModel.isProfileChange.observe(viewLifecycleOwner) { isChange ->
            if (isChange) {
                binding.navHeaderLayout.tvCoin.text = formatNumber(cachePreferencesHelper.pointUser)
            }
        }
    }

    private fun initAdMob() {
        AdmobHelp.instance?.loadAdsBanner(requireActivity(), binding.frmAds)
        AdmobHelp.instance?.loadAdsLargerBanner(requireActivity(), binding.frmAdsBanner)
    }

    private fun initViewStateChange() {
        viewModel.getListAppForYouState.mapNotNull { it }.onEach(this::onViewStateListAppForYouChange)
            .launchIn(lifecycleScope)
        viewModel.getListAppForYou(cachePreferencesHelper.accessToken,Constant.FOR_YOU)

        viewModel.getListAppTrendingState.mapNotNull { it }.onEach(this::onViewStateListAppTrendingChange)
            .launchIn(lifecycleScope)
        viewModel.getListAppTrending(cachePreferencesHelper.accessToken,Constant.TRENDING)

        viewModel.getListAppTotalSupportState.mapNotNull { it }.onEach(this::onViewStateListAppTotalSupportChange)
            .launchIn(lifecycleScope)
        viewModel.getListAppTotalSupport(cachePreferencesHelper.accessToken,Constant.TOTAL_SUPPORT)

        viewModel.getListAppPoolsCollectionState.mapNotNull { it }.onEach(this::onViewStateListAppPoolCollectionChange)
            .launchIn(lifecycleScope)
        viewModel.getListAppPoolCollection(cachePreferencesHelper.accessToken,Constant.POOLS_COLLECTION)


        viewModel.getBannerState.mapNotNull { it }.onEach(this::onViewStateBannerChange)
            .launchIn(lifecycleScope)
        viewModel.getBanner(cachePreferencesHelper.accessToken)

    }

    @SuppressLint("RestrictedApi")
    private fun initView() {
        binding.apply {
            binding.nested.viewTreeObserver.addOnGlobalLayoutListener {   // detect to hide show bottomView when keyboard show
                try {
                    Timber.i("addOnGlobalLayoutListener")
                    val heightDiff =
                        binding.nested.rootView.height - binding.nested.height
                    if (heightDiff > dpToPx(requireContext(), 200) && !isKeyboardShowing) {
                        // Keyboard is showing, adjust BottomNavigationView position
                        isKeyboardShowing = true
                        Timber.i("isKeyboardShowing = true")
                        binding.viewBottom.visibility = View.GONE
                        (requireActivity() as MainActivity).hideBottomView()
                        //  binding.bottomNavigationViewMain.translationY = heightDiff.toFloat()
                    } else if (heightDiff < dpToPx(requireContext(), 200) && isKeyboardShowing) {
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

            rvForYou.apply {
                adapter = appForYouAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            rvTrending.apply {
                adapter = appTrendingAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            rvTotalSupport.apply {
                adapter = appTotalSupportAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            rvPoolsCollection.apply {
                adapter = appPoolCollectionAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }


            /**
             * event listener
             * */

            appForYouAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment,bundle)
            }

            appTrendingAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment,bundle)
            }
            appTotalSupportAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment,bundle)
            }

            appForYouAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment,bundle)
            }


            appPoolCollectionAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_homeFragment_to_detailGameFragment,bundle)
            }

            tvSeeMoreForYou.setOnClickListener{
                val bundle = Bundle().apply {
                    putString("keySeeMoreTitle",Constant.TITLE_FOR_YOU)
                    putString("keySeeMoreTag", Constant.FOR_YOU)
                }
                findNavController().navigate(R.id.action_seeMore_to_seeMoreFragment, bundle)
            }
            tvSeeMoreTrending.setOnClickListener{
                val bundle = Bundle().apply {
                    putString("keySeeMoreTitle",Constant.TITLE_TRENDING)
                    putString("keySeeMoreTag", Constant.TRENDING)
                }
                findNavController().navigate(R.id.action_seeMore_to_seeMoreFragment, bundle)
            }
            tvSeeMoreTotalSupport.setOnClickListener{
                val bundle = Bundle().apply {
                    putString("keySeeMoreTitle",Constant.TITLE_TOTAL_SUPPORT)
                    putString("keySeeMoreTag", Constant.TOTAL_SUPPORT)
                }
                findNavController().navigate(R.id.action_seeMore_to_seeMoreFragment, bundle)
            }
            tvSeeMorePoolsCollection.setOnClickListener{
                val bundle = Bundle().apply {
                    putString("keySeeMoreTitle",Constant.TITLE_POOLS_COLLECTION)
                    putString("keySeeMoreTag", Constant.POOLS_COLLECTION)
                }
                findNavController().navigate(R.id.action_seeMore_to_seeMoreFragment, bundle)
            }

            /**
             * nav header
             * */

            binding.navHeaderLayout.ivGift.setOnClickListener {
//                findNavController().navigate(R.id.action_homeFragment_to_giftFragment)
            }

            binding.navHeaderLayout.edtSearch.setOnClickListener{
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }
            binding.navHeaderLayout.edtSearch.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
                }
            }

        }

        binding.navHeaderLayout.tvCoin.paint.setShader(textGradientColor(binding.navHeaderLayout.tvCoin))
        binding.navHeaderLayout.llCoin.setOnClickListener {
            AppUtils.openAppPassDataIsPackageName(
                requireContext(),
                AppUtils.POOL_DASHBOARD_PACKAGE_NAME
            )
        }

    }
//    private fun setDataSlidePhotoAdapter(listNewDomain : List<String>) {
//        newPageAdapter = NewPageAdapter(listNewDomain, binding.root.context)
//        binding.viewPaperNew.setAdapter(newPageAdapter)
//        binding.circle.setViewPager(binding.viewPaperNew)
//        newPageAdapter!!.registerDataSetObserver(binding.circle.getDataSetObserver());
//        autoTransation(listNewDomain)
//    }
//    private fun autoTransation(listNewDomain: List<String>) {
//        if (listNewDomain.isEmpty()) {
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

    private fun onViewStateListAppForYouChange(event : GetListAppState){
        when(event){
            is GetListAppState.Error ->{
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)


            }
            is GetListAppState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetListAppState.Success ->{
                handleLoading(false)

                if(event.data.items.isNotEmpty()){
                    binding.apply {
                        rvForYou.visibility = View.VISIBLE
                        tvSeeMoreForYou.visibility = View.VISIBLE
                        tvEmptyForYou.visibility = View.GONE
                        appForYouAdapter.list = event.data.items
                        appForYouAdapter.notifyDataSetChanged()
                    }
                }

            }
        }
    }

    private fun onViewStateListAppTrendingChange(event : GetListAppState){
        when(event){
            is GetListAppState.Error ->{
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)


            }
            is GetListAppState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetListAppState.Success ->{
                handleLoading(false)
                if(event.data.items.isNotEmpty()){
                    binding.apply {
                        rvTrending.visibility = View.VISIBLE
                        tvSeeMoreTrending.visibility = View.VISIBLE
                        tvEmptyTrending.visibility = View.GONE
                        appTrendingAdapter.list = event.data.items
                        appTrendingAdapter.notifyDataSetChanged()
                    }
                }

            }
        }
    }

    private fun onViewStateListAppTotalSupportChange(event : GetListAppState){
        when(event){
            is GetListAppState.Error ->{
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)


            }
            is GetListAppState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetListAppState.Success ->{
                handleLoading(false)



                if(event.data.items.isNotEmpty()){
                    binding.apply {
                        rvTotalSupport.visibility = View.VISIBLE
                        tvSeeMoreTotalSupport.visibility = View.VISIBLE
                        tvEmptyTotalSupport.visibility = View.GONE
                        appTotalSupportAdapter.list = event.data.items
                        appTotalSupportAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun onViewStateListAppPoolCollectionChange(event : GetListAppState){
        when(event){
            is GetListAppState.Error ->{
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)


            }
            is GetListAppState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetListAppState.Success ->{
                handleLoading(false)

                if(event.data.items.isNotEmpty()){
                    binding.apply {
                        rvPoolsCollection.visibility = View.VISIBLE
                        tvSeeMorePoolsCollection.visibility = View.VISIBLE
                        tvEmptyPoolsCollection.visibility = View.GONE
                        appPoolCollectionAdapter.list = event.data.items
                        appPoolCollectionAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }


    private fun onViewStateBannerChange(event : GetBannerState){
        when(event){
            is GetBannerState.Error ->{
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)


            }
            is GetBannerState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetBannerState.Success ->{
                handleLoading(false)
//                setDataSlidePhotoAdapter(event.data.items[0].imageUrls)
            }
        }
    }
}