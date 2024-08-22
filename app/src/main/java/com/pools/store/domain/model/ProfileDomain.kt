package com.pools.store.domain.model

data class ProfileDomain(
    val userId: String,
    val fullName: String,
    val userName: String,
    val email: String,
    val avatar: String,
    val poolsId: String,
    val point: Double,
    val passCode: String,
    val birthDay: String,
    val gender: String,
    val totalPools: Long,
)