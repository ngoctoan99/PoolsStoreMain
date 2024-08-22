package com.pools.store.data.request

data class RequestGetListPreview(
    val bearer_token : String,
    val data : RequestGetListPreviewData
)

data class RequestGetListPreviewData(
    val pageCurrent : Int,
    val pageSize : Int,
    val noLimit : Boolean,
    val createdAt : Int,
    val apkId : String
)