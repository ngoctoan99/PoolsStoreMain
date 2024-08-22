package com.pools.store.data.remote.dto

import com.pools.store.domain.model.ProfileDomain
import com.squareup.moshi.Json

data class ProfileDto(
    @Json(name = "userId")
    val userId: String?,
    @Json(name = "fullName")
    val fullName: String?,
    @Json(name = "userName")
    val userName: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "email_verified")
    val emailVerified: Boolean?,
    @Json(name = "avatar")
    val avatar: String?,
    @Json(name = "poolsId")
    val poolsId: String?,
    @Json(name = "point")
    val point: Double?,
    @Json(name = "passCode")
    val passCode: String?,
    @Json(name = "isPoolsPhone")
    val isPoolsPhone: Boolean?,
    @Json(name = "birthDay")
    val birthDay: String?,
    @Json(name = "gender")
    val gender: String?,
    @Json(name = "totalPools")
    val totalPools: Long?,
)

fun ProfileDto.toDomain(): ProfileDomain{
    return ProfileDomain(
      userId = this.userId?:"",
        fullName =this.fullName?:"",
        userName = this.userName?:"",
        email = this.email?:"",
        avatar = this.avatar?:"",
        poolsId = this.poolsId?:"",
        point = this.point?:0.0,
        passCode = this.passCode?:"",
        birthDay = this.birthDay?:"",
        gender = this.gender?:"",
        totalPools = this.totalPools?:0
    )
}