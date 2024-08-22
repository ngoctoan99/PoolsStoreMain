package com.pools.store.data.remote.dto

import com.pools.store.domain.model.AppDomain
import com.pools.store.domain.model.ListAppDomain
import com.pools.store.domain.model.PagingDomain
import com.pools.store.domain.model.PublisherIdDomain
import com.squareup.moshi.Json

data class ListAppDto(
    @Json(name = "items")
    val items: List<AppDto>?,
    @Json(name = "paging")
    val paging: PagingDto?,
)

data class AppDto(
    @Json(name = "name")
    val name: String?,
    @Json(name = "packageName")
    val packageName: String?,
    @Json(name = "version")
    val version: String?,
    @Json(name = "fileUrl")
    val fileUrl: String?,
    @Json(name = "iconUrl")
    val iconUrl: String?,
    @Json(name = "types")
    val types: List<String>?,
    @Json(name = "tags")
    val tags: List<String>?,
//    @Json(name = "gameCategories")
//    val gameCategories: List<String>?,
//    @Json(name = "applicationCategories")
//    val applicationCategories: List<String?>?,
    @Json(name = "accessType")
    val accessType: String?,
    @Json(name = "acceptDownload")
    val acceptDownload: Boolean?,
    @Json(name = "size")
    val size: String?,
    @Json(name = "pricing")
    val pricing: Long?,
    @Json(name = "id")
    val id: String?,
    @Json(name="countDownload")
    val countDownload: Int?,
    @Json(name = "averageStar")
    val averageStar: Double?,
    @Json(name = "publisher")
    val publisherId: PublisherIdDto?,
    @Json(name = "imageContentUrls")
    val imageContentUrls: List<String>?,
)

data class PagingDto(
    @Json(name = "pageCurrent")
    val pageCurrent: Long?,
    @Json(name = "pageSize")
    val pageSize: Long?,
    @Json(name = "totalPage")
    val totalPage: Long?,
    @Json(name = "totalSize")
    val totalSize: Long?,
)

data class PublisherIdDto(
    @Json(name = "id")
    val id: String?,
    @Json(name = "name")
    val name: String?,
)
fun PublisherIdDto.toDomain() : PublisherIdDomain{
    return PublisherIdDomain(
        id  = this.id ?:"",
        name  = this.name ?:""
    )
}

fun PagingDto.toDomain(): PagingDomain {
    return PagingDomain(
        pageCurrent = this.pageCurrent ?: 0,
        pageSize = this.pageSize ?: 0,
        totalPage = this.totalPage ?: 0,
        totalSize = this.totalSize ?: 0
    )
}



fun AppDto.toDomain(): AppDomain {
    return AppDomain(
        name = this.name ?: "",
        packageName = this.packageName ?: "",
        version = this.version ?: "",
        fileUrl = this.fileUrl ?: "",
        iconUrl = this.iconUrl ?: "",
        types = this.types ?: emptyList(),
        tags = this.tags ?: emptyList(),
//        gameCategories = this.gameCategories ?: emptyList(),
//        applicationCategories = this.applicationCategories ?: emptyList(),
        accessType = this.accessType ?: "",
        acceptDownload = this.acceptDownload ?: false,
        size = this.size ?: "",
        pricing = this.pricing ?: 0,
        id = this.id ?: "",
        countDownload = this.countDownload ?: 0,
        averageStar = this.averageStar?:0.0,
        publisherId = this.publisherId?.toDomain()?:PublisherIdDto(id = "",name ="").toDomain(),
        imageContentUrls = this.imageContentUrls?: emptyList()
    )
}

fun ListAppDto.toDomain(): ListAppDomain {
    return ListAppDomain(
        items = this.items?.map { it.toDomain() }?: emptyList(),
        paging = this.paging?.toDomain()?:PagingDto(pageCurrent = 0 , pageSize = 0,totalPage = 0,totalSize = 0).toDomain()
    )
}


