package com.pools.store.data.remote.dto

import com.pools.store.domain.model.RatingPostDomain
import com.squareup.moshi.Json

data class RatingPostDto(
    @Json(name = "star")
    val star: Long?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "userId")
    val userId: String?,
    @Json(name = "fullName")
    val fullName: String?,
    @Json(name = "avatarUrl")
    val avatarUrl: String?,
    @Json(name = "apkId")
    val apkId: String?,
    @Json(name = "createdAt")
    val createdAt: String?,
    @Json(name = "updatedAt")
    val updatedAt: String?,
    @Json(name = "id")
    val id: String?,
)

fun RatingPostDto.toDomain(): RatingPostDomain {
    return RatingPostDomain(
        star = this.star?:0, description = this.description?:"", userId = this.userId?:"", fullName = this.fullName?:"", avatarUrl = this.avatarUrl?:"", apkId = this.apkId?:"", createdAt = this.createdAt?:"", updatedAt = this.updatedAt?:"", id = this.id?:""
    )
}