package com.pools.store.libs

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.pools.store.BuildConfig
import com.pools.store.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class DownloadController(private val context: Context, private val url: String) {
    companion object {
        private const val FILE_NAME = "SampleDownloadApp.apk"
        private const val FILE_BASE_PATH = "file://"
        private const val MIME_TYPE = "application/vnd.android.package-archive"
        private const val PROVIDER_PATH = ".provider"
        private const val APP_INSTALL_PATH = "\"application/vnd.android.package-archive\""
        private var downloadId: Long = 0
        private lateinit var downloadApkReceiver: DownloadApkReceiver
    }
    fun enqueueDownload() {
        var destination =
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
        destination += FILE_NAME
        val uri = Uri.parse("$FILE_BASE_PATH$destination")
        val file = File(destination)
        if (file.exists()) file.delete()
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri)
        request.setMimeType(MIME_TYPE)
        request.setTitle(context.getString(R.string.title_file_download))
        request.setDescription(context.getString(R.string.downloading))
        // set destination
        request.setDestinationUri(uri)
        request.setAllowedOverMetered(true)
        request.setAllowedOverRoaming(true)
        showInstallOption(destination, uri)
        // Enqueue a new download and same the referenceId
        downloadManager.enqueue(request)
        Toast.makeText(context, context.getString(R.string.downloading), Toast.LENGTH_LONG)
            .show()
    }


    private fun showInstallOption(
        destination: String,
        uri: Uri
    ) {
        Log.d("TTT","showInstallOption")
        // set BroadcastReceiver to install app when .apk is downloaded
        downloadApkReceiver = DownloadApkReceiver()

//        val onComplete = object : BroadcastReceiver() {
//            override fun onReceive(
//                context: Context,
//                intent: Intent
//            ) {
//                Log.d("TTT","onComplete")
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    val contentUri = FileProvider.getUriForFile(
//                        context,
//                        BuildConfig.APPLICATION_ID + PROVIDER_PATH,
//                        File(destination)
//                    )
//                    val install = Intent(Intent.ACTION_VIEW)
//                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                    install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
//                    install.data = contentUri
//                    context.startActivity(install)
//                    context.unregisterReceiver(this)
//                    // finish()
//                } else {
//                    Log.d("TTT","install")
//                    val install = Intent(Intent.ACTION_VIEW)
//                    install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    install.setDataAndType(
//                        uri,
//                        APP_INSTALL_PATH
//                    )
//                    context.startActivity(install)
//                    context.unregisterReceiver(this)
//                    // finish()
//                }
//            }
//        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            context.registerReceiver(downloadApkReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                Context.RECEIVER_EXPORTED)
        }else {
            context.registerReceiver(downloadApkReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }

    }

    @SuppressLint("Range")
    private suspend fun trackDownloadProgress() {
        var downloading = true
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        while (downloading) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor: Cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                val bytesDownloaded =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val bytesTotal =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                if (bytesTotal > 0) {
                    val progress = (bytesDownloaded.toDouble() * 100L / bytesTotal.toDouble()).toInt()
//                    progressBar.progress = progress
                    Log.d("TTT","trackDownloadProgress : ${progress}")
                }

                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                downloading = status == DownloadManager.STATUS_RUNNING

                if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                    downloading = false
                }
            }
            cursor.close()

            // Delay for 1 second before checking again
//            delay(1000)
        }
    }
}