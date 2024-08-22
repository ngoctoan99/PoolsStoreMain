package com.pools.store.data.remote.dto

import com.pools.store.domain.model.CategoryIdDomain
import com.pools.store.domain.model.DetailAppDomain
import com.pools.store.domain.model.DetailDomain
import com.pools.store.domain.model.NameDomain
import com.pools.store.domain.model.PublisherIdDomain
import com.pools.store.domain.model.RatingStarDomain
import com.pools.store.domain.model.RelatedApkDomain
import com.squareup.moshi.Json


data class DetailAppDto(
    @Json(name = "detail")
    val detail: DetailDto?,
    @Json(name = "ads")
    val ads: List<RelatedApkDto>?,
    @Json(name = "isFavorite")
    val isFavorite: Boolean,
    @Json(name = "relatedApks")
    val relatedApks: List<RelatedApkDto>?,
    @Json(name = "limitClaimInstall")
    val limitClaimInstall: Long?,
    @Json(name = "limitClaimRating")
    val limitClaimRating: Long?,

)

data class DetailDto(
    @Json(name = "ratingStar")
    val ratingStar: RatingStarDto?,
    @Json(name = "countRating")
    val countRating: Long?,
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
    @Json(name = "size")
    val size: String?,
    @Json(name = "pricing")
    val pricing: Long?,
    @Json(name = "organizationName")
    val organizationName: String?,
    @Json(name = "thumbnailUrl")
    val thumbnailUrl: String?,
    @Json(name = "imageContentUrls")
    val imageContentUrls: List<String>?,
    @Json(name = "about")
    val about: String?,
    @Json(name = "countDownload")
    val countDownload: Long?,
    @Json(name = "averageStar")
    val averageStar: Double?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "accessType")
    val accessType: String?,
    @Json(name = "acceptDownload")
    val acceptDownload: Boolean?,
    @Json(name = "publisher")
    val publisherId : PublisherIdDto?
)

data class CategoryIdDto(
    @Json(name = "name")
    val name: NameDto?,
    @Json(name = "iconUrl")
    val iconUrl: String?,
    @Json(name = "apkType")
    val apkType: String?,
    @Json(name = "id")
    val id: String?,
)

data class NameDto(
    @Json(name = "en")
    val en: String?,
    @Json(name = "ko")
    val ko: String?,
    @Json(name = "_id")
    val _id: String?,
)

data class RelatedApkDto(
    @Json(name = "countRating")
    val countRating: Long?,
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
    @Json(name = "categoryIds")
    val categoryIds: List<String>?,
    @Json(name = "accessType")
    val accessType: String?,
    @Json(name = "acceptDownload")
    val acceptDownload: Boolean?,
    @Json(name = "size")
    val size: String?,
    @Json(name = "pricing")
    val pricing: Long?,
    @Json(name = "organizationName")
    val organizationName: String?,
    @Json(name = "thumbnailUrl")
    val thumbnailUrl: String?,
    @Json(name = "imageContentUrls")
    val imageContentUrls: List<String>?,
    @Json(name = "about")
    val about: String?,
    @Json(name = "countDownload")
    val countDownload: Long?,
    @Json(name = "averageStar")
    val averageStar: Double?,
    @Json(name = "id")
    val id: String?,
)

data class RatingStarDto(
    @Json(name = "star1")
    val star1: Long?,
    @Json(name = "star2")
    val star2: Long?,
    @Json(name = "star3")
    val star3: Long?,
    @Json(name = "star4")
    val star4: Long?,
    @Json(name = "star5")
    val star5: Long?,
)

fun NameDto.toDomain() : NameDomain{
    return NameDomain(
        en = this.en?:"", ko = this.ko?:"", _id = this._id?:""
    )
}

fun CategoryIdDto.toDomain()  : CategoryIdDomain{
    return CategoryIdDomain(
        name = this.name?.toDomain()?:NameDomain(en = "", ko = "", _id = ""),
        iconUrl = this.iconUrl?:"",
        apkType = this.apkType?:"",
        id = this.id?:""
    )
}

fun RelatedApkDto.toDomain() : RelatedApkDomain {
    return RelatedApkDomain(
        countRating = this.countRating?:0,
        name =this.name?:"",
        packageName = this.packageName?:"",
        version = this.version?:"",
        fileUrl = this.fileUrl?:"",
        iconUrl =  this.iconUrl?:"",
        types =  this.types?: emptyList(),
        tags = this.tags?: emptyList(),
        categoryIds = this.categoryIds?: emptyList(),
        accessType = this.accessType?:"",
        acceptDownload = this.acceptDownload?:false,
        size = this.size?:"",
        pricing = this.pricing?:0,
        organizationName = this.organizationName?:"",
        thumbnailUrl = this.thumbnailUrl?:"",
        imageContentUrls =  this.imageContentUrls?: emptyList(),
        about = this.about?:"",
        countDownload = this.countDownload?:0,
        averageStar = this.averageStar?:0.0,
        id = this.id?:""

    )
}
fun RatingStarDto.toDomain() : RatingStarDomain{
    return RatingStarDomain(
        star1 = this.star1?:0,
        star2 = this.star2?:0,
        star3 = this.star3?:0,
        star4 = this.star4?:0,
        star5 = this.star5?:0,
    )
}

fun DetailDto.toDomain() : DetailDomain {
    return DetailDomain(
        countRating = this.countRating?:0,
        name =this.name?:"",
        packageName = this.packageName?:"",
        version = this.version?:"",
        fileUrl = this.fileUrl?:"",
        iconUrl =  this.iconUrl?:"",
        types =  this.types?: emptyList(),
        tags = this.tags?: emptyList(),
        accessType = this.accessType?:"",
        acceptDownload = this.acceptDownload?:false,
        size = this.size?:"",
        pricing = this.pricing?:0,
        organizationName = this.organizationName?:"",
        thumbnailUrl = this.thumbnailUrl?:"",
        imageContentUrls =  this.imageContentUrls?: emptyList(),
        about = this.about?:"",
        countDownload = this.countDownload?:0,
        averageStar = this.averageStar?:0.0,
        id = this.id?:"",
        ratingStar = this.ratingStar?.toDomain()?:RatingStarDto(star1 = 0,star2 = 0, star3 = 0, star4 = 0, star5 = 0).toDomain(),
        publisherId = this.publisherId?.toDomain()?:PublisherIdDto(id = "",name ="").toDomain()

    )
}

fun DetailAppDto.toDomain() : DetailAppDomain{
    return DetailAppDomain(
        detail = this.detail?.toDomain()?:DetailDto(countRating = 0,
            name ="",
            packageName ="",
            version ="",
            fileUrl = "",
            iconUrl =  "",
            types = emptyList(),
            tags = emptyList(),
            accessType = "",
            acceptDownload = false,
            size = "",
            pricing = 0,
            organizationName = "",
            thumbnailUrl = "",
            imageContentUrls = emptyList(),
            about = "",
            countDownload = 0,
            averageStar = 0.0,
            id = "",
            ratingStar = RatingStarDto(star1 = 0,star2 = 0, star3 = 0, star4 = 0, star5 = 0),
            publisherId = PublisherIdDto(id = "",name ="")).toDomain(),
        ads = this.ads?.map { it.toDomain() }?: emptyList(),
        relatedApks = this.relatedApks?.map { it.toDomain() }?: emptyList(),
        isFavorite = this.isFavorite?:false,
        limitClaimRating = this.limitClaimRating?:0,
        limitClaimInstall = this.limitClaimInstall?:0
    )
}