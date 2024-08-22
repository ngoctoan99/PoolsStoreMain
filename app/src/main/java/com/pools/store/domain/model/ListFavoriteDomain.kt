// Domain Models
package com.pools.store.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class ListFavoriteDomain(
    val isDeleted: Boolean,
    val userId: String,
    val favoriteIds: List<String>,
    val fcmToken: String,
    val id: String,
    val favorites: List<FavoritesDomain>,
)


@Parcelize
data class FavoritesDomain(
    val fileUrl: String,
    val isDeleted: Boolean,
    val name: String,
    val packageName: String,
    val version: String,
    val iconUrl: String,
    val types: List<String>,
    val categoryIds: List<String>,
    val accessType: String,
    val acceptDownload: Boolean,
    val size: String,
    val pricing: Long,
    val publisherId: String,
    val thumbnailUrl: String,
    val imageContentUrls: List<String>,
    val about: String,
    val countDownload: Long,
    val averageStar: Double,
    val ratingStar: FavoriteRatingStarDomain,
    val tags: List<String>,
    val id: String,
):Parcelable


@Parcelize
data class FavoriteRatingStarDomain(
    val star1: Long,
    val star2: Long,
    val star3: Long,
    val star4: Long,
    val star5: Long,
):Parcelable
