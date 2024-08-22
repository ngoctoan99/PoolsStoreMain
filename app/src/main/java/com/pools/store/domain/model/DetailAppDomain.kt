package com.pools.store.domain.model

data class DetailAppDomain(
    val detail: DetailDomain,
    val ads: List<RelatedApkDomain>,
    val relatedApks: List<RelatedApkDomain>,
    val isFavorite: Boolean,
    val limitClaimInstall: Long,
    val limitClaimRating: Long,
)

data class DetailDomain(
    val ratingStar: RatingStarDomain,
    val countRating: Long,
    val name: String,
    val packageName: String,
    val version: String,
    val fileUrl: String,
    val iconUrl: String,
    val types: List<String>,
    val tags: List<String>,
    val accessType: String,
    val acceptDownload: Boolean,
    val size: String,
    val pricing: Long,
    val organizationName: String,
    val thumbnailUrl: String,
    val imageContentUrls: List<String>,
    val about: String,
    val countDownload: Long,
    val averageStar: Double,
    val id: String,
    val publisherId : PublisherIdDomain
)

data class CategoryIdDomain(
    val name: NameDomain,
    val iconUrl: String,
    val apkType: String,
    val id: String,
)

data class NameDomain(
    val en: String,
    val ko: String,
    val _id: String,
)

data class RelatedApkDomain(
    val countRating: Long,
    val name: String,
    val packageName: String,
    val version: String,
    val fileUrl: String,
    val iconUrl: String,
    val types: List<String>,
    val tags: List<String>,
    val categoryIds: List<String>,
    val accessType: String,
    val acceptDownload: Boolean,
    val size: String,
    val pricing: Long,
    val organizationName: String,
    val thumbnailUrl: String,
    val imageContentUrls: List<String>,
    val about: String,
    val countDownload: Long,
    val averageStar: Double,
    val id: String,
)


data class RatingStarDomain(
    val star1: Long,
    val star2: Long,
    val star3: Long,
    val star4: Long,
    val star5: Long,
)
