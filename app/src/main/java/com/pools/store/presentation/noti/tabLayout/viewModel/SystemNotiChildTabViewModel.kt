package com.pools.store.presentation.noti.tabLayout.viewModel

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
import com.pools.store.domain.model.NotiHistoryDomain
import com.pools.store.utils.Constant
import com.pools.store.utils.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SystemNotiChildTabViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val cachePreferencesHelper: CachePreferencesHelper,
    private val myApi: MyApi
) : BaseViewModel(contextProvider) {

    val isHaveData = MutableLiveData<Boolean>()



    private lateinit var _notiPagingFlow: Flow<PagingData<NotiHistoryDomain>>
    val notiPagingFlow: Flow<PagingData<NotiHistoryDomain>>
        get() = _notiPagingFlow



    fun getListNoti(): Flow<PagingData<NotiHistoryDomain>> {
        _notiPagingFlow = Pager(PagingConfig(pageSize = Constant.LIMIT_IN_ONE_PAGE)) {
            NotiPagingSource()
        }.flow.cachedIn(viewModelScope)
        return _notiPagingFlow
    }


    inner class NotiPagingSource : PagingSource<Int, NotiHistoryDomain>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotiHistoryDomain> {
            return try {
                val page = params.key ?: 1
                Log.d("NotiPagingSource1","${page}")
                val response = myApi.getListNoti(
                    authorization = cachePreferencesHelper.accessToken,
                    pageCurrent = page,
                    pageSize = Constant.LIMIT_IN_ONE_PAGE,
                    noLimit = false,
                    createdAt = -1,
                )
                Log.d("NotiPagingSource2","${response}")
                val data = response.data?.toDomain()
                Log.d("NotiPagingSource3","${data}")
                isHaveData.value = data!!.items.isNotEmpty()
                Log.d("NotiPagingSource4","${isHaveData.value}")
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

        override fun getRefreshKey(state: PagingState<Int, NotiHistoryDomain>): Int? {
            return null
        }

    }
}