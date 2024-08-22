package com.pools.store.domain.model


data class LanguageDomain(
    val data: LanguageDataDomain,
)
data class LanguageDataDomain(
   val noData: String,
   val login: String,
   val pleaseLoginInUsingThePoolsDashboardAppToContinue: String,
   val close: String,
)