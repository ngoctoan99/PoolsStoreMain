package com.pools.store.data.request

data class RequestGetListCategory(
    val bearer_token : String,
    val data : RequestGetListCategoryData
)

data class RequestGetListCategoryData(
    val pageCurrent : Int,
    val pageSize : Int,
    val noLimit : Boolean,
    val createdAt : Int,
    val apkType:String
)