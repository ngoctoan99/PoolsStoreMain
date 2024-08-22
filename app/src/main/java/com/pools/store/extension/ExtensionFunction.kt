package com.pools.store.extension

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextPaint
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.pools.store.R
import com.pools.store.domain.model.LanguageDataDomain
import com.pools.store.domain.model.LanguageDomain
import com.pools.store.libs.DownloadController
import java.text.DecimalFormat
import java.util.Locale
import kotlin.math.abs

private val PERMISSION_REQUEST_STORAGE = 0
private var apkSlideUrl =
    "https://poolsphone.s3.ap-southeast-1.amazonaws.com/images/pools-popup-services/AppServices/PoolSlide_v1_0_1.apk"
private lateinit var downloadController: DownloadController

val gson :Gson = Gson()
fun AppCompatActivity.checkSelfPermissionCompat(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)

fun AppCompatActivity.shouldShowRequestPermissionRationaleCompat(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun AppCompatActivity.requestPermissionsCompat(
    permissionsArray: Array<String>,
    requestCode: Int
) {
    ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
}

fun View.showSnackbar(msgId: Int, length: Int) {
    showSnackbar(context.getString(msgId), length)
}

fun View.showSnackbar(msg: String, length: Int) {
    showSnackbar(msg, length, null, {})
}

fun View.showSnackbar(
    msgId: Int,
    length: Int,
    actionMessageId: Int,
    action: (View) -> Unit
) {
    showSnackbar(context.getString(msgId), length, context.getString(actionMessageId), action)
}

fun View.showSnackbar(
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    action: (View) -> Unit
) {
    val snackbar = Snackbar.make(this, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    }
}

fun checkVersionApp(packageName: String, context: Context): String {
    var pinfo: PackageInfo? = null
    pinfo = try {
        context.packageManager.getPackageInfo(packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        return "not install"
    }
    return if (pinfo != null) {
        pinfo.versionName
    } else {
        "error"
    }
}

fun checkVersionAppBoolean(packageName: String, context: Context): Boolean {
    var pinfo: PackageInfo? = null
    pinfo = try {
        context.packageManager.getPackageInfo(packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }
    return if (pinfo != null) {
        return true
    } else {
        return false
    }
}

// check app in device
//        if(checkVersionAppBoolean("com.pools.slide",this)){
//            binding.tvStatus.text = "Open"
//            binding.tvStatus.visibility = View.VISIBLE
//            binding.processBar.visibility = View.GONE
//        }else {
//            binding.tvStatus.text = "Download"
//            binding.tvStatus.visibility = View.VISIBLE
//            binding.processBar.visibility = View.GONE
//        }

//        val accessToken   = AppUtils.getTokenLogin(this)["accessToken"]
//        Log.d("TTT","MainActivity ${accessToken}")
//        Log.d("TTT","Version ${checkVersionApp("com.pools.dashboard",this)}")
//        Log.d("TTT","Version ${checkVersionApp("com.pools.slide",this)}")
//        binding.apply {
//            btnDownload.setOnClickListener {
//                if(tvStatus.text == "Download"){
//                    binding.tvStatus.visibility = View.GONE
//                    processBar.visibility = View.VISIBLE
//                    downloadController = DownloadController(binding.root.context, apkSlideUrl)
//                    if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
//                        downloadController.enqueueDownload()
//                    }else {
//                        checkStoragePermission()
//                    }
//                }else {
//                    Toast.makeText(binding.root.context,"Open App",Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

//private fun checkStoragePermission() {
//    // Check if the storage permission has been granted
//    if (checkSelfPermissionCompat(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//        PackageManager.PERMISSION_GRANTED
//    ) {
//        // start downloading
//        downloadController.enqueueDownload()
//    } else {
//        // Permission is missing and must be requested.
//        requestStoragePermission()
//    }
//}
//private fun requestStoragePermission() {
//    if (shouldShowRequestPermissionRationaleCompat(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//        binding.root.showSnackbar(
//            R.string.storage_access_required,
//            Snackbar.LENGTH_INDEFINITE, R.string.ok
//        ) {
//            requestPermissionsCompat(
//                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                PERMISSION_REQUEST_STORAGE
//            )
//        }
//    } else {
//        requestPermissionsCompat(
//            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
//            PERMISSION_REQUEST_STORAGE
//        )
//    }
//}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        //for check internet over Bluetooth
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}

fun textGradientColor(textView: TextView): Shader {
    val paint: TextPaint = textView.paint
    val width = paint.measureText("Tianjin, China")

    val textShader: Shader = LinearGradient(
        0f, 0f, width, textView.textSize, intArrayOf(
            ContextCompat.getColor(textView.context, R.color.stroke_green_start),
            ContextCompat.getColor(textView.context, R.color.stroke_green_start),
            ContextCompat.getColor(textView.context, R.color.stroke_green_end),
            ContextCompat.getColor(textView.context, R.color.stroke_green_end),
        ), null, Shader.TileMode.CLAMP
    )
    return textShader
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        ?: return
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}

fun isHttpOrHttpsUrl(url: String): Boolean {
    return Patterns.WEB_URL.matcher(url)
        .matches() && (url.startsWith("http://") || url.startsWith("https://"))
}


fun formatNumber(number: Double): String {
    var numberString = ""
    numberString = if (abs(number / 1000000000000.0) > 1) {
        DecimalFormat("0.#").format(number / 1000000000000.0) + "T"
    } else if (abs(number / 1000000000) > 1) {
        DecimalFormat("0.#").format(number / 1000000000) + "B"
    } else if (abs(number / 1000000) > 1) {
        DecimalFormat("0.#").format(number / 1000000) + "M"
    } else if (abs(number / 1000) > 1) {
        DecimalFormat("0.#").format(number / 1000) + "K"
    } else {
        DecimalFormat("0.#").format(number)
    }
    return numberString
}

fun getCurrentLanguageCode(): String {
    val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Resources.getSystem().configuration.getLocales()[0]
    } else {
        Resources.getSystem().configuration.locale
    }
    return locale.language
}

fun convertObjectToJson(data: LanguageDomain): String {
    val json = gson.toJson(data)
    return json
}

fun convertJsonToObject(json: String?): LanguageDomain {
    return if (json!!.isNotEmpty()) {
        gson.fromJson(json, LanguageDomain::class.java)
    } else {
        LanguageDomain(
            data = LanguageDataDomain(
                noData = "No Data",
                login = "Login",
                pleaseLoginInUsingThePoolsDashboardAppToContinue = "Please login in using the Pools Dashboard app to continue",
                close = "Close"
            )
        )
    }
}

fun restartActivity(activity: Activity) {
    Log.d("TTT", "restartActivity")
    val intent = activity.intent
    activity.finish()
    activity.startActivity(intent)
}




