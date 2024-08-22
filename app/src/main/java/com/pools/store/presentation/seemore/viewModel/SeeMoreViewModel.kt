package com.pools.store.presentation.seemore.viewModel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.pools.store.base.BaseViewModel
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.data.remote.MyApi
import com.pools.store.data.remote.dto.toDomain
import com.pools.store.data.request.RequestGetListApp
import com.pools.store.data.request.RequestGetListAppData
import com.pools.store.data.request.RequestGetListFavorite
import com.pools.store.data.usercase.GetListAppUseCase
import com.pools.store.data.usercase.GetListFavoriteUseCase
import com.pools.store.domain.model.AppDomain
import com.pools.store.domain.model.DetailAppDomain
import com.pools.store.domain.model.ListAppDomain
import com.pools.store.domain.model.ListFavoriteDomain
import com.pools.store.utils.Constant
import com.pools.store.utils.CoroutineContextProvider
import com.pools.store.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject



@HiltViewModel
class SeeMoreViewModel  @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val cachePreferencesHelper: CachePreferencesHelper,
    private val myApi: MyApi
): BaseViewModel(contextProvider) {


    val isSeeMoreHaveData = MutableLiveData<Boolean>()
    private lateinit var _seeMoreAppPagingFlow: Flow<PagingData<AppDomain>>
    val seeMoreAppPagingFlow: Flow<PagingData<AppDomain>>
        get() = _seeMoreAppPagingFlow

    fun getListSeeMoreApp(tags:String): Flow<PagingData<AppDomain>> {
        _seeMoreAppPagingFlow = Pager(PagingConfig(pageSize = Constant.LIMIT_IN_ONE_PAGE)) {
            SeeMorePagingSource(tags = tags)
        }.flow.cachedIn(viewModelScope)
        return _seeMoreAppPagingFlow
    }

    inner class SeeMorePagingSource(val tags:String)  : PagingSource<Int, AppDomain>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppDomain> {
            return try {
                val page = params.key ?: 1

                val response = myApi.getListApp(
                    authorization = cachePreferencesHelper.accessToken,
                    pageCurrent = page,
                    pageSize = Constant.LIMIT_IN_ONE_PAGE,
                    noLimit = false,
                    createdAt = -1,
                    q = "",
                    tags = tags,
                    types = null
                )
                val data = response.data?.toDomain()

                isSeeMoreHaveData.value = data!!.items.isNotEmpty()
                val nextPage = if (data.items.isEmpty() || data.paging.totalSize <= page * Constant.LIMIT_IN_ONE_PAGE) null else page + 1

                LoadResult.Page(
                    data = data.items,
                    prevKey = null,
                    nextKey = nextPage
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, AppDomain>): Int? {
            return null
        }

    }
}