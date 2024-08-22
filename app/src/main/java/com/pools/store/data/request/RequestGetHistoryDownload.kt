package com.pools.store.data.request

data class RequestGetHistoryDownload(
    val bearer_token : String,
    val data : RequestGetHistoryDownloadData
)

data class RequestGetHistoryDownloadData(
    val pageCurrent : Int,
    val pageSize : Int,
    val noLimit : Boolean,
    val createdAt : Int,
)