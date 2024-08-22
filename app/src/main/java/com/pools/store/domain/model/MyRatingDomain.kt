package com.pools.store.domain.model


data class MyRatingDomain(
    val star: Long,
    val description: String,
    val fullName: String,
    val avatarUrl: String,
    val createdAt: String,
)