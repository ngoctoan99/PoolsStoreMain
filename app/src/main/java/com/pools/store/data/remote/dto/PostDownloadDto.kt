package com.pools.store.data.remote.dto

import com.pools.store.domain.model.PostDownloadDomain
import com.squareup.moshi.Json

data class PostDownloadDto (
    @Json(name ="userId")
    val userId: String?,
    @Json(name ="apkId")
    val apkId: String?
)

fun PostDownloadDto.toDomain() : PostDownloadDomain{
    return PostDownloadDomain(
        userId = this.userId?:"", apkId = this.apkId?:""
    )
}