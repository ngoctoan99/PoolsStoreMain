package com.pools.store.data.request

data class RequestGetListByCategoryId(
    val bearer_token : String,
    val data : RequestGetListByCategoryIdData
)

data class RequestGetListByCategoryIdData(
    val pageCurrent : Int,
    val pageSize : Int,
    val noLimit : Boolean,
    val createdAt : Int,
    var q : String = "",
    var categoryIds: String
)