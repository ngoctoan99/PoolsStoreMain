package com.pools.store.presentation.group.viewModel

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
import com.pools.store.domain.model.AppDomain
import com.pools.store.utils.Constant
import com.pools.store.utils.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class GroupViewModel  @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val cachePreferencesHelper: CachePreferencesHelper,
    private val myApi: MyApi
): BaseViewModel(contextProvider) {


    val isGroupHaveData = MutableLiveData<Boolean>()
    private lateinit var _groupPagingFlow: Flow<PagingData<AppDomain>>
    val groupPagingFlow: Flow<PagingData<AppDomain>>
        get() = _groupPagingFlow

    fun getListGroup(categoryId:String): Flow<PagingData<AppDomain>> {
        _groupPagingFlow = Pager(PagingConfig(pageSize = Constant.LIMIT_IN_ONE_PAGE)) {
            GroupPagingSource(categoryId = categoryId)
        }.flow.cachedIn(viewModelScope)
        return _groupPagingFlow
    }

    inner class GroupPagingSource(val categoryId:String)  : PagingSource<Int, AppDomain>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppDomain> {
            return try {
                val page = params.key ?: 1

                val response = myApi.getListByCategoryId(
                    authorization = cachePreferencesHelper.accessToken,
                    pageCurrent = page,
                    pageSize = Constant.LIMIT_IN_ONE_PAGE,
                    noLimit = false,
                    createdAt = -1,
                    q = "",
                    categoryIds = categoryId
                )
                val data = response.data?.toDomain()

                isGroupHaveData.value = data!!.items.isNotEmpty()
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