package com.pools.store.domain.model

data class DetailPublisherDomain(
    val name: String,
    val bio: String,
    val apks: List<AppDomain>,
    val avatarUrl: String,
    val backgroundUrl: String,
    val id: String,
)