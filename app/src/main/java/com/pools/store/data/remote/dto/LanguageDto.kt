package com.pools.store.data.remote.dto

import com.pools.store.domain.model.LanguageDataDomain
import com.pools.store.domain.model.LanguageDomain
import com.squareup.moshi.Json

data class LanguageDto(
    @Json(name="data")
    val data: LanguageDataDto?,
)
data class LanguageDataDto(
    @Json(name = "No data")
    val noData: String?,
    @Json(name ="Login")
    val login: String?,
    @Json(name = "Please login in using the Pools Dashboard app to continue")
    val pleaseLoginInUsingThePoolsDashboardAppToContinue: String,
    @Json(name = "Close")
    val close: String?,
)

fun LanguageDataDto.toDomain() : LanguageDataDomain{
    return LanguageDataDomain(
        noData = this.noData?:"",
        login = this.login?:"",
        pleaseLoginInUsingThePoolsDashboardAppToContinue = this.pleaseLoginInUsingThePoolsDashboardAppToContinue?:"",
        close = this.close?:"",
    )
}
fun LanguageDto.toDomain() : LanguageDomain{
    return LanguageDomain(
        data = this.data?.toDomain()?:LanguageDataDto(
            noData = "",
            login = "",
            pleaseLoginInUsingThePoolsDashboardAppToContinue = "",
            close = ""
        ).toDomain()
    )
}