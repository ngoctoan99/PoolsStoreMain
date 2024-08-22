package com.pools.store.domain.repository

import com.pools.store.data.request.RequestDetailApp
import com.pools.store.data.request.RequestDetailPublisher
import com.pools.store.data.request.RequestGetBanner
import com.pools.store.data.request.RequestGetHistoryDownload
import com.pools.store.data.request.RequestGetListApp
import com.pools.store.data.request.RequestGetListByCategoryId
import com.pools.store.data.request.RequestGetListCategory
import com.pools.store.data.request.RequestGetListFavorite
import com.pools.store.data.request.RequestGetListPreview
import com.pools.store.data.request.RequestGetProfile
import com.pools.store.data.request.RequestMyRating
import com.pools.store.data.request.RequestNotiHistory
import com.pools.store.data.request.RequestPostDownload
import com.pools.store.data.request.RequestPostUser
import com.pools.store.data.request.RequestPutListFavorite
import com.pools.store.data.request.RequestRatingPost
import com.pools.store.domain.model.BannerDomain
import com.pools.store.domain.model.DetailAppDomain
import com.pools.store.domain.model.DetailPublisherDomain
import com.pools.store.domain.model.DownloadHistoryDomain
import com.pools.store.domain.model.ListAppDomain
import com.pools.store.domain.model.ListCategoryDomain
import com.pools.store.domain.model.ListNotiHistoryDomain
import com.pools.store.domain.model.ListFavoriteDomain
import com.pools.store.domain.model.ListPreviewDomain
import com.pools.store.domain.model.MyRatingDomain
import com.pools.store.domain.model.PostDownloadDomain
import com.pools.store.domain.model.PostUserDomain
import com.pools.store.domain.model.ProfileDomain
import com.pools.store.domain.model.RatingPostDomain
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MyRepository {
    suspend fun getProfile(
        request: RequestGetProfile
    ): Flow<Resource<ProfileDomain>>
    suspend fun getListApp(
        request: RequestGetListApp
    ): Flow<Resource<ListAppDomain>>
    suspend fun getListPreview(
        request: RequestGetListPreview
    ): Flow<Resource<ListPreviewDomain>>

    suspend fun postRating(
        request: RequestRatingPost
    ): Flow<Resource<RatingPostDomain>>



    suspend fun getDetailApp(
        request: RequestDetailApp
    ): Flow<Resource<DetailAppDomain>>

    suspend fun getMyRating(
        request: RequestMyRating
    ): Flow<Resource<MyRatingDomain>>


    suspend fun getListCategory(
        request: RequestGetListCategory
    ): Flow<Resource<ListCategoryDomain>>


    suspend fun getListAppSearch(
        request: RequestGetListApp
    ): Flow<Resource<ListAppDomain>>

    suspend fun getBanner(
        request: RequestGetBanner
    ): Flow<Resource<BannerDomain>>

    suspend fun getDetailPublisher(
        request: RequestDetailPublisher
    ): Flow<Resource<DetailPublisherDomain>>


    suspend fun postDownload(
        request: RequestPostDownload
    ): Flow<Resource<PostDownloadDomain>>

    suspend fun getListByCategoryId(
        request: RequestGetListByCategoryId
    ): Flow<Resource<ListAppDomain>>

    suspend fun getListFavorite(
        request: RequestGetListFavorite
    ): Flow<Resource<ListFavoriteDomain>>
    suspend fun getDownloadHistory(
        request: RequestGetHistoryDownload
    ): Flow<Resource<DownloadHistoryDomain>>

    suspend fun postUser(
        request: RequestPostUser
    ): Flow<Resource<PostUserDomain>>

    suspend fun getListNoti(
        request: RequestNotiHistory
    ): Flow<Resource<ListNotiHistoryDomain>>

    suspend fun putAddFavorite(
        request: RequestPutListFavorite
    ): Flow<Resource<ListFavoriteDomain>>

    suspend fun putRemoveFavorite(
        request: RequestPutListFavorite
    ): Flow<Resource<ListFavoriteDomain>>

}