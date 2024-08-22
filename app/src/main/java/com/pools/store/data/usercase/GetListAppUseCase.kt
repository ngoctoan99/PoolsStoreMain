package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestGetListApp
import com.pools.store.domain.model.ListAppDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetListAppBaseUseCase = BaseUseCase<RequestGetListApp, Flow<Resource<ListAppDomain>>>

class GetListAppUseCase @Inject constructor(
    private val myRepository: MyRepository
) : GetListAppBaseUseCase {
    override suspend fun execute(params: RequestGetListApp) =
        myRepository.getListApp(params)
}