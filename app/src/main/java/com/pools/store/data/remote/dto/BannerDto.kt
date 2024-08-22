package com.pools.store.data.remote.dto

import com.pools.store.domain.model.BannerDomain
import com.pools.store.domain.model.ItemBannerDomain
import com.squareup.moshi.Json

data class BannerDto(
    @Json(name = "items" )
    val items: List<ItemBannerDto>?,
    @Json(name = "paging" )
    val paging: PagingDto?,
)

data class ItemBannerDto(
    @Json(name = "name")
    val name: String?,
    @Json(name = "imageUrls" )
    val imageUrls: List<String>?,
)

fun ItemBannerDto.toDomain() : ItemBannerDomain{
    return ItemBannerDomain(
        name = this.name?:"",
        imageUrls = this.imageUrls?: emptyList()
    )
}
fun BannerDto.toDomain() : BannerDomain{
    return BannerDomain(
        items = this.items?.map { it.toDomain() } ?: emptyList(),
        paging = this.paging?.toDomain()?:PagingDto(
            pageCurrent = 0,
            pageSize =  0,
            totalPage = 0,
            totalSize = 0
        ).toDomain()
    )
}
