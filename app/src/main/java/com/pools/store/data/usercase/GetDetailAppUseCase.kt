package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestDetailApp
import com.pools.store.domain.model.DetailAppDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetDetailAppBaseUseCase = BaseUseCase<RequestDetailApp, Flow<Resource<DetailAppDomain>>>

class GetDetailAppUseCase @Inject constructor(
    private val myRepository: MyRepository
) : GetDetailAppBaseUseCase {
    override suspend fun execute(params: RequestDetailApp) =
        myRepository.getDetailApp(params)
}