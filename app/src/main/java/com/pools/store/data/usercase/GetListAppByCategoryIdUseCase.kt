package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestGetListByCategoryId
import com.pools.store.domain.model.ListAppDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


typealias GetListByCategoryIdBaseUseCase = BaseUseCase<RequestGetListByCategoryId, Flow<Resource<ListAppDomain>>>
class GetListByCategoryIdUseCase @Inject constructor(
    private val myRepository: MyRepository
):GetListByCategoryIdBaseUseCase {
    override suspend fun execute(params: RequestGetListByCategoryId) = myRepository.getListByCategoryId(params)

}