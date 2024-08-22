package com.pools.store.presentation.detailGame.view


import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.internal.ViewUtils
import com.pools.store.R
import com.pools.store.base.BaseFragment
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.FragmentDetailGameBinding
import com.pools.store.extension.checkVersionApp
import com.pools.store.extension.checkVersionAppBoolean
import com.pools.store.extension.isHttpOrHttpsUrl
import com.pools.store.extension.textGradientColor
import com.pools.store.libs.DownloadController
import com.pools.store.presentation.ShareViewModel
import com.pools.store.presentation.detailGame.adapter.ImageContentAdapter
import com.pools.store.presentation.detailGame.adapter.PreviewAdapter
import com.pools.store.presentation.detailGame.adapter.RatingBarAdapter
import com.pools.store.presentation.detailGame.adapter.TagAdapter
import com.pools.store.presentation.detailGame.viewModel.DetailGameViewModel
import com.pools.store.presentation.detailGame.viewModel.GetDetailAppState
import com.pools.store.presentation.detailGame.viewModel.GetListFavoriteState
import com.pools.store.presentation.detailGame.viewModel.GetListPreviewState
import com.pools.store.presentation.detailGame.viewModel.GetMyRatingState
import com.pools.store.presentation.detailGame.viewModel.PostDownloadState
import com.pools.store.presentation.detailGame.viewModel.PostRatingState
import com.pools.store.presentation.home.adapter.AppAdapter
import com.pools.store.presentation.home.viewModel.GetListAppState
import com.pools.store.presentation.main.GetProfileState
import com.pools.store.presentation.main.MainActivity
import com.pools.store.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Objects
import javax.inject.Inject
import kotlin.text.*


@AndroidEntryPoint
class DetailGameFragment : BaseFragment<FragmentDetailGameBinding, DetailGameViewModel>() {

    override fun getViewBinding() = FragmentDetailGameBinding.inflate(layoutInflater)
    override val viewModel: DetailGameViewModel by viewModels()

    @Inject
    lateinit var ratingBarAdapter: RatingBarAdapter

    @Inject
    lateinit var adsRecommendAdapter: AppAdapter

    @Inject
    lateinit var similarAdapter: AppAdapter

    @Inject
    lateinit var previewAdapter: PreviewAdapter

    @Inject
    lateinit var tagAdapter: TagAdapter


    @Inject
    lateinit var imageContentAdapter: ImageContentAdapter

    lateinit var downloadController: DownloadController

    private var isKeyboardShowing = false
    private var isRating = false
    private var isInstall = false

    private var isStatusInstall = false
    private var REQUEST_WRITE_EXTERNAL_STORAGE = 100
    private val sharedViewModel: ShareViewModel by viewModels()

    private lateinit var  onDownloadSuccess : BroadcastReceiver
    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper
    var publisherId  = ""
    val args: DetailGameFragmentArgs by navArgs()
    override fun onBackFragment() {
        super.onBackFragment()
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateChange()
        iniView()
//        checkAppInDevice()

        onDownloadSuccess = object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                if(action == DownloadManager.ACTION_DOWNLOAD_COMPLETE){
                    showLoading(false)
                }
            }

        }
    }


    private fun initViewStateChange() {
        viewModel.getDetailAppState.mapNotNull { it }.onEach(this::onViewStateDetailAppChange)
            .launchIn(lifecycleScope)
        viewModel.getDetailApp(CachePreferencesHelper(requireContext()).accessToken,args.dataApp.id)
        viewModel.getListAppState.mapNotNull { it }.onEach(this::onViewStateListAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListApp(CachePreferencesHelper(requireContext()).accessToken)

        viewModel.getListPreviewState.mapNotNull { it }.onEach(this::onViewStateListPreviewChange)
            .launchIn(lifecycleScope)
        viewModel.getListPreview(CachePreferencesHelper(requireContext()).accessToken,args.dataApp.id)

        viewModel.postRatingState.mapNotNull { it }.onEach(this::onViewStateRatingPostChange)
            .launchIn(lifecycleScope)

        viewModel.getMyRatingState.mapNotNull { it }.onEach(this::onViewStateMyRatingChange)
            .launchIn(lifecycleScope)
        viewModel.getMyRating(CachePreferencesHelper(requireContext()).accessToken,args.dataApp.id)


        viewModel.postDownloadState.mapNotNull { it }.onEach(this::onViewStatePostDownloadChange)
            .launchIn(lifecycleScope)


        viewModel.getProfileState.mapNotNull { it }.onEach(this::onViewStateProfileChange)
            .launchIn(lifecycleScope)

        viewModel.putAddFavorite.mapNotNull { it }.onEach(this::onViewStateAddFavoriteChange)
            .launchIn(lifecycleScope)
        viewModel.putRemoveFavorite.mapNotNull { it }.onEach(this::onViewStateRemoveFavoriteChange)
            .launchIn(lifecycleScope)
    }

    @SuppressLint("RestrictedApi", "ClickableViewAccessibility")
    private fun iniView() {
        binding.apply {
            binding.cltDetailGame.viewTreeObserver.addOnGlobalLayoutListener {   // detect to hide show bottomView when keyboard show
                try {
                    Timber.i("addOnGlobalLayoutListener")
                    val heightDiff =
                        binding.cltDetailGame.rootView.height - binding.cltDetailGame.height
                    if (heightDiff > ViewUtils.dpToPx(
                            requireContext(),
                            200
                        ) && !isKeyboardShowing
                    ) {
                        // Keyboard is showing, adjust BottomNavigationView position
                        isKeyboardShowing = true
                        Timber.i("isKeyboardShowing = true")
                        binding.viewBottom.visibility = View.GONE
                        (requireActivity() as MainActivity).hideBottomView()
                        //  binding.bottomNavigationViewMain.translationY = heightDiff.toFloat()
                    } else if (heightDiff < ViewUtils.dpToPx(
                            requireContext(),
                            200
                        ) && isKeyboardShowing
                    ) {
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
            cltPreview.visibility = View.GONE

            tvInstall.setOnClickListener {
                if (Objects.equals(tvInstall.text.toString(),Constant.InstallPoint) || Objects.equals(tvInstall.text.toString(),Constant.Update) || Objects.equals(tvInstall.text.toString(),Constant.Install)){
                    if(isHttpOrHttpsUrl(args.dataApp.fileUrl)){
                        if(Objects.equals(tvInstall.text.toString(),Constant.InstallPoint) || Objects.equals(tvInstall.text.toString(),Constant.Install)){
                            isInstall = true
                        }
                        downloadController = DownloadController(binding.root.context, args.dataApp.fileUrl)
                        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
                            downloadController.enqueueDownload()
                            showLoading(true)
                        }else {
                            checkPermissionWrite()
                        }
                    }else {
                        Toast.makeText(binding.root.context,"Link Download invalid",Toast.LENGTH_SHORT).show()
                    }

                }
                else if(Objects.equals(tvInstall.text.toString(),Constant.Open)){
                    openApp(args.dataApp.packageName)
                }

            }



            rvImage.apply {
                adapter = imageContentAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            rvRatingBar.apply {
                adapter = ratingBarAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            rvAdsRecommend.apply {
                adapter = adsRecommendAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            rvSimilar.apply {
                adapter = similarAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            rvPreview.apply {
                adapter = previewAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                addItemDecoration(
                    DividerItemDecoration(
                        rvPreview.context,
                        DividerItemDecoration.VERTICAL
                    )


                )
            }
            rvTag.apply {
                adapter = tagAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }


            ratingBar.setOnTouchListener { v, event ->
                ratingBar.onTouchEvent(event)
            }
            adsRecommendAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_detailGameFragment_to_detailGameFragment,bundle)
            }

            similarAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)
                findNavController().navigate(R.id.action_detailGameFragment_to_detailGameFragment,bundle)
            }

//
//            ratingBarAdapter.list = list2
//            ratingBarAdapter.notifyDataSetChanged()

//            tagAdapter.list = list
//            tagAdapter.notifyDataSetChanged()


            ivSearch.setOnClickListener{
                findNavController().navigate(R.id.action_detailGameFragment_to_searchFragment)
            }


            ivLeft.setOnClickListener {
                onBackFragment()
            }
            tvMoreAbout.setOnClickListener {
                if (Objects.equals(tvMoreAbout.text.toString(),"See More"))
                {
                    tvAbout.setMaxLines(Integer.MAX_VALUE)
                    tvAbout.ellipsize = null
                    tvMoreAbout.text = "See less"
                }
                else
                {
                    tvAbout.ellipsize = TextUtils.TruncateAt.END
                    tvAbout.setMaxLines(5)
                    tvMoreAbout.text = "See More"
                }


            }
            binding.tvWritePreview.paint.setShader(textGradientColor(binding.tvWritePreview))
            cltPreviewRating.setOnClickListener {
                it.visibility = View.GONE
//                binding.ivCoinTv.visibility = View.GONE
                cltPreview.visibility = View.VISIBLE
            }
//            binding.ivCoinTv.visibility = View.GONE
            tvSubmit.setOnClickListener {
                if (binding.ratingBar.rating > 0 && binding.edtPreview.text.trim().isNotEmpty()) {
                    viewModel.postRating(
                        CachePreferencesHelper(requireContext()).accessToken,
                        binding.ratingBar.rating.toInt(),
                        binding.edtPreview.text.trim().toString(),
                        args.dataApp.id
                    )
                } else {
                    Toast.makeText(
                        binding.root.context,
                        "Please fill full infor to rating",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                binding.ratingBar.rating  = 0f
                binding.edtPreview.text.clear()
            }
            tvAuthor.setOnClickListener {
                val bundle = bundleOf("publisherId" to publisherId)
                findNavController().navigate(R.id.action_detailGameFragment_to_publisherNameFragment,bundle)
            }

            lnlShare.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, args.dataApp.fileUrl)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }


            ivFavorite.setOnClickListener {
                viewModel.addFavoriteApp(
                    CachePreferencesHelper(requireContext()).accessToken,
                    args.dataApp.id
                )
            }
            ivFavoriteFull.setOnClickListener {
                viewModel.removeFavoriteApp(
                    CachePreferencesHelper(requireContext()).accessToken,
                    args.dataApp.id
                )
            }
        }

    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressInstall.visibility = View.VISIBLE
            binding.tvInstall.visibility = View.GONE
            binding.ivCoin.visibility = View.GONE
        } else {
            binding.progressInstall.visibility = View.GONE
            binding.tvInstall.visibility = View.VISIBLE
            binding.ivCoin.visibility = View.VISIBLE
        }
    }


    private fun onViewStateListPreviewChange(event: GetListPreviewState) {
        when (event) {
            is GetListPreviewState.Error -> {
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)

            }

            is GetListPreviewState.Loading -> {
                handleLoading(event.isLoading)
            }

            is GetListPreviewState.Success -> {
                handleLoading(false)

                if(event.data.items.isNotEmpty()){
                    previewAdapter.list = event.data.items
                    previewAdapter.notifyDataSetChanged()
                    binding.tvEmptyPreview.visibility = View.GONE
                    binding.rvPreview.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onViewStateRatingPostChange(event: PostRatingState) {
        when (event) {
            is PostRatingState.Error -> {
                handleLoading(false)
                if(viewModel.isCanCallAPIPostRating){
                    viewModel.isCanCallAPIPostRating = false
                    handleErrorMessage(message = event.error, statusCode = event.statusCode)
                    Toast.makeText(requireContext(), event.error,Toast.LENGTH_SHORT).show()
                }

            }

            is PostRatingState.Loading -> {
                handleLoading(event.isLoading)
            }

            is PostRatingState.Success -> {
                handleLoading(false)
                if(viewModel.isCanCallAPIPostRating){
                    viewModel.isCanCallAPIPostRating = false
                    binding.apply {
                        cltPreviewRating.visibility = View.GONE
//                        binding.ivCoinTv.visibility = View.GONE
                        cltPreview.visibility = View.GONE
                    }
                    viewModel.getListPreview(CachePreferencesHelper(binding.root.context).accessToken,args.dataApp.id)
                    viewModel.getDetailApp(CachePreferencesHelper(requireContext()).accessToken,args.dataApp.id)
                    viewModel.getProfile(cachePreferencesHelper.accessToken)
                }

            }
        }
    }


    private fun onViewStateMyRatingChange(event: GetMyRatingState) {
        when (event) {
            is GetMyRatingState.Error -> {
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)
            }

            is GetMyRatingState.Loading -> {
                handleLoading(event.isLoading)
            }

            is GetMyRatingState.Success -> {
                handleLoading(false)
                binding.apply {
                  if (event.data.star > 0){

                      isRating = true
                      cltPreviewRating.visibility = View.GONE
//                      binding.ivCoinTv.visibility = View.GONE
                  }
                }
            }
        }
    }

    private fun onViewStateListAppChange(event: GetListAppState) {
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

                if (event.data.items.isNotEmpty()){
                    binding.apply {
                        adsRecommendAdapter.list = event.data.items
                        adsRecommendAdapter.notifyDataSetChanged()

                        similarAdapter.list = event.data.items
                        similarAdapter.notifyDataSetChanged()

                        rvAdsRecommend.visibility = View.VISIBLE
                        tvEmptyAdsRecommend.visibility = View.GONE

                        rvSimilar.visibility = View.VISIBLE
                        tvEmptySimilar.visibility = View.GONE



                    }
                }



            }
        }
    }


    @SuppressLint("DefaultLocale")
    private fun onViewStateDetailAppChange(event: GetDetailAppState) {
        when (event) {
            is GetDetailAppState.Error -> {
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)
            }

            is GetDetailAppState.Loading -> {
                handleLoading(event.isLoading)
            }

            is GetDetailAppState.Success -> {
                handleLoading(false)
                    binding.apply {
                        val totalRating = event.data.detail.ratingStar.star1 + event.data.detail.ratingStar.star2 + event.data.detail.ratingStar.star3 + event.data.detail.ratingStar.star4  +event.data.detail.ratingStar.star5
                        val star = String.format("%.1f", event.data.detail.averageStar)
                        val preview = "(${totalRating} Reviews)"
//                        val size = "${event.data.detail.size / 1024} MB"
                        tvName.text = event.data.detail.name
                        tvAuthor.text = event.data.detail.publisherId.name
                        publisherId = event.data.detail.publisherId.id
                        if(event.data.isFavorite) {
                            ivFavoriteFull.visibility = View.VISIBLE
                            ivFavorite.visibility = View.GONE
                        } else {
                            ivFavoriteFull.visibility = View.GONE
                            ivFavorite.visibility = View.VISIBLE
                        }




                        tvStart.text = star
                        tvNumberPreview.text = preview
                        tvSize.text =event.data.detail.size
                        tvDownload.text = event.data.detail.countDownload.toString()
                        if (event.data.detail.pricing > 0)
                        {
                            tvGenre.text = event.data.detail.pricing.toString()
                        }
                        tvAbout.setEllipsize(TextUtils.TruncateAt.END)
                        tvAbout.setMaxLines(5)
                        tvAbout.text = event.data.detail.about

                        tvRatingStar.text = star
                        tvTotalReviews.text = preview

                        tagAdapter.list = event.data.detail.tags
                        tagAdapter.notifyDataSetChanged()

                        imageContentAdapter.list = event.data.detail.imageContentUrls
                        imageContentAdapter.notifyDataSetChanged()
                        val listStar = listOf(event.data.detail.ratingStar.star1,event.data.detail.ratingStar.star2,event.data.detail.ratingStar.star3,event.data.detail.ratingStar.star4,event.data.detail.ratingStar.star5)
                        ratingBarAdapter.list = listStar
                        ratingBarAdapter.notifyDataSetChanged()
                        Glide.with(binding.root.context).load(event.data.detail.iconUrl).into(ivGameChild)
                        if(!isStatusInstall){
                            if(event.data.limitClaimInstall == 0L){
                                tvInstall.text  = Constant.Install
                                ivCoin.visibility = View.GONE
                            }else {
                                tvInstall.text  = Constant.InstallPoint
                                ivCoin.visibility = View.VISIBLE
                            }
                        }

                        if(event.data.limitClaimRating == 0L){
                            tvWritePreview.text  = resources.getString(R.string.title_detail_write_a_review_not_point)
                            ivCoinTv.visibility = View.GONE
                        }else {
                            tvWritePreview.text  = resources.getString(R.string.title_detail_write_a_review_add_point)
                            ivCoinTv.visibility = View.VISIBLE
                        }


                    }
            }
        }
    }


    private fun checkPermissionWrite(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                requireActivity(), arrayOf<String>(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ),
                REQUEST_WRITE_EXTERNAL_STORAGE
            )
        }else {
            downloadController.enqueueDownload()
            showLoading(true)
        }
    }

    private fun onViewStatePostDownloadChange(event: PostDownloadState) {
        when (event) {
            is PostDownloadState.Error -> {
                handleLoading(false)
                if(viewModel.isCanCallAPIPostDownload){
                    viewModel.isCanCallAPIPostDownload = false
                    handleErrorMessage(message = event.error, statusCode = event.statusCode)
                    Toast.makeText(requireContext(), event.error,Toast.LENGTH_SHORT).show()
                }

            }

            is PostDownloadState.Loading -> {
                handleLoading(event.isLoading)
            }

            is PostDownloadState.Success -> {
                handleLoading(false)
                if(viewModel.isCanCallAPIPostDownload){
                    viewModel.isCanCallAPIPostDownload = false
                    if(!isRating){
                        binding.cltPreviewRating.visibility = View.VISIBLE
//                        binding.ivCoinTv.visibility = View.VISIBLE
                    }

                    viewModel.getProfile(cachePreferencesHelper.accessToken)
                    viewModel.getDetailApp(CachePreferencesHelper(requireContext()).accessToken,args.dataApp.id)
                }

            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkAppInDevice(){
        if(!Objects.equals(checkVersionApp(args.dataApp.packageName,binding.root.context),args.dataApp.version) && checkVersionAppBoolean(args.dataApp.packageName,binding.root.context)){
            binding.tvInstall.text = Constant.Update
            binding.ivCoin.visibility = View.GONE
            isStatusInstall = true
        } else if(checkVersionAppBoolean(args.dataApp.packageName,binding.root.context) && Objects.equals(checkVersionApp(args.dataApp.packageName,binding.root.context),args.dataApp.version)){
            binding.tvInstall.text = Constant.Open
            binding.ivCoin.visibility = View.GONE
            isStatusInstall = true
            if(isInstall){
                viewModel.postRatingDownload(CachePreferencesHelper(requireContext()).accessToken,args.dataApp.id)
                isInstall = false
            }


        } else {
//            binding.tvInstall.text = Constant.InstallPoint
            isStatusInstall = false
            binding.cltPreviewRating.visibility = View.GONE
            binding.ivCoinTv.visibility = View.GONE
            binding.ivCoin.visibility = View.GONE
        }


    }

    override fun onResume() {
        super.onResume()
        checkAppInDevice()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requireContext().registerReceiver(
                onDownloadSuccess, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                Context.RECEIVER_EXPORTED)
        }else {
            requireContext().registerReceiver(onDownloadSuccess, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        requireContext().unregisterReceiver(onDownloadSuccess)
    }


    private fun openApp(packageName: String) {
        val pm: PackageManager = requireContext().packageManager
        try {
            // Check if the package is installed
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            // Create an intent to launch the app
            val launchIntent: Intent? = pm.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                startActivity(launchIntent)
            } else {
                Toast.makeText(requireContext(), "Unable to find launch intent for package: $packageName", Toast.LENGTH_LONG).show()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // App is not installed, redirect to Play Store
            val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            if (playStoreIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(playStoreIntent)
            } else {
                Toast.makeText(requireContext(), "App not installed and Play Store not available.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onViewStateProfileChange(event : GetProfileState){
        when(event){
            is GetProfileState.Error ->{
            }
            is GetProfileState.Loading ->{
            }
            is GetProfileState.Success ->{
                sharedViewModel.isProfileChange.value = true
            }
        }
    }

    private fun onViewStateAddFavoriteChange(event : GetListFavoriteState){
        when(event){

            is GetListFavoriteState.Error ->{
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)
                Log.d("TTT E"," ${event.error} ${event.statusCode}")
//                Toast.makeText(requireContext(), "Add favorite failed", Toast.LENGTH_SHORT).show()
            }
            is GetListFavoriteState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetListFavoriteState.Success ->{
                handleLoading(false)
                Log.i("TTT S"," ${event}")
                binding.apply {
                    ivFavorite.visibility = View.GONE
                    ivFavoriteFull.visibility = View.VISIBLE
                }

            }
        }
    }

    private fun onViewStateRemoveFavoriteChange(event : GetListFavoriteState){
        when(event){

            is GetListFavoriteState.Error ->{
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)
                Log.d("TTT E"," ${event.error} ${event.statusCode}")
//                Toast.makeText(requireContext(), "Remove favorite failed", Toast.LENGTH_SHORT).show()
            }
            is GetListFavoriteState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetListFavoriteState.Success ->{
                handleLoading(false)
                Log.i("TTT S"," ${event}")
                binding.apply {
                    ivFavoriteFull.visibility = View.GONE
                    ivFavorite.visibility = View.VISIBLE

                }

            }
        }
    }

}