package com.pools.store.fcm




data class DataDefaultNotification(
    val isDeleted: Boolean,
    val name: String,
    val description: String,
    val type: String,
    val point: Long,
    val userId: String,
    val isSeen: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val __v: Long,
    val id: String,
)

data class Name(
    val en: String,
)

data class Description(
    val en: String,
)
