package com.pools.store.data.request

data class RequestPostUser(
    val bearer_token : String,
    val data : RequestPostUserData
)

data class RequestPostUserData(
    val fcmToken : String,

)