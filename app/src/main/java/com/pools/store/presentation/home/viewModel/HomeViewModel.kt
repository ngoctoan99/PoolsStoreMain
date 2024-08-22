package com.pools.store.presentation.home.viewModel

import androidx.lifecycle.viewModelScope
import com.pools.store.base.BaseViewModel
import com.pools.store.data.request.RequestGetBanner
import com.pools.store.data.request.RequestGetBannerData
import com.pools.store.data.request.RequestGetListApp
import com.pools.store.data.request.RequestGetListAppData
import com.pools.store.data.usercase.GetBannerUseCase
import com.pools.store.data.usercase.GetListAppUseCase
import com.pools.store.domain.model.BannerDomain
import com.pools.store.domain.model.ListAppDomain
import com.pools.store.domain.model.ProfileDomain
import com.pools.store.utils.Constant
import com.pools.store.utils.CoroutineContextProvider
import com.pools.store.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

sealed class GetProfileState {
    data class Loading(val isLoading: Boolean = false) : GetProfileState()
    data class Error(var error: String = "", var statusCode: Int?) : GetProfileState()
    data class Success(val data: ProfileDomain) : GetProfileState()
}

sealed class GetListAppState {
    data class Loading(val isLoading: Boolean = false) : GetListAppState()
    data class Error(var error: String = "", var statusCode: Int?) : GetListAppState()
    data class Success(val data: ListAppDomain) : GetListAppState()
}

sealed class GetBannerState {
    data class Loading(val isLoading: Boolean = false) : GetBannerState()
    data class Error(var error: String = "", var statusCode: Int?) : GetBannerState()
    data class Success(val data: BannerDomain) : GetBannerState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val getListAppUseCase: GetListAppUseCase,
    private val getBannerUseCase: GetBannerUseCase,
    ) : BaseViewModel(contextProvider) {



    private val _getListAppForYouState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListAppForYouState = _getListAppForYouState.asStateFlow()

    private val _getListAppTrendingState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListAppTrendingState = _getListAppTrendingState.asStateFlow()

    private val _getListAppTotalSupportState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListAppTotalSupportState = _getListAppTotalSupportState.asStateFlow()

    private val _getListAppPoolsCollectionState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListAppPoolsCollectionState = _getListAppPoolsCollectionState.asStateFlow()


    private val _getBannerState =
        MutableStateFlow<GetBannerState>(GetBannerState.Loading(false))
    val getBannerState = _getBannerState.asStateFlow()


    fun getListAppForYou(accessToken : String , tags: String) {
        launchCoroutineIO {
            getListAppUseCase.execute(
                RequestGetListApp(
                    bearer_token = accessToken,
                    data = RequestGetListAppData(
                        pageCurrent = 1,
                        pageSize = Constant.LIMIT_IN_ONE_PAGE,
                        noLimit = false,
                        createdAt = -1,
                        tags = tags
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getListAppForYouState.value = GetListAppState.Loading(true)
                    }
                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            _getListAppForYouState.value =
                                GetListAppState.Success(result.data)
                        }
                    }
                    is Resource.Error -> {
                        _getListAppForYouState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun getBanner(accessToken : String) {
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
                            _getBannerState.value =
                                GetBannerState.Success(result.data)
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


    fun getListAppTrending(accessToken : String , tags: String) {
        launchCoroutineIO {
            getListAppUseCase.execute(
                RequestGetListApp(
                    bearer_token = accessToken,
                    data = RequestGetListAppData(
                        pageCurrent = 1,
                        pageSize = Constant.LIMIT_IN_ONE_PAGE,
                        noLimit = false,
                        createdAt = -1,
                        tags = tags
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getListAppTrendingState.value = GetListAppState.Loading(true)
                    }
                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            _getListAppTrendingState.value =
                                GetListAppState.Success(result.data)
                        }
                    }
                    is Resource.Error -> {
                        _getListAppTrendingState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun getListAppTotalSupport(accessToken : String , tags: String) {
        launchCoroutineIO {
            getListAppUseCase.execute(
                RequestGetListApp(
                    bearer_token = accessToken,
                    data = RequestGetListAppData(
                        pageCurrent = 1,
                        pageSize = Constant.LIMIT_IN_ONE_PAGE,
                        noLimit = false,
                        createdAt = -1,
                        tags = tags
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getListAppTotalSupportState.value = GetListAppState.Loading(true)
                    }
                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            _getListAppTotalSupportState.value =
                                GetListAppState.Success(result.data)
                        }
                    }
                    is Resource.Error -> {
                        _getListAppTotalSupportState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun getListAppPoolCollection(accessToken : String , tags: String) {
        launchCoroutineIO {
            getListAppUseCase.execute(
                RequestGetListApp(
                    bearer_token = accessToken,
                    data = RequestGetListAppData(
                        pageCurrent = 1,
                        pageSize = Constant.LIMIT_IN_ONE_PAGE,
                        noLimit = false,
                        createdAt = -1,
                        tags = tags
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getListAppPoolsCollectionState.value = GetListAppState.Loading(true)
                    }
                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            _getListAppPoolsCollectionState.value =
                                GetListAppState.Success(result.data)
                        }
                    }
                    is Resource.Error -> {
                        _getListAppPoolsCollectionState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    }