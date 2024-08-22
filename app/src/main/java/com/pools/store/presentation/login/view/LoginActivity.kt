package com.pools.store.presentation.login.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.playground.pools_sso.AppUtils
import com.pools.store.base.BaseActivity
import com.pools.store.base.BaseViewModel
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.ActivityLoginBinding
import com.pools.store.extension.convertJsonToObject
import com.pools.store.extension.convertObjectToJson
import com.pools.store.extension.getCurrentLanguageCode
import com.pools.store.extension.restartActivity
import com.pools.store.presentation.login.viewModel.LoginViewModel
import com.pools.store.presentation.main.GetLanguageState
import com.pools.store.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Objects
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, BaseViewModel>() {
    override fun getViewBinding() = ActivityLoginBinding.inflate(layoutInflater)
    override val viewModel: LoginViewModel by viewModels()
    private var alertDialog: AlertDialog? = null

    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            btnLogin.setOnClickListener {
                loginUser()
            }
        }

        if (cachePreferencesHelper.fcmToken.isEmpty()) {
            callToken()
        }
        loginUser()
        initViewStateChange()
        initView()
    }

    private fun initView() {
        binding.btnLogin.text = convertJsonToObject(cachePreferencesHelper.languageApp).data.login
    }


    private fun initViewStateChange() {
        viewModel.getLanguageState.mapNotNull { it }.onEach(this::onViewStateGetLanguageChange)
            .launchIn(lifecycleScope)

    }

    private fun callToken() {
        try {
            FirebaseApp.initializeApp(binding.root.context)
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
                if (!TextUtils.isEmpty(token)) {
                    cachePreferencesHelper.fcmToken = token
//                    Timber.d("retrieve token successful : $token")

                } else {
                    Timber.d("token should not be null...")
                }
            }.addOnFailureListener { _: Exception? -> }.addOnCanceledListener {}
                .addOnCompleteListener { task: Task<String> ->
                    try {
                        Timber.d(
                            "This is the token : " + task.result
                        )
//                        Log.d("TTT","This is the token : " + task.result)
                    } catch (_: Exception) {
                    }
                }
        } catch (_: Exception) {
        }
    }

    private fun loginUser(){
        val token = AppUtils.checkLogin(this)
        if(!token){
            loginUserDialog()
        }else {
            CachePreferencesHelper(this).accessToken = "Bearer ${AppUtils.getTokenLogin(this)["accessToken"]}"
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun loginUserDialog() {
        if(alertDialog == null || alertDialog?.isShowing != true){
            val build = AlertDialog.Builder(this)
            build.setTitle(convertJsonToObject(cachePreferencesHelper.languageApp).data.login)
            build.setMessage(
                convertJsonToObject(cachePreferencesHelper.languageApp).data.pleaseLoginInUsingThePoolsDashboardAppToContinue
            )
            build.setCancelable(false)
            build.setNegativeButton(
                convertJsonToObject(cachePreferencesHelper.languageApp).data.close
            ) { dialog, _ -> dialog.cancel() }
            build.setPositiveButton(convertJsonToObject(cachePreferencesHelper.languageApp).data.login) { dialog, _ ->
                if (AppUtils.isAppInstalled(
                        this,
                        AppUtils.POOL_DASHBOARD_PACKAGE_NAME
                    )
                ) {
                    AppUtils.openAppPassDataIsPackageName(
                        this,
                        AppUtils.POOL_DASHBOARD_PACKAGE_NAME
                    )
                } else {
                    AppUtils.redirectToPlayStore(
                        this,
                        AppUtils.POOL_DASHBOARD_PACKAGE_NAME
                    )
                }
                dialog.cancel()
            }
            alertDialog = build.create()
        }

        alertDialog?.show()
    }

    override fun onResume() {
        super.onResume()
        if(!Objects.equals(cachePreferencesHelper.languageCode, getCurrentLanguageCode()))
        {
        viewModel.getLanguage(cachePreferencesHelper.accessToken,getCurrentLanguageCode())
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
                binding.btnLogin.text = convertJsonToObject(cachePreferencesHelper.languageApp).data.login
                cachePreferencesHelper.languageCode = getCurrentLanguageCode()
                Handler(Looper.getMainLooper()).postDelayed({
                        restartActivity(this)
                    }, 1000)
            }
        }
    }
}