package com.pools.store.data.remote

import com.pools.store.base.BaseResponse
import com.pools.store.data.remote.dto.BannerDto
import com.pools.store.data.remote.dto.DetailAppDto
import com.pools.store.data.remote.dto.DetailPublisherDto
import com.pools.store.data.remote.dto.DownloadHistoryDto
import com.pools.store.data.remote.dto.ListAppDto
import com.pools.store.data.remote.dto.ListCategoryDto

import com.pools.store.data.remote.dto.ListNotiHistoryDto
import com.pools.store.data.remote.dto.ListFavoriteDto
import com.pools.store.data.remote.dto.ListPreviewDto
import com.pools.store.data.remote.dto.MyRatingDto
import com.pools.store.data.remote.dto.PostDownloadDto
import com.pools.store.data.remote.dto.PostUserDto
import com.pools.store.data.remote.dto.ProfileDto
import com.pools.store.data.remote.dto.RatingPostDto
import com.pools.store.data.request.RequestPostDownloadData
import com.pools.store.data.request.RequestPostUser
import com.pools.store.data.request.RequestPostUserData
import com.pools.store.data.request.RequestPutListFavorite
import com.pools.store.data.request.RequestPutListFavoriteData
import com.pools.store.data.request.RequestRatingPostData
import com.pools.store.domain.model.ListFavoriteDomain
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApi {
    @GET("/api/v1/user/user-info")
    suspend fun getProfile(
        @Header("Authorization") authorization: String,
    ): BaseResponse<ProfileDto>
    @GET("/api/v1/apk")
    suspend fun getListApp(
        @Header("Authorization") authorization: String,
        @Query("pageCurrent") pageCurrent: Int,
        @Query("pageSize") pageSize: Int,
        @Query("noLimit") noLimit: Boolean,
        @Query("createdAt") createdAt: Int,
        @Query("q") q: String,
        @Query("tags") tags: String,
        @Query("types") types: String?,
    ): BaseResponse<ListAppDto>
    @GET("/api/v1/apk")
    suspend fun getListByCategoryId(
        @Header("Authorization") authorization: String,
        @Query("pageCurrent") pageCurrent: Int,
        @Query("pageSize") pageSize: Int,
        @Query("noLimit") noLimit: Boolean,
        @Query("createdAt") createdAt: Int,
        @Query("q") q: String,
        @Query("categoryIds") categoryIds: String,
    ): BaseResponse<ListAppDto>
    @GET("/api/v1/apk")
    suspend fun getListAppSearch(
        @Header("Authorization") authorization: String,
        @Query("pageCurrent") pageCurrent: Int,
        @Query("pageSize") pageSize: Int,
        @Query("noLimit") noLimit: Boolean,
        @Query("createdAt") createdAt: Int,
        @Query("q") q: String,
    ): BaseResponse<ListAppDto>
    @GET("/api/v1/rating-apk")
    suspend fun getListPreview(
        @Header("Authorization") authorization: String,
        @Query("pageCurrent") pageCurrent: Int,
        @Query("pageSize") pageSize: Int,
        @Query("noLimit") noLimit: Boolean,
        @Query("createdAt") createdAt: Int,
        @Query("apkId") apkId: String,
    ): BaseResponse<ListPreviewDto>

    @POST("/api/v1/rating-apk")
    suspend fun postRating(
        @Header("Authorization") authorization: String,
        @Body request : RequestRatingPostData
    ): BaseResponse<RatingPostDto>


    @GET("/api/v1/apk/details/{id}")
    suspend fun getDetailApp(
        @Header("Authorization") authorization: String,
        @Path("id") id : String
    ): BaseResponse<DetailAppDto>

    @GET("/api/v1/rating-apk/rating")
    suspend fun getMyRating(
        @Header("Authorization") authorization: String,
        @Query("apkId") apkId : String
    ): BaseResponse<MyRatingDto>


    @GET("/api/v1/category")
    suspend fun getListCategory(
        @Header("Authorization") authorization: String,
        @Query("pageCurrent") pageCurrent: Int,
        @Query("pageSize") pageSize: Int,
        @Query("noLimit") noLimit: Boolean,
        @Query("createdAt") createdAt: Int,
        @Query("apkType") apkType: String,
    ): BaseResponse<ListCategoryDto>


    @GET("/api/v1/banner")
    suspend fun getBanner(
        @Header("Authorization") authorization: String,
        @Query("pageCurrent") pageCurrent: Int,
        @Query("pageSize") pageSize: Int,
        @Query("noLimit") noLimit: Boolean,
        @Query("createdAt") createdAt: Int,
    ): BaseResponse<BannerDto>


    @GET("/api/v1/publisher/{id}")
    suspend fun getDetailPublisher(
        @Header("Authorization") authorization: String,
        @Path("id") id : String
    ): BaseResponse<DetailPublisherDto>


    @POST("/api/v1/download-history")
    suspend fun postDownload(
        @Header("Authorization") authorization: String,
        @Body request : RequestPostDownloadData
    ): BaseResponse<PostDownloadDto>

    /**
     * https://pools-app-store-dev.poolsmobility.com/api/v1/user
     **/
    @GET("/api/v1/user")
    suspend fun getListFavorite(
        @Header("Authorization") authorization: String,
    ): BaseResponse<ListFavoriteDto>
    @GET("/api/v1/download-history/download-list")
    suspend fun getDownloadHistory(
        @Header("Authorization") authorization: String,
        @Query("pageCurrent") pageCurrent: Int,
        @Query("pageSize") pageSize: Int,
        @Query("noLimit") noLimit: Boolean,
        @Query("createdAt") createdAt: Int,
    ): BaseResponse<DownloadHistoryDto>


    @POST("/api/v1/user")
    suspend fun postUser(
        @Header("Authorization") authorization: String,
        @Body request : RequestPostUserData
    ): BaseResponse<PostUserDto>


    @GET("/api/v1/notification")
    suspend fun getListNoti(
        @Header("Authorization") authorization: String,
        @Query("pageCurrent") pageCurrent: Int,
        @Query("pageSize") pageSize: Int,
        @Query("noLimit") noLimit: Boolean,
        @Query("createdAt") createdAt: Int,
    ): BaseResponse<ListNotiHistoryDto>

    @PUT("/api/v1/user/add-favorite")
    suspend fun putAddFavorite(
        @Header("Authorization") authorization: String,
        @Body request : RequestPutListFavoriteData
    ): BaseResponse<ListFavoriteDto>

    @PUT("/api/v1/user/remove-favorite")
    suspend fun putRemoveFavorite(
        @Header("Authorization") authorization: String,
        @Body request : RequestPutListFavoriteData
    ): BaseResponse<ListFavoriteDto>
}