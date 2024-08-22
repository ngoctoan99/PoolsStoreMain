package com.pools.store.domain.model

data class ListNotiHistoryDomain(
    val items: List<NotiHistoryDomain>,
    val paging: PagingDomain,
)

data class NotiHistoryDomain(
    val name: String,
    val description: String,
    val type: String,
    val point: Double,
    val createdAt : String
)

data class NameNotiDomain(
    val en: String,
)

data class DescriptionNotiDomain(
    val en: String,
)

