package com.pools.store.ads

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.ViewGroup
import com.google.android.gms.ads.*


class AdmobHelp private constructor() {
    fun init(context: Context) {
        MobileAds.initialize(context) {}
    }

    fun loadAdsBanner(activity: Activity, container: ViewGroup) {

        val layoutParams: ViewGroup.LayoutParams = container.layoutParams
        layoutParams.height = (AdSize.BANNER.getHeightInPixels(activity))
      //  layoutParams.height = (AdSize.BANNER.getHeightInPixels(activity) + activity.resources.getDimension(com.playgroundx.airo.R.dimen.dimen10dp)).toInt()//this is in pixels

        container.layoutParams = layoutParams

        val adView = AdView(activity)
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = activity.getString(com.pools.store.R.string.str_banner_ads)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        container.addView(adView)
//
//        // Determine the screen width (less decorations) to use for the ad width.
//        // If the ad hasn't been laid out, default to the full screen width.
//        val display: Display?
//        val outMetrics = DisplayMetrics()
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
//            display = activity.display
//            display?.getRealMetrics(outMetrics)
//        } else {
//            @Suppress("DEPRECATION")
//            display = activity.windowManager.defaultDisplay
//            @Suppress("DEPRECATION")
//            display.getMetrics(outMetrics)
//        }
//
//        val density = outMetrics.density
//        var adWidthPixels = container.width.toFloat()
//        if (adWidthPixels == 0f) {
//            adWidthPixels = outMetrics.widthPixels.toFloat()
//        }
//        val adWidth = (adWidthPixels / density).toInt()
//        adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth))
//        // Start loading the ad in the background.


    }


    fun loadAdsLargerBanner(activity: Activity, container: ViewGroup) {

        val layoutParams: ViewGroup.LayoutParams = container.layoutParams
        layoutParams.height = activity.resources.getDimension(com.pools.store.R.dimen.dimen250dp).toInt()
        layoutParams.width = (AdSize.SMART_BANNER.getWidthInPixels(activity) + activity.resources.getDimension(com.pools.store.R.dimen.dimen100dp)).toInt()
        //  layoutParams.height = (AdSize.BANNER.getHeightInPixels(activity) + activity.resources.getDimension(com.playgroundx.airo.R.dimen.dimen10dp)).toInt()//this is in pixels

        container.layoutParams = layoutParams

        val adView = AdView(activity)
        adView.setAdSize(AdSize(AdSize.FULL_WIDTH,activity.resources.getDimension(com.pools.store.R.dimen.dimen80dp).toInt()))
        adView.adUnitId = activity.getString(com.pools.store.R.string.str_banner_ads)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        container.addView(adView)

//        // Determine the screen width (less decorations) to use for the ad width.
//        // If the ad hasn't been laid out, default to the full screen width.
//        val display: Display?
//        val outMetrics = DisplayMetrics()
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
//            display = activity.display
//            display?.getRealMetrics(outMetrics)
//        } else {
//            @Suppress("DEPRECATION")
//            display = activity.windowManager.defaultDisplay
//            @Suppress("DEPRECATION")
//            display.getMetrics(outMetrics)
//        }
//
//        val density = outMetrics.density
//        var adWidthPixels = container.width.toFloat()
//        if (adWidthPixels == 0f) {
//            adWidthPixels = outMetrics.widthPixels.toFloat()
//        }
//        val adWidth = (adWidthPixels / density).toInt()
//        adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth))
////        // Start loading the ad in the background.


    }

    fun loadAdsBannerAdaptive(activity: Activity, container: ViewGroup) {
        val adView = AdView(activity)
        container.addView(adView)
        adView.adUnitId = activity.getString(com.pools.store.R.string.str_banner_ads)
        // Determine the screen width (less decorations) to use for the ad width.
        // If the ad hasn't been laid out, default to the full screen width.
        val display: Display?
        val outMetrics = DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display = activity.display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }

        val density = outMetrics.density
        var adWidthPixels = container.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }
        val adWidth = (adWidthPixels / density).toInt()
        val adRequest = AdRequest.Builder().build()
        adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth))
        // Start loading the ad in the background.
        adView.loadAd(adRequest)


    }




    companion object {
        var instance: AdmobHelp? = null
            get() {
                if (field == null) {
                    field = AdmobHelp()
                }
                return field
            }
            private set
    }
}
