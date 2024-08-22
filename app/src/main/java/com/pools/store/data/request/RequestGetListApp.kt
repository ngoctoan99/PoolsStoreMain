package com.pools.store.data.request

data class RequestGetListApp(
    val bearer_token : String,
    val data : RequestGetListAppData
)

data class RequestGetListAppData(
    val pageCurrent : Int,
    val pageSize : Int,
    val noLimit : Boolean,
    val createdAt : Int,
    var q : String = "",
    var tags : String = "TRENDING",
    var types : String? = null
)