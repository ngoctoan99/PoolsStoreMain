package com.pools.store.data.usercase

import com.pools.store.data.request.RequestPutListFavorite
import com.pools.store.domain.repository.MyRepository
import javax.inject.Inject

class PutRemoveFavoriteUseCase  @Inject constructor(
    private val myRepository: MyRepository
): PutAddFavoriteBaseUseCase {
    override suspend fun execute(params: RequestPutListFavorite) = myRepository.putRemoveFavorite(params)
}