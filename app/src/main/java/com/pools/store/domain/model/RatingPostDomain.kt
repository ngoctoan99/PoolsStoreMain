package com.pools.store.domain.model

data class RatingPostDomain(
    val star: Long,
    val description: String,
    val userId: String,
    val fullName: String,
    val avatarUrl: String,
    val apkId: String,
    val createdAt: String,
    val updatedAt: String,
    val id: String,
)