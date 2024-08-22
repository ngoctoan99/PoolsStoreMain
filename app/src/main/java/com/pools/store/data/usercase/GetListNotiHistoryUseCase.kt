package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestGetListApp
import com.pools.store.data.request.RequestNotiHistory
import com.pools.store.domain.model.ListNotiHistoryDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetListNotiHistoryBaseUseCase = BaseUseCase<RequestNotiHistory, Flow<Resource<ListNotiHistoryDomain>>>

class GetListNotiHistoryUseCase @Inject constructor(
    private val myRepository: MyRepository
) : GetListNotiHistoryBaseUseCase {
    override suspend fun execute(params: RequestNotiHistory) =
        myRepository.getListNoti(params)
}