package com.pools.store.data.request

data class RequestDetailApp(
    val bearer_token : String,
    val data : RequestDetailAppData
)

data class RequestDetailAppData(
   val id : String
)