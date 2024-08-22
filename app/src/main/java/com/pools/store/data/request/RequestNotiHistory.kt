package com.pools.store.data.request

data class RequestNotiHistory(
    val bearer_token : String,
    val data : RequestNotiHistoryData
)

data class RequestNotiHistoryData(
    val pageCurrent : Int,
    val pageSize : Int,
    val noLimit : Boolean,
    val createdAt : Int,
)