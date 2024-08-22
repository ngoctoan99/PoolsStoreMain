package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestDetailPublisher
import com.pools.store.domain.model.DetailPublisherDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetDetailPublisherBaseUseCase = BaseUseCase<RequestDetailPublisher, Flow<Resource<DetailPublisherDomain>>>

class GetDetailPublisherUseCase @Inject constructor(
    private val myRepository: MyRepository
) : GetDetailPublisherBaseUseCase {
    override suspend fun execute(params: RequestDetailPublisher) =
        myRepository.getDetailPublisher(params)
}