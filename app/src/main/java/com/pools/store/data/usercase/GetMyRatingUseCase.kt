package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestMyRating
import com.pools.store.domain.model.MyRatingDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetMyRatingBaseUseCase = BaseUseCase<RequestMyRating, Flow<Resource<MyRatingDomain>>>

class GetMyRatingUseCase @Inject constructor(
    private val myRepository: MyRepository
) : GetMyRatingBaseUseCase {
    override suspend fun execute(params: RequestMyRating) =
        myRepository.getMyRating(params)
}