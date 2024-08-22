package com.pools.store.data.request

data class RequestRatingPost(
    val bearer_token : String,
    val data : RequestRatingPostData
)

data class RequestRatingPostData(
    val star : Int,
    val description : String,
    val apkId : String
)