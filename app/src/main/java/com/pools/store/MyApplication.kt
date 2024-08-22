package com.pools.store

import android.app.Application
import com.pools.store.ads.AdmobHelp
import com.pools.store.base.BaseActivity
import com.pools.store.base.BaseFragment
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication :Application(){
    companion object {

        lateinit var instance: MyApplication
        private var activityList: ArrayList<BaseActivity<*, *>>? = null
        private var fragmentList: ArrayList<BaseFragment<*, *>>? = null

    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        AdmobHelp.instance?.init(this)
    }
}