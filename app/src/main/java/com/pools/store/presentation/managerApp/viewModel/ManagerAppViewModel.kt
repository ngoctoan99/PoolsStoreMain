package com.pools.store.presentation.managerApp.viewModel

import android.util.Log
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
import com.pools.store.data.request.RequestGetProfile
import com.pools.store.data.request.RequestGetProfileData
import com.pools.store.data.usercase.GetProfileUseCase
import com.pools.store.domain.model.AppDomain
import com.pools.store.domain.model.ItemsDownloadHistoryDomain
import com.pools.store.domain.model.ProfileDomain
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
class ManagerAppViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val cachePreferencesHelper: CachePreferencesHelper,
    private val myApi: MyApi
) : BaseViewModel(contextProvider) {

    val isHaveData = MutableLiveData<Boolean>()



    private lateinit var _managerAppPagingFlow: Flow<PagingData<ItemsDownloadHistoryDomain>>
    val managerAppPagingFlow: Flow<PagingData<ItemsDownloadHistoryDomain>>
        get() = _managerAppPagingFlow



    fun getDownloadHistory(): Flow<PagingData<ItemsDownloadHistoryDomain>> {
        _managerAppPagingFlow = Pager(PagingConfig(pageSize = Constant.LIMIT_IN_ONE_PAGE)) {
            DownloadHistoryPagingSource()
        }.flow.cachedIn(viewModelScope)
        return _managerAppPagingFlow
    }


    inner class DownloadHistoryPagingSource : PagingSource<Int, ItemsDownloadHistoryDomain>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemsDownloadHistoryDomain> {
            return try {
                val page = params.key ?: 1
                Log.d("TTTDownloadHistoryPagingSource","${page}")
                val response = myApi.getDownloadHistory(
                    authorization = cachePreferencesHelper.accessToken,
                    pageCurrent = page,
                    pageSize = Constant.LIMIT_IN_ONE_PAGE,
                    noLimit = false,
                    createdAt = -1,
                )
                Log.d("TTTDownloadHistoryPagingSource","${response}")
                val data = response.data?.toDomain()
                isHaveData.value = data!!.items.isNotEmpty()
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

        override fun getRefreshKey(state: PagingState<Int, ItemsDownloadHistoryDomain>): Int? {
            return null
        }

    }
}