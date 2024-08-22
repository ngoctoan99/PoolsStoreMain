package com.pools.store.data.remote.dto

import com.pools.store.domain.model.MyRatingDomain
import com.squareup.moshi.Json

data class MyRatingDto(
    @Json(name = "star")
    val star: Long?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "fullName")
    val fullName: String?,
    @Json(name = "avatarUrl")
    val avatarUrl: String?,
    @Json(name = "createdAt")
    val createdAt: String?,
)

fun MyRatingDto.toDomain() : MyRatingDomain{
    return MyRatingDomain(
        star = this.star?:0,
        description = this.description?:"",
        fullName =  this.fullName?:"",
        avatarUrl = this.avatarUrl?:"",
        createdAt = this.createdAt?:""
    )
}