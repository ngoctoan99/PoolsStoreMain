package com.pools.store.data.request

data class RequestPostDownload(
    val bearer_token : String,
    val data : RequestPostDownloadData
)

data class RequestPostDownloadData(
    val apkId : String,
)