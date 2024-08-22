package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestGetListCategory
import com.pools.store.domain.model.ListCategoryDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetListCategoryBaseUseCase = BaseUseCase<RequestGetListCategory, Flow<Resource<ListCategoryDomain>>>
class GetListCategoryUseCase @Inject constructor(
    private val myRepository: MyRepository
) : GetListCategoryBaseUseCase {
    override suspend fun execute(params: RequestGetListCategory) =
        myRepository.getListCategory(params)
}