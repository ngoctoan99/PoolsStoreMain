package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestPutListFavorite
import com.pools.store.domain.model.ListFavoriteDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias PutAddFavoriteBaseUseCase = BaseUseCase<RequestPutListFavorite, Flow<Resource<ListFavoriteDomain>>>

class PutAddFavoriteUseCase  @Inject constructor(
    private val myRepository: MyRepository
):PutAddFavoriteBaseUseCase {
    override suspend fun execute(params: RequestPutListFavorite) = myRepository.putAddFavorite(params)
}