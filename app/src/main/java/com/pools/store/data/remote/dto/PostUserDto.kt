package com.pools.store.data.remote.dto

import com.pools.store.domain.model.PostUserDomain
import com.squareup.moshi.Json

data class PostUserDto(
    @Json(name = "userId")
    val userId: String?,
    @Json(name = "fcmToken")
    val fcmToken: String?,
)

fun PostUserDto.toDomain() : PostUserDomain{
    return PostUserDomain(
        userId = this.userId?:"",
        fcmToken = this.fcmToken?:""
    )
}