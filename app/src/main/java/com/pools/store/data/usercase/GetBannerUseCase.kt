package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestGetBanner
import com.pools.store.domain.model.BannerDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetBannerBaseUseCase = BaseUseCase<RequestGetBanner, Flow<Resource<BannerDomain>>>

class GetBannerUseCase @Inject constructor(
    private val myRepository: MyRepository
) : GetBannerBaseUseCase {
    override suspend fun execute(params: RequestGetBanner) =
        myRepository.getBanner(params)
}