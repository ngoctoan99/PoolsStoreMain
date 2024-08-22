package com.pools.store.domain.repository

import com.pools.store.data.request.RequestGetLanguage
import com.pools.store.domain.model.LanguageDomain
import com.pools.store.domain.model.ProfileDomain
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    suspend fun getLanguage(
        request: RequestGetLanguage
    ): Flow<Resource<LanguageDomain>>
}