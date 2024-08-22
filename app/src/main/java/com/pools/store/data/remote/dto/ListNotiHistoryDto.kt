package com.pools.store.data.remote.dto

import com.pools.store.domain.model.DescriptionNotiDomain
import com.pools.store.domain.model.ListNotiHistoryDomain
import com.pools.store.domain.model.NameNotiDomain
import com.pools.store.domain.model.NotiHistoryDomain
import com.squareup.moshi.Json

data class ListNotiHistoryDto(
    @Json(name = "items")
    val items: List<NotiHistoryDto>?,
    @Json(name = "paging")
    val paging: PagingDto?,
)

data class NotiHistoryDto(
    @Json(name = "name")
    val name: String?,
    @Json(name = "description")
    val description:String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "point")
    val point: Double?,
    @Json(name = "createdAt")
    val createdAt: String?,
)

data class NameNotiDto(
    @Json(name = "en")
    val en: String?,
)

data class DescriptionNotiDto(
    @Json(name = "en")
    val en: String?,
)

fun DescriptionNotiDto.toDomain() : DescriptionNotiDomain {
    return DescriptionNotiDomain(
        en = this.en?:""
    )
}
fun NameNotiDto.toDomain() : NameNotiDomain {
    return NameNotiDomain(
        en = this.en?:""
    )
}

fun NotiHistoryDto.toDomain() : NotiHistoryDomain{
    return NotiHistoryDomain(
        name = this.name?:"",
        description =  this.description?:"",
        type  =this.type?:"",
        point = this.point?:0.0,
        createdAt = this.createdAt?:""
    )
}

fun ListNotiHistoryDto.toDomain() : ListNotiHistoryDomain{
    return ListNotiHistoryDomain(
        items = this.items?.map { it.toDomain() }?: emptyList(),
        paging = this.paging?.toDomain()?:PagingDto(pageCurrent = 0,pageSize = 0, totalSize = 0, totalPage = 0).toDomain()
    )
}
