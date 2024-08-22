package com.pools.store.data.remote.dto

import com.pools.store.domain.model.DownloadHistoryDomain
import com.pools.store.domain.model.ItemsDownloadHistoryDomain
import com.squareup.moshi.Json

data class DownloadHistoryDto (
    @Json(name = "items")
    val items: List<ItemsDownloadHistoryDto>?,
    @Json(name = "paging")
    val paging: PagingDto?,
)

data class ItemsDownloadHistoryDto(
    @Json(name = "apk")
    val apk: AppDto?
)

fun DownloadHistoryDto.toDomain() : DownloadHistoryDomain{
    return DownloadHistoryDomain(
        items = this.items?.map { it.toDomain() }?: emptyList(),
        paging = this.paging?.toDomain()?:PagingDto(pageCurrent = 0,pageSize = 0, totalSize = 0, totalPage = 0).toDomain()
    )
}

fun ItemsDownloadHistoryDto.toDomain() : ItemsDownloadHistoryDomain{
    return ItemsDownloadHistoryDomain(
        apk = this.apk?.toDomain()?:AppDto( name =  "",
            packageName =  "",
            version = "",
            fileUrl = "",
            iconUrl =  "",
            types =  emptyList(),
            tags =  emptyList(),
            accessType =  "",
            acceptDownload =  false,
            size = "",
            pricing =  0,
            id = "",
            countDownload =  0,
            averageStar = 0.0,
            publisherId = PublisherIdDto(id = "",name =""),
            imageContentUrls = emptyList()
        ).toDomain()
    )
}