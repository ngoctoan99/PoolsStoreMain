package com.pools.store.data.request

data class RequestGetBanner(
    val bearer_token : String,
    val data : RequestGetBannerData
)

data class RequestGetBannerData(
    val pageCurrent : Int,
    val pageSize : Int,
    val noLimit : Boolean,
    val createdAt : Int,
)