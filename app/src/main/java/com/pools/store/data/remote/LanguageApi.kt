package com.pools.store.data.remote

import com.pools.store.base.BaseResponse
import com.pools.store.data.remote.dto.LanguageDto

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LanguageApi {
    @GET("/api/languages")
    suspend fun getLanguage(
        @Header("Authorization") authorization: String,
        @Query("language") language: String
    ): BaseResponse<LanguageDto>
}