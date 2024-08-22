package com.pools.store.data.request

data class RequestPutListFavorite (
    val bearer_token : String,
    val data : RequestPutListFavoriteData
)
data class RequestPutListFavoriteData(
    val apkId : String,
)