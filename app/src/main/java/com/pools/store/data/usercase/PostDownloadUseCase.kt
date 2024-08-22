package com.pools.store.data.usercase

import com.pools.store.base.BaseUseCase
import com.pools.store.data.request.RequestPostDownload
import com.pools.store.domain.model.PostDownloadDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias PostDownloadBaseUseCase = BaseUseCase<RequestPostDownload, Flow<Resource<PostDownloadDomain>>>

class PostDownloadUseCase @Inject constructor(
    private val myRepository: MyRepository
) : PostDownloadBaseUseCase {
    override suspend fun execute(params: RequestPostDownload) =
        myRepository.postDownload(params)
}