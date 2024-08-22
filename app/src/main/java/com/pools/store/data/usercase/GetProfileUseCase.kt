package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestGetProfile
import com.pools.store.domain.model.ProfileDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetProfileBaseUseCase = BaseUseCase<RequestGetProfile, Flow<Resource<ProfileDomain>>>

class GetProfileUseCase @Inject constructor(
    private val myRepository: MyRepository) : GetProfileBaseUseCase {
    override suspend fun execute(params: RequestGetProfile) =
        myRepository.getProfile(params)
}