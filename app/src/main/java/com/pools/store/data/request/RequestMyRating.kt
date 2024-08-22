package com.pools.store.data.request

data class RequestMyRating(
    val bearer_token : String,
    val data : RequestMyRatingData
)

data class RequestMyRatingData(
  val apkId : String
)