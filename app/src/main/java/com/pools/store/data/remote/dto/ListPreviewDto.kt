package com.pools.store.data.remote.dto

import com.pools.store.domain.model.ListPreviewDomain

import com.pools.store.domain.model.PagingPreviewDomain
import com.pools.store.domain.model.PreviewDomain
import com.squareup.moshi.Json

data class ListPreviewDto(
    @Json(name = "items")
    val items: List<PreviewDto>?,
    @Json(name = "paging")
    val paging: PagingPreviewDto?,
)

data class PreviewDto(
    @Json(name = "star")
    val star: Long?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "userId")
    val userId: String?,
    @Json(name = "fullName")
    val fullName: String?,
    @Json(name = "avatarUrl")
    val avatarUrl: String?,
    @Json(name = "apkId")
    val apkId: String?,
    @Json(name = "createdAt")
    val createdAt : String?,
    @Json(name = "id")
    val id: String?,
)

data class PagingPreviewDto(
    @Json(name = "pageCurrent")
    val pageCurrent: Long?,
    @Json(name = "pageSize")
    val pageSize: Long?,
    @Json(name = "totalPage")
    val totalPage: Long?,
    @Json(name = "totalSize")
    val totalSize: Long?,
)


fun PagingPreviewDto.toDomain(): PagingPreviewDomain {
    return PagingPreviewDomain(
        pageCurrent = this.pageCurrent ?: 0,
        pageSize = this.pageSize ?: 0,
        totalPage = this.totalPage ?: 0,
        totalSize = this.totalSize ?: 0
    )
}

fun PreviewDto.toDomain(): PreviewDomain{
    return PreviewDomain(
        star = this.star?:0,
        description = this.description?:"",
        userId = this.userId?:"",
        fullName = this.fullName?:"",
        avatarUrl = this.avatarUrl?:"",
        apkId = this.apkId?:"",
        id = this.id?:"",
        createdAt = this.createdAt?:""

    )
}

fun ListPreviewDto.toDomain() : ListPreviewDomain{
    return ListPreviewDomain(
        items = this.items?.map { it.toDomain() }?: emptyList(),
        paging = this.paging?.toDomain()?:PagingPreviewDto(pageCurrent = 0 , pageSize = 0,totalPage = 0,totalSize = 0).toDomain()
    )
}

