package com.pools.store.presentation.profile.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.playground.pools_sso.AppUtils
import com.pools.store.R
import com.pools.store.base.BaseFragment
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.FragmentProfileBinding
import com.pools.store.extension.formatNumber
import com.pools.store.extension.textGradientColor
import com.pools.store.presentation.ShareViewModel
import com.pools.store.presentation.main.MainActivity
import com.pools.store.presentation.profile.viewModel.ProfileViewModel
import com.pools.store.utils.AppFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)
    override val viewModel: ProfileViewModel by viewModels()

    private val sharedViewModel: ShareViewModel by activityViewModels()

    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initShareViewModelChange()

    }
    private fun initShareViewModelChange() {

        sharedViewModel.isProfileChange.observe(viewLifecycleOwner) { isChange ->
            if (isChange) {
                binding.apply {
                    binding.navHeaderLayout.tvCoin.text = formatNumber(cachePreferencesHelper.pointUser)
                    Glide.with(binding.root.context).load(cachePreferencesHelper.avatarUser).into(ivProfile)
                    tvName.text = cachePreferencesHelper.nameUser
                    tvId.text = "# ${AppFormatter().formatStringHide(cachePreferencesHelper.codeUser)}"
                }

                /**
                 * nav header
                 * */
                binding.navHeaderLayout.ivGift.setOnClickListener {
//                    findNavController().navigate(R.id.action_homeFragment_to_giftFragment)
                }
                binding.navHeaderLayout.llCoin.setOnClickListener {
                    AppUtils.openAppPassDataIsPackageName(
                        requireContext(),
                        AppUtils.POOL_DASHBOARD_PACKAGE_NAME
                    )
                }
                binding.cardAvatar.setOnClickListener {
                    try {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("pooldashboard://Profile"))
                        startActivity(browserIntent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace(System.out)
                    }
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
        }
    }
    private fun initView() {

       binding.apply {
            navHeaderLayout.tvCoin.paint.setShader(textGradientColor(binding.navHeaderLayout.tvCoin))
           linearManagerApp.setOnClickListener {
               findNavController().navigate(R.id.action_profileFragment_to_managerAppFragment)
           }
           /**
            * go to favorite fragment
            * */
        lnlFavorite.setOnClickListener{
               findNavController().navigate(R.id.action_profileFragment_to_favoriteFragment)
           }

           /**
            * nav header
            * */
           navHeaderLayout.ivGift.setOnClickListener {
               findNavController().navigate(R.id.action_homeFragment_to_giftFragment)
           }

          navHeaderLayout.edtSearch.setOnClickListener {
               findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
           }
          navHeaderLayout.edtSearch.setOnFocusChangeListener { v, hasFocus ->
               if (hasFocus) {
                   findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
               }
           }
           navHeaderLayout.tvCoin.text = cachePreferencesHelper.pointUser.toString()

       }


//        binding.navHeaderLayout.tvCoin.paint.setShader(textGradientColor(binding.navHeaderLayout.tvCoin))
        binding.navHeaderLayout.tvCoin.text = formatNumber(cachePreferencesHelper.pointUser)

    }


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).checkInternet()
    }
}