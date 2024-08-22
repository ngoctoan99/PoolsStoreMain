package com.pools.store.data.request

data class RequestDetailPublisher(
    val bearer_token : String,
    val data : RequestDetailPublisherData
)

data class RequestDetailPublisherData(
    val id : String,
)