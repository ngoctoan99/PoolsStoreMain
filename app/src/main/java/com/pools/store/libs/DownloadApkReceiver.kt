package com.pools.store.libs

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.pools.store.BuildConfig

import java.io.File

class DownloadApkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val FILE_NAME = "SampleDownloadApp.apk"
        val FILE_BASE_PATH = "file://"
        val MIME_TYPE = "application/vnd.android.package-archive"
        val PROVIDER_PATH = ".provider"
        val APP_INSTALL_PATH = "\"application/vnd.android.package-archive\""
        val action = intent?.action
        if (intent != null) {
            if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                Log.d("TTT", "onComplete")
                var destination =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
                destination += FILE_NAME
                val uri = Uri.parse("$FILE_BASE_PATH$destination")
                val file = File(destination)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val contentUri = context.let {
                        FileProvider.getUriForFile(
                            it,
                            BuildConfig.APPLICATION_ID + PROVIDER_PATH,
                            File(destination)
                        )
                    }
                    val install = Intent(Intent.ACTION_VIEW)
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                    install.data = contentUri
                    context.startActivity(install)
//                    context.unregisterReceiver(this)
//                     finish()
                } else {
                    Log.d("TTT", "install")
                    val install = Intent(Intent.ACTION_VIEW)
                    install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    install.setDataAndType(
                        uri,
                        APP_INSTALL_PATH
                    )
                    context.startActivity(install)
//                    context.unregisterReceiver(this)
                    // finish()
                }
            }
        }


    }
}