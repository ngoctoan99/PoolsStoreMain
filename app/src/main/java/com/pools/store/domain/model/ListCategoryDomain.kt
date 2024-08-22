package com.pools.store.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class ListCategoryDomain(
    val items: List<CategoryDomain>,
    val paging: PagingCategoryDomain,
)
@Parcelize
data class CategoryDomain(
    val name: String,
    val iconUrl: String,
    val apkType: String,
    val createdAt: String,
    val updatedAt: String,
    val id: String,
) : Parcelable

data class PagingCategoryDomain(
    val pageCurrent: Long,
    val pageSize: Long,
    val totalPage: Long,
    val totalSize: Long,
)
@Parcelize
data class NameCategoryDomain(
    val en: String,
):Parcelable