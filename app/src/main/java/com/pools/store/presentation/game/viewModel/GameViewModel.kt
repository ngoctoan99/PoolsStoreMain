package com.pools.store.presentation.game.viewModel

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
import com.pools.store.data.request.RequestGetBanner
import com.pools.store.data.request.RequestGetBannerData
import com.pools.store.data.request.RequestGetListApp
import com.pools.store.data.request.RequestGetListAppData
import com.pools.store.data.request.RequestGetListCategory
import com.pools.store.data.request.RequestGetListCategoryData
import com.pools.store.data.usercase.GetBannerUseCase
import com.pools.store.data.usercase.GetListAppUseCase
import com.pools.store.data.usercase.GetListCategoryUseCase
import com.pools.store.domain.model.AppDomain
import com.pools.store.domain.model.BannerDomain
import com.pools.store.domain.model.CategoryDomain
import com.pools.store.domain.model.ListAppDomain
import com.pools.store.domain.model.ListCategoryDomain
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

sealed class GetListAppState {
    data class Loading(val isLoading: Boolean = false) : GetListAppState()
    data class Error(var error: String = "", var statusCode: Int?) : GetListAppState()
    data class Success(val data: ListAppDomain) : GetListAppState()
}

sealed class GetListCategoryState {
    data class Loading(val isLoading: Boolean = false) : GetListCategoryState()
    data class Error(var error: String = "", var statusCode: Int?) : GetListCategoryState()
    data class Success(val data: ListCategoryDomain) : GetListCategoryState()
}

sealed class GetBannerState {
    data class Loading(val isLoading: Boolean = false) : GetBannerState()
    data class Error(var error: String = "", var statusCode: Int?) : GetBannerState()
    data class Success(val data: BannerDomain) : GetBannerState()
}


@HiltViewModel
class GameViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val cachePreferencesHelper: CachePreferencesHelper,
    private val getListAppUseCase: GetListAppUseCase,
    private val getListCategoryUseCase: GetListCategoryUseCase,
    private val getBannerUseCase: GetBannerUseCase,
    private val myApi: MyApi
) : BaseViewModel(contextProvider) {

    private val _getBannerState =
        MutableStateFlow<GetBannerState>(GetBannerState.Loading(false))
    val getBannerState = _getBannerState.asStateFlow()

    private val _getListCategoryState =
        MutableStateFlow<GetListCategoryState>(GetListCategoryState.Loading(false))
    val getListCategoryState = _getListCategoryState.asStateFlow()

    val categoryHaveData = MutableLiveData<Boolean>()
    private lateinit var _getListCategoryPagingFlow: Flow<PagingData<CategoryDomain>>
    val getListCategoryPagingFlow: Flow<PagingData<CategoryDomain>>
        get() = _getListCategoryPagingFlow
    /**
     * game screen
     * */
    private val _getListByTwoParamsState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListByTwoParamsState = _getListByTwoParamsState.asStateFlow()

    private val _getListTrendingAppState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListTrendingAppState = _getListTrendingAppState.asStateFlow()

    private val _getListNewGameAppState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListNewGameAppState = _getListNewGameAppState.asStateFlow()

    private val _getListPlayToEarnAppState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListPlayToEarnAppState = _getListPlayToEarnAppState.asStateFlow()




    fun getListApp(accessToken: String, tags: String) {
        launchCoroutineIO {
            getListAppUseCase.execute(
                RequestGetListApp(
                    bearer_token = accessToken,
                    data = RequestGetListAppData(
                        pageCurrent = 1,
                        pageSize = 10,
                        noLimit = false,
                        createdAt = -1,
                        tags = tags
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        if (tags == Constant.TRENDING) _getListTrendingAppState.value =
                            GetListAppState.Loading(true)
                        else if (tags == Constant.NEW_GAMES) _getListNewGameAppState.value =
                            GetListAppState.Loading(true)
                        else if (tags == Constant.PLAY_TO_EARN) _getListPlayToEarnAppState.value =
                            GetListAppState.Loading(true)
                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            if (tags == Constant.TRENDING) _getListTrendingAppState.value =
                                GetListAppState.Success(result.data)
                            else if (tags == Constant.NEW_GAMES) _getListNewGameAppState.value =
                                GetListAppState.Success(result.data)
                            else if (tags == Constant.PLAY_TO_EARN) _getListPlayToEarnAppState.value =
                                GetListAppState . Success (result.data)

                        }
                    }

                    is Resource.Error -> {
                        if (tags == Constant.TRENDING) _getListTrendingAppState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                        else if (tags == Constant.NEW_GAMES) _getListNewGameAppState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                        else if (tags == Constant.PLAY_TO_EARN) _getListPlayToEarnAppState.value =
                            GetListAppState.Error(result.message, result.statusCode)

                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun getListByTwoParams(accessToken: String, tags: String, types: String) {
        launchCoroutineIO {
            getListAppUseCase.execute(
                RequestGetListApp(
                    bearer_token = accessToken,
                    data = RequestGetListAppData(
                        pageCurrent = 1,
                        pageSize = 10,
                        noLimit = false,
                        createdAt = -1,
                        tags = tags,
                        types = types
                    )
                )

            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {

                        _getListByTwoParamsState.value = GetListAppState.Loading(true)

                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            _getListByTwoParamsState.value = GetListAppState.Success(result.data)

                        }
                        Log.d("TTT offer game:", " ${result.data}")
                    }

                    is Resource.Error -> {
                        _getListByTwoParamsState.value =
                            GetListAppState.Error(result.message, result.statusCode)

                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun getListCategory(accessToken: String, apkType:String) {
        launchCoroutineIO {
            getListCategoryUseCase.execute(
                RequestGetListCategory(
                    bearer_token = accessToken,
                    data = RequestGetListCategoryData(
                        pageCurrent = 1,
                        pageSize = 10,
                        noLimit = false,
                        createdAt = -1,
                        apkType = apkType
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getListCategoryState.value = GetListCategoryState.Loading(true)
                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            _getListCategoryState.value = GetListCategoryState.Success(result.data)
                        }
                    }

                    is Resource.Error -> {
                        _getListCategoryState.value =
                            GetListCategoryState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

//    fun getListCategory(): Flow<PagingData<CategoryDomain>> {
//        _getListCategoryPagingFlow = Pager(PagingConfig(pageSize = Constant.LIMIT_IN_ONE_PAGE)) {
//            ListCategoryPagingSource()
//        }.flow.cachedIn(viewModelScope)
//        return _getListCategoryPagingFlow
//    }
//
//    inner class ListCategoryPagingSource : PagingSource<Int, CategoryDomain>() {
//        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryDomain> {
//            return try {
//                val page = params.key ?: 1
//                val response = myApi.getListCategory(
//                    authorization = cachePreferencesHelper.accessToken,
//                    pageCurrent = page,
//                    pageSize = Constant.LIMIT_IN_ONE_PAGE,
//                    noLimit = false,
//                    createdAt = -1,
//                    apkType = "apkType"
//
//                )
//                val data = response.data?.toDomain()
//
//                categoryHaveData.value = data!!.items.isNotEmpty()
//                val nextPage = if (data.items.isEmpty() || data.paging.totalSize <= page * Constant.LIMIT_IN_ONE_PAGE) null else page + 1
//
//                LoadResult.Page(
//                    data = data.items,
//                    prevKey = null,
//                    nextKey = nextPage
//                )
//            } catch (e: Exception) {
//                LoadResult.Error(e)
//            }
//        }
//
//        override fun getRefreshKey(state: PagingState<Int, CategoryDomain>): Int? {
//            return null
//        }
//
//    }

    fun getBanner(accessToken: String) {
        launchCoroutineIO {
            getBannerUseCase.execute(
                RequestGetBanner(
                    bearer_token = accessToken,
                    data = RequestGetBannerData(
                        pageCurrent = 1,
                        pageSize = Constant.LIMIT_IN_ONE_PAGE,
                        noLimit = false,
                        createdAt = -1
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getBannerState.value = GetBannerState.Loading(true)
                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            _getBannerState.value = GetBannerState.Success(result.data)
                        }
                    }

                    is Resource.Error -> {
                        _getBannerState.value =
                            GetBannerState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}