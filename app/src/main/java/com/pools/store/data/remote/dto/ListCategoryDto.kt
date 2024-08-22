package com.pools.store.data.remote.dto

import com.pools.store.domain.model.CategoryDomain
import com.pools.store.domain.model.ListCategoryDomain
import com.pools.store.domain.model.NameCategoryDomain
import com.pools.store.domain.model.NameNotiDomain
import com.pools.store.domain.model.PagingCategoryDomain
import com.squareup.moshi.Json

data class ListCategoryDto(
    @Json(name = "items")
    val items: List<CategoryDto>?,
    @Json(name = "paging")
    val paging: PagingCategoryDto?,
)

data class CategoryDto(
    @Json(name = "name")
    val name: String?,
    @Json(name = "iconUrl")
    val iconUrl: String?,
    @Json(name = "apkType")
    val apkType:String?,
    @Json(name = "createdAt")
    val createdAt: String?,
    @Json(name = "updatedAt")
    val updatedAt: String?,
    @Json(name = "id")
    val id: String?,
)

data class PagingCategoryDto(
    @Json(name = "pageCurrent")
    val pageCurrent: Long?,
    @Json(name = "pageSize")
    val pageSize: Long?,
    @Json(name = "totalPage")
    val totalPage: Long?,
    @Json(name = "totalSize")
    val totalSize: Long?,
)

data class NameCategoryDTO(
    @Json(name = "en")
    val en: String?,
)

fun NameCategoryDTO.toDomain() : NameCategoryDomain {
    return NameCategoryDomain(
        en = this.en?:""
    )
}

fun PagingCategoryDto.toDomain(): PagingCategoryDomain {
    return PagingCategoryDomain(
        pageCurrent = this.pageCurrent ?: 0,
        pageSize = this.pageSize ?: 0,
        totalPage = this.totalPage ?: 0,
        totalSize = this.totalSize ?: 0
    )
}



fun CategoryDto.toDomain(): CategoryDomain {
    return CategoryDomain(
        name = this.name?:"",
        iconUrl = this.iconUrl ?: "",
        apkType = this.apkType ?: "",
        createdAt = this.createdAt ?: "",
        updatedAt = this.updatedAt ?: "",
        id = this.id ?: ""
    )
}

fun ListCategoryDto.toDomain(): ListCategoryDomain {
    return ListCategoryDomain(
        items = this.items?.map { it.toDomain() }?: emptyList(),
        paging = this.paging?.toDomain()?:PagingCategoryDto(pageCurrent = 0 , pageSize = 0,totalPage = 0,totalSize = 0).toDomain()
    )
}