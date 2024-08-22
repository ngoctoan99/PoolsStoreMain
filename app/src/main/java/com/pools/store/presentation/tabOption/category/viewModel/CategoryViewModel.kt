package com.pools.store.presentation.tabOption.category.viewModel


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
import com.pools.store.domain.model.CategoryDomain
import com.pools.store.utils.Constant
import com.pools.store.utils.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel  @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val cachePreferencesHelper: CachePreferencesHelper,
    private val myApi: MyApi
): BaseViewModel(contextProvider) {


    val isCategoryHaveData = MutableLiveData<Boolean>()
    private lateinit var _categoryPagingFlow: Flow<PagingData<CategoryDomain>>
    val groupPagingFlow: Flow<PagingData<CategoryDomain>>
        get() = _categoryPagingFlow

    fun getListCategory(apkType:String): Flow<PagingData<CategoryDomain>> {
        _categoryPagingFlow = Pager(PagingConfig(pageSize = Constant.LIMIT_IN_ONE_PAGE)) {
            CategoryPagingSource( apkType = apkType)
        }.flow.cachedIn(viewModelScope)
        return _categoryPagingFlow
    }

    inner class CategoryPagingSource(val apkType:String)  : PagingSource<Int, CategoryDomain>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryDomain> {
            return try {
                val page = params.key ?: 1

                val response = myApi.getListCategory(
                    authorization = cachePreferencesHelper.accessToken,
                    pageCurrent = page,
                    pageSize = Constant.LIMIT_IN_ONE_PAGE,
                    noLimit = false,
                    createdAt = -1,
                    apkType = apkType
                )
                val data = response.data?.toDomain()

                isCategoryHaveData.value = data!!.items.isNotEmpty()
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

        override fun getRefreshKey(state: PagingState<Int, CategoryDomain>): Int? {
            return null
        }

    }
}