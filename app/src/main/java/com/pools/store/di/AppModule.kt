package com.pools.store.di

import android.content.Context
import com.pools.store.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.data.remote.LanguageApi
import com.pools.store.data.remote.MyApi
import com.pools.store.data.repository.LanguageImpl
import com.pools.store.data.repository.MyImpl
import com.pools.store.domain.repository.LanguageRepository
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.CoroutineContextProvider
import com.pools.store.utils.CoroutineContextProviderImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferenceHelper(@ApplicationContext context: Context): CachePreferencesHelper {
        return CachePreferencesHelper(context)
    }

    @Provides
    @Singleton
    fun provideCoroutineContextProvider(contextProvider: CoroutineContextProviderImp): CoroutineContextProvider =
        contextProvider


    @Provides
    @Singleton
    fun provideMyApi(): MyApi {
        return Retrofit.Builder()
            .baseUrl("https://pools-app-store-dev.poolsmobility.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(createOkHttpClient(createLoggingInterceptor()))
            .build()
            .create(MyApi::class.java)
    }


    @Provides
    @Singleton
    fun provideLanguageApi(): LanguageApi {
        return Retrofit.Builder()
            .baseUrl("https://popup.poolsmobility.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(createOkHttpClient(createLoggingInterceptor()))
            .build()
            .create(LanguageApi::class.java)
    }

    private fun createOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val OK_HTTP_TIMEOUT = 60L
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }


    @Provides
    @Singleton
    fun provideAiroRepository(
        api: MyApi,
    ): MyRepository {
        return MyImpl(api)
    }


    @Provides
    @Singleton
    fun provideLanguageRepository(
        api: LanguageApi,
    ): LanguageRepository {
        return LanguageImpl(api)
    }

}