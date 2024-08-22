package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestRatingPost
import com.pools.store.domain.model.RatingPostDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias PostRatingBaseUseCase = BaseUseCase<RequestRatingPost, Flow<Resource<RatingPostDomain>>>

class PostRatingUseCase @Inject constructor(
    private val myRepository: MyRepository
) : PostRatingBaseUseCase {
    override suspend fun execute(params: RequestRatingPost) =
        myRepository.postRating(params)
}