package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestGetListPreview
import com.pools.store.data.request.RequestGetProfile
import com.pools.store.domain.model.ListPreviewDomain
import com.pools.store.domain.model.ProfileDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetListPreviewBaseUseCase = BaseUseCase<RequestGetListPreview, Flow<Resource<ListPreviewDomain>>>

class GetListPreviewUseCase @Inject constructor(
    private val myRepository: MyRepository
) : GetListPreviewBaseUseCase {
    override suspend fun execute(params: RequestGetListPreview) =
        myRepository.getListPreview(params)
}