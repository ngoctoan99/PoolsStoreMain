package com.pools.store.domain.model

data class DownloadHistoryDomain (
    val items: List<ItemsDownloadHistoryDomain>,
    val paging: PagingDomain,
)

data class ItemsDownloadHistoryDomain(
    val apk: AppDomain
)