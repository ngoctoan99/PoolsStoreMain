package com.pools.store.domain.model

data class BannerDomain(
    val items: List<ItemBannerDomain>,
    val paging: PagingDomain,
)

data class ItemBannerDomain(
    val name: String,
    val imageUrls: List<String>,
)