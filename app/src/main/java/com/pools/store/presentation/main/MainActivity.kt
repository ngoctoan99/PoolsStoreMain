package com.pools.store.presentation.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log


import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController

import com.playground.pools_sso.AppUtils
import com.pools.store.R
import com.pools.store.base.BaseActivity
import com.pools.store.base.BaseViewModel
import com.pools.store.core.DialogErrorNetWork
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.ActivityMainBinding
import com.pools.store.extension.MANDATORY_PERMISSIONS_APP
import com.pools.store.extension.REQUEST_PERMISSIONS_CODE_POST_NOTIFICAION
import com.pools.store.extension.convertJsonToObject
import com.pools.store.extension.convertObjectToJson
import com.pools.store.extension.getCurrentLanguageCode
import com.pools.store.extension.isInternetAvailable
import com.pools.store.extension.restartActivity
import com.pools.store.presentation.ShareViewModel
import com.pools.store.presentation.login.view.LoginActivity


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Objects
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override val viewModel: MainViewModel by viewModels()
    private val sharedViewModel: ShareViewModel by viewModels()

    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private   var dialogErrorNetWork: DialogErrorNetWork?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkStatusPermissionPosNotification()

        }
        initViewStateChange()
//        Toast.makeText(this,"Language : ${getCurrentLanguageCode()} // ${convertJsonToObject(cachePreferencesHelper.languageApp).data.login}",Toast.LENGTH_SHORT).show()
    }

    private fun initViewStateChange() {
        viewModel.getProfileState.mapNotNull { it }.onEach(this::onViewStateProfileChange)
            .launchIn(lifecycleScope)
        viewModel.getProfile(cachePreferencesHelper.accessToken)

        viewModel.postUserState.mapNotNull { it }.onEach(this::onViewStatePostUserChange)
            .launchIn(lifecycleScope)
        viewModel.postUser(cachePreferencesHelper.accessToken,cachePreferencesHelper.fcmToken)

        viewModel.getLanguageState.mapNotNull { it }.onEach(this::onViewStateGetLanguageChange)
            .launchIn(lifecycleScope)

    }


    override fun onResume() {
        super.onResume()
        if(!Objects.equals(cachePreferencesHelper.languageCode, getCurrentLanguageCode()))
        {
            viewModel.getLanguage(cachePreferencesHelper.accessToken,getCurrentLanguageCode())
        }

        val token = AppUtils.checkLogin(this)
        if(!token){
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }else {
            cachePreferencesHelper.accessToken = "Bearer ${AppUtils.getTokenLogin(this)["accessToken"]}"
            viewModel.getProfile(cachePreferencesHelper.accessToken)
            viewModel.postUser(cachePreferencesHelper.accessToken,cachePreferencesHelper.fcmToken)
        }
    }

    private fun setupBottomNavigationBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.navigationHostFragment
        ) as NavHostFragment

        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        binding.bottomNavigationViewMain.setupWithNavController(navController)
        binding.bottomNavigationViewMain.itemTextAppearanceInactive =
            R.style.MyBottomNavigationTitleText
        binding.bottomNavigationViewMain.itemTextAppearanceActive =
            R.style.MyBottomNavigationTitleText
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    fun hideBottomView() {
        binding.frmBottom.visibility = View.GONE
        binding.viewBackgroundBottom.visibility = View.GONE
        binding.viewShadow.visibility = View.GONE


    }


    fun showBottomView() {
        binding.frmBottom.visibility = View.VISIBLE
        binding.viewBackgroundBottom.visibility = View.VISIBLE
        binding.viewShadow.visibility = View.VISIBLE

    }

    fun checkInternet(){
        if (!isInternetAvailable(this)) {
            Timber.i("!isInternetAvailable")
            if(dialogErrorNetWork==null){
                dialogErrorNetWork= DialogErrorNetWork()
            }
            if ( !dialogErrorNetWork!!.isVisible && !dialogErrorNetWork!!.isAdded) {
                dialogErrorNetWork!!.show(
                    supportFragmentManager.beginTransaction().remove(dialogErrorNetWork!!),
                    "dialog_error_network"
                )

            }

//            showToast(objectLanguageApp.str_validation_not_internet)

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

    private fun onViewStatePostUserChange(event : PostUserState){
        when(event){
            is PostUserState.Error ->{
            }
            is PostUserState.Loading ->{
            }
            is PostUserState.Success ->{
            }
        }
    }

    private fun checkStatusPermissionPosNotification() {
        // Check if all permissions are granted
        val notGrantedPermissions = MANDATORY_PERMISSIONS_APP["PostNotification"]!!.filter {
            ContextCompat.checkSelfPermission(
                this, it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (notGrantedPermissions.isEmpty()) {


        } else {
            // Request the permissions
            ActivityCompat.requestPermissions(
                this,
                notGrantedPermissions.toTypedArray(),
                REQUEST_PERMISSIONS_CODE_POST_NOTIFICAION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_CODE_POST_NOTIFICAION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions are granted
                // Do something that requires the permissions
            } else {
            }
        }
    }


    private fun onViewStateGetLanguageChange(event : GetLanguageState){
        when(event){
            is GetLanguageState.Error ->{
            }
            is GetLanguageState.Loading ->{
            }
            is GetLanguageState.Success ->{
                cachePreferencesHelper.languageApp = convertObjectToJson(event.data)
                cachePreferencesHelper.languageCode = getCurrentLanguageCode()
                Handler(Looper.getMainLooper()).postDelayed({
                    restartActivity(this)
                }, 1000)
            }
        }
    }


}