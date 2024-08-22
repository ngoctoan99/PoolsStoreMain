package com.pools.store.data.repository

import com.pools.store.data.handleError
import com.pools.store.data.remote.LanguageApi
import com.pools.store.data.remote.dto.toDomain
import com.pools.store.data.request.RequestGetLanguage
import com.pools.store.domain.model.LanguageDomain
import com.pools.store.domain.repository.LanguageRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LanguageImpl @Inject constructor(
    val api: LanguageApi
) : LanguageRepository {
    override suspend fun getLanguage(request: RequestGetLanguage): Flow<Resource<LanguageDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getLanguage(
                authorization = request.bearer_token,
                language = request.data.language
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }
}