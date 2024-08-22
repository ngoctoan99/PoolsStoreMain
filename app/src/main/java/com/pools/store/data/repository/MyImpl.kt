package com.pools.store.data.repository

import com.pools.store.data.handleError
import com.pools.store.data.remote.MyApi
import com.pools.store.data.remote.dto.toDomain
import com.pools.store.data.request.RequestDetailApp
import com.pools.store.data.request.RequestDetailPublisher
import com.pools.store.data.request.RequestGetBanner
import com.pools.store.data.request.RequestGetHistoryDownload
import com.pools.store.data.request.RequestGetListApp
import com.pools.store.data.request.RequestGetListByCategoryId
import com.pools.store.data.request.RequestGetListCategory
import com.pools.store.data.request.RequestGetListFavorite
import com.pools.store.data.request.RequestGetListPreview
import com.pools.store.data.request.RequestGetProfile
import com.pools.store.data.request.RequestMyRating
import com.pools.store.data.request.RequestNotiHistory
import com.pools.store.data.request.RequestPostDownload
import com.pools.store.data.request.RequestPostUser
import com.pools.store.data.request.RequestPutListFavorite
import com.pools.store.data.request.RequestRatingPost
import com.pools.store.domain.model.BannerDomain
import com.pools.store.domain.model.DetailAppDomain
import com.pools.store.domain.model.DetailPublisherDomain
import com.pools.store.domain.model.DownloadHistoryDomain
import com.pools.store.domain.model.ListAppDomain
import com.pools.store.domain.model.ListCategoryDomain
import com.pools.store.domain.model.ListNotiHistoryDomain
import com.pools.store.domain.model.ListFavoriteDomain
import com.pools.store.domain.model.ListPreviewDomain
import com.pools.store.domain.model.MyRatingDomain
import com.pools.store.domain.model.PostDownloadDomain
import com.pools.store.domain.model.PostUserDomain
import com.pools.store.domain.model.ProfileDomain
import com.pools.store.domain.model.RatingPostDomain
import com.pools.store.domain.repository.MyRepository
import com.pools.store.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyImpl @Inject constructor(
    val api: MyApi
) : MyRepository {

    override suspend fun getProfile(request: RequestGetProfile): Flow<Resource<ProfileDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getProfile(authorization = request.bearer_token).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getListApp(request: RequestGetListApp): Flow<Resource<ListAppDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getListApp(
                authorization = request.bearer_token,
                pageCurrent = request.data.pageCurrent,
                pageSize = request.data.pageSize,
                noLimit = request.data.noLimit,
                createdAt = request.data.createdAt,
                q = request.data.q,
                tags = request.data.tags,
                types = request.data.types
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getListPreview(request: RequestGetListPreview): Flow<Resource<ListPreviewDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getListPreview(
                authorization = request.bearer_token,
                pageCurrent = request.data.pageCurrent,
                pageSize = request.data.pageSize,
                noLimit = request.data.noLimit,
                createdAt = request.data.createdAt,
                apkId = request.data.apkId,
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun postRating(request: RequestRatingPost): Flow<Resource<RatingPostDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.postRating(
                authorization = request.bearer_token,
                request = request.data
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getListCategory(request: RequestGetListCategory): Flow<Resource<ListCategoryDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getListCategory(
                authorization = request.bearer_token,
                pageCurrent = request.data.pageCurrent,
                pageSize = request.data.pageSize,
                noLimit = request.data.noLimit,
                createdAt = request.data.createdAt,
                apkType = request.data.apkType,
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getDetailApp(request: RequestDetailApp): Flow<Resource<DetailAppDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getDetailApp(
                authorization = request.bearer_token,
                id = request.data.id
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getMyRating(request: RequestMyRating): Flow<Resource<MyRatingDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getMyRating(
                authorization = request.bearer_token,
                apkId = request.data.apkId
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }


    override suspend fun getListAppSearch(request: RequestGetListApp): Flow<Resource<ListAppDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getListApp(
                authorization = request.bearer_token,
                pageCurrent = request.data.pageCurrent,
                pageSize = request.data.pageSize,
                noLimit = request.data.noLimit,
                createdAt = request.data.createdAt,
                q = request.data.q,
                tags = request.data.tags,
                types = request.data.types
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getBanner(request: RequestGetBanner): Flow<Resource<BannerDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getBanner(
                authorization = request.bearer_token,
                pageCurrent = request.data.pageCurrent,
                pageSize = request.data.pageSize,
                noLimit = request.data.noLimit,
                createdAt = request.data.createdAt,
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getDetailPublisher(request: RequestDetailPublisher): Flow<Resource<DetailPublisherDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getDetailPublisher(
                authorization = request.bearer_token,
                id = request.data.id
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun postDownload(request: RequestPostDownload): Flow<Resource<PostDownloadDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.postDownload(
                authorization = request.bearer_token,
                request = request.data
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getListByCategoryId(request: RequestGetListByCategoryId): Flow<Resource<ListAppDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getListByCategoryId(
                authorization = request.bearer_token,
                pageCurrent = request.data.pageCurrent,
                pageSize = request.data.pageSize,
                noLimit = request.data.noLimit,
                createdAt = request.data.createdAt,
                q = request.data.q,
                categoryIds = request.data.categoryIds
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getListFavorite(request: RequestGetListFavorite): Flow<Resource<ListFavoriteDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getListFavorite(authorization = request.bearer_token).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getDownloadHistory(request: RequestGetHistoryDownload): Flow<Resource<DownloadHistoryDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getDownloadHistory(
                authorization = request.bearer_token,
                pageCurrent = request.data.pageCurrent,
                pageSize = request.data.pageSize,
                noLimit = request.data.noLimit,
                createdAt = request.data.createdAt,
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun postUser(request: RequestPostUser): Flow<Resource<PostUserDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.postUser(
                authorization = request.bearer_token,
                request = request.data
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun getListNoti(request: RequestNotiHistory): Flow<Resource<ListNotiHistoryDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.getListNoti(
                authorization = request.bearer_token,
                pageCurrent = request.data.pageCurrent,
                pageSize = request.data.pageSize,
                noLimit = request.data.noLimit,
                createdAt = request.data.createdAt,
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }

    override suspend fun putAddFavorite(request: RequestPutListFavorite): Flow<Resource<ListFavoriteDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.putAddFavorite(
                authorization = request.bearer_token,
                request = request.data
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }

    }

    override suspend fun putRemoveFavorite(request: RequestPutListFavorite): Flow<Resource<ListFavoriteDomain>> {
        return flow {
            emit(Resource.Loading())
            val user = api.putRemoveFavorite(
                authorization = request.bearer_token,
                request = request.data
            ).data?.toDomain()
            user?.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }

    }
}