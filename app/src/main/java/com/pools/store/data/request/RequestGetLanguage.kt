package com.pools.store.data.request

data class RequestGetLanguage(
    val bearer_token : String,
    val data : RequestGetLanguageData
)

data class RequestGetLanguageData(
    val language : String
)