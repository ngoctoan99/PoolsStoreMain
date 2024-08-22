package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestGetListFavorite
import com.pools.store.domain.model.ListFavoriteDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetListFavoriteBaseUseCase = BaseUseCase<RequestGetListFavorite, Flow<Resource<ListFavoriteDomain>>>
class GetListFavoriteUseCase @Inject constructor(
    private val myRepository: MyRepository
):GetListFavoriteBaseUseCase {
    override suspend fun execute(params: RequestGetListFavorite) = myRepository.getListFavorite(params)
}