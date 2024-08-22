package com.pools.store.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class ListAppDomain(
    val items: List<AppDomain>,
    val paging: PagingDomain,
)
@Parcelize
data class AppDomain(
    val name: String,
    val packageName: String,
    val version: String,
    val fileUrl: String,
    val iconUrl: String,
    val types: List<String>,
    val tags: List<String>,
//    val gameCategories: List<String>,
//    val applicationCategories: List<String?>,
    val accessType: String,
    val acceptDownload: Boolean,
    val size: String,
    val pricing: Long,
    val id: String,
    val imageContentUrls: List<String>,
    val countDownload: Int,
    val averageStar: Double,
    val publisherId : PublisherIdDomain
) : Parcelable

data class PagingDomain(
    val pageCurrent: Long,
    val pageSize: Long,
    val totalPage: Long,
    val totalSize: Long,
)
@Parcelize
data class PublisherIdDomain(
    val id: String,
    val name: String,
) : Parcelable