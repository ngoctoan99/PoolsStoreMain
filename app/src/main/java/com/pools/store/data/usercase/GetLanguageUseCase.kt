package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestGetLanguage
import com.pools.store.domain.model.LanguageDomain
import com.pools.store.domain.repository.LanguageRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetLanguageBaseUseCase = BaseUseCase<RequestGetLanguage, Flow<Resource<LanguageDomain>>>

class GetLanguageUseCase @Inject constructor(
    private val myRepository: LanguageRepository
) : GetLanguageBaseUseCase {
    override suspend fun execute(params: RequestGetLanguage) =
        myRepository.getLanguage(params)
}