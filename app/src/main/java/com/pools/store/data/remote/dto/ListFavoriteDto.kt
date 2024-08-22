// DTO Classes
package com.pools.store.data.remote.dto

import com.pools.store.domain.model.FavoriteRatingStarDomain
import com.pools.store.domain.model.FavoritesDomain
import com.pools.store.domain.model.ListFavoriteDomain
import com.squareup.moshi.Json


data class ListFavoriteDto(
    @Json(name = "isDeleted") val isDeleted: Boolean?,
    @Json(name = "userId") val userId: String?,
    @Json(name = "favoriteIds") val favoriteIds: List<String>?,
    @Json(name = "fcmToken") val fcmToken: String?,
    @Json(name = "id") val id: String?,
    @Json(name = "favorites") val favorites: List<FavoritesDto>?,
)

data class FavoritesDto(
    @Json(name = "fileUrl") val fileUrl: String?,
    @Json(name = "isDeleted") val isDeleted: Boolean?,
    @Json(name = "name") val name: String?,
    @Json(name = "packageName") val packageName: String?,
    @Json(name = "version") val version: String?,
    @Json(name = "iconUrl") val iconUrl: String?,
    @Json(name = "types") val types: List<String>?,
    @Json(name = "categoryIds") val categoryIds: List<String>?,
    @Json(name = "accessType") val accessType: String?,
    @Json(name = "acceptDownload") val acceptDownload: Boolean?,
    @Json(name = "size") val size: String?,
    @Json(name = "pricing") val pricing: Long?,
    @Json(name = "publisherId") val publisherId: String?,
    @Json(name = "thumbnailUrl") val thumbnailUrl: String?,
    @Json(name = "imageContentUrls") val imageContentUrls: List<String>?,
    @Json(name = "about") val about: String?,
    @Json(name = "countDownload") val countDownload: Long?,
    @Json(name = "averageStar") val averageStar: Double?,
    @Json(name = "ratingStar") val ratingStar: FavoriteRatingStarDto?,
    @Json(name = "tags") val tags: List<String>?,
    @Json(name = "id") val id: String?,
)

data class FavoriteRatingStarDto(
    @Json(name = "star1") val star1: Long?,
    @Json(name = "star2") val star2: Long?,
    @Json(name = "star3") val star3: Long?,
    @Json(name = "star4") val star4: Long?,
    @Json(name = "star5") val star5: Long?,
)



fun ListFavoriteDto.toDomain(): ListFavoriteDomain {
    return ListFavoriteDomain(
        isDeleted = this.isDeleted ?: false,
        userId = this.userId ?: "",
        favoriteIds = this.favoriteIds ?: emptyList(),
        fcmToken = this.fcmToken ?: "",
        id = this.id ?: "",
        favorites = this.favorites?.map { it.toDomain() } ?: emptyList()
    )
}


fun FavoritesDto.toDomain(): FavoritesDomain {
    return FavoritesDomain(
        fileUrl = this.fileUrl ?: "",
        isDeleted = this.isDeleted ?: false,
        name = this.name ?: "",
        packageName = this.packageName ?: "",
        version = this.version ?: "",
        iconUrl = this.iconUrl ?: "",
        types = this.types ?: emptyList(),
        categoryIds = this.categoryIds ?: emptyList(),
        accessType = this.accessType ?: "",
        acceptDownload = this.acceptDownload ?: false,
        size = this.size ?: "",
        pricing = this.pricing ?: 0,
        publisherId = this.publisherId ?: "",
        thumbnailUrl = this.thumbnailUrl ?: "",
        imageContentUrls = this.imageContentUrls ?: emptyList(),
        about = this.about ?: "",
        countDownload = this.countDownload ?: 123,
        averageStar = this.averageStar ?: 0.0,
        ratingStar = this.ratingStar?.toDomain() ?: FavoriteRatingStarDto(
            star1 = 0,
            star2 = 0,
            star3 = 0,
            star4 = 0,
            star5 = 0
        ).toDomain(),
        tags = this.tags ?: emptyList(),
        id = this.id ?: ""
    )
}


fun FavoriteRatingStarDto.toDomain(): FavoriteRatingStarDomain {
    return FavoriteRatingStarDomain(
        star1 = this.star1 ?: 0,
        star2 = this.star2 ?: 0,
        star3 = this.star3 ?: 0,
        star4 = this.star4 ?: 0,
        star5 = this.star5 ?: 0,
    )
}



//
//fun ListFavoriteDto.toDomain(): ListFavoriteDomain {
//    return ListFavoriteDomain(
//        data = this.data?.toDomain() ?: FavoriteDataDto(
//            isDeleted = false,
//            userId = "",
//            favoriteIds = emptyList(),
//            createdAt = "",
//            updatedAt = "",
//            fcmToken = "",
//            id = "",
//            favorites = emptyList()
//        ).toDomain()
//    )
//}

//fun ListFavoriteDto.toDomain(): ListFavoriteDomain {
//    return ListFavoriteDomain(
//        data = this.data?.toDomain() ?: FavoriteDataDto(
//            isDeleted = false,
//            userId = "",
//            favoriteIds = emptyList(),
//            fcmToken = "",
//            id = "",
//            favorites = listOf(
//                FavoritesDto(
//                    fileUrl = "",
//                    isDeleted = false,
//                    name = "",
//                    packageName = "",
//                    version = "",
//                    iconUrl = "",
//                    types = emptyList(),
//                    categoryIds = emptyList(),
//                    accessType = "",
//                    acceptDownload = false,
//                    size = "",
//                    pricing = 0,
//                    publisherId = "",
//                    thumbnailUrl = "",
//                    imageContentUrls = emptyList(),
//                    about = "",
//                    countDownload = 777,
//                    averageStar = 0,
//                    ratingStar = FavoriteRatingStarDto(
//                        star1 = 0,
//                        star2 = 0,
//                        star3 = 0,
//                        star4 = 0,
//                        star5 = 0
//                    ),
//                    tags = emptyList(),
//                    id = "",
//                )
//            )
//        ).toDomain()
//    )
//}


