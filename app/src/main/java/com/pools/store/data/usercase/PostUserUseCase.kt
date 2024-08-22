package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestPostUser
import com.pools.store.domain.model.PostUserDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias PostUserBaseUseCase = BaseUseCase<RequestPostUser, Flow<Resource<PostUserDomain>>>

class PostUserUseCase @Inject constructor(
    private val myRepository: MyRepository
) : PostUserBaseUseCase {
    override suspend fun execute(params: RequestPostUser) =
        myRepository.postUser(params)
}