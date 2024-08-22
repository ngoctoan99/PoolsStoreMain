package com.pools.store.domain.model

data class ListPreviewDomain(
    val items: List<PreviewDomain>,
    val paging: PagingPreviewDomain,
)

data class PreviewDomain(
    val star: Long,
    val description: String,
    val userId: String,
    val fullName: String,
    val avatarUrl: String,
    val apkId: String,
    val id: String,
    val createdAt : String
)

data class PagingPreviewDomain(
    val pageCurrent: Long,
    val pageSize: Long,
    val totalPage: Long,
    val totalSize: Long,
)