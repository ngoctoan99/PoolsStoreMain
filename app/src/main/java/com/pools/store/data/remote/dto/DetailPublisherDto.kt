package com.pools.store.data.remote.dto


import com.pools.store.domain.model.DetailPublisherDomain
import com.squareup.moshi.Json

data class DetailPublisherDto(
    @Json(name = "name")
    val name: String?,
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "apks")
    val apks: List<AppDto>?,
    @Json(name = "avatarUrl")
    val avatarUrl: String?,
    @Json(name = "backgroundUrl")
    val backgroundUrl: String?,
    @Json(name = "id")
    val id: String?,
)

fun DetailPublisherDto.toDomain() : DetailPublisherDomain{
    return DetailPublisherDomain(
        name = this.name?:"",
        bio = this.bio?:"",
        apks = this.apks?.map { it.toDomain() }?: emptyList(),
        avatarUrl = this.avatarUrl?:"",
        backgroundUrl = this.backgroundUrl?:"",
        id = this.id?:""
    )
}