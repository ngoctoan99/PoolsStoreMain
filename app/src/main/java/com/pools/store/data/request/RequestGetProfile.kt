package com.pools.store.data.request

data class RequestGetProfile(
    val bearer_token : String,
    val data : RequestGetProfileData
)

data class RequestGetProfileData(
    val deviceId : String,
    val fcmToken : String,
    val platform : String,
    val platformVersion : String,
    val deviceName : String,
    val ipAddress : String,
)



