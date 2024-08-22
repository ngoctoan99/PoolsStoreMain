package com.pools.store.presentation.app.viewModel

import androidx.lifecycle.viewModelScope
import com.pools.store.base.BaseViewModel
import com.pools.store.data.remote.MyApi
import com.pools.store.data.request.RequestGetBanner
import com.pools.store.data.request.RequestGetBannerData
import com.pools.store.data.request.RequestGetListApp
import com.pools.store.data.request.RequestGetListAppData
import com.pools.store.data.request.RequestGetListCategory
import com.pools.store.data.request.RequestGetListCategoryData
import com.pools.store.data.usercase.GetBannerUseCase
import com.pools.store.data.usercase.GetListAppUseCase
import com.pools.store.data.usercase.GetListCategoryUseCase
import com.pools.store.domain.model.BannerDomain
import com.pools.store.domain.model.ListAppDomain
import com.pools.store.domain.model.ListCategoryDomain
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
class AppViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val getListAppUseCase: GetListAppUseCase,
    private val getListCategoryUseCase: GetListCategoryUseCase,
    private val getBannerUseCase: GetBannerUseCase,
    private val myApi: MyApi
) : BaseViewModel(contextProvider) {

    private val _getListAppState = MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListAppState = _getListAppState.asStateFlow()

    private val _getListHostDiscountAppState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListHostDiscountAppState = _getListHostDiscountAppState.asStateFlow()

    private val _getListRecommendAppState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListRecommendAppState = _getListRecommendAppState.asStateFlow()

    private val _getListNoInternetAppState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListNoInternetAppState = _getListNoInternetAppState.asStateFlow()

    /**
     * game category
     * */
    private val _getListCategoryState =
        MutableStateFlow<GetListCategoryState>(GetListCategoryState.Loading(false))
    val getListCategoryState = _getListCategoryState.asStateFlow()

    /**
     * banner
     * */
    private val _getBannerState = MutableStateFlow<GetBannerState>(GetBannerState.Loading(false))
    val getBannerState = _getBannerState.asStateFlow()

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
                        if (tags == Constant.HOT_DISCOUNT) _getListHostDiscountAppState.value =
                            GetListAppState.Loading(true)
                        else if (tags == Constant.RECOMMEND) _getListRecommendAppState.value =
                            GetListAppState.Loading(true)
                        else if (tags == Constant.NO_INTERNET) _getListNoInternetAppState.value =
                            GetListAppState.Loading(true)
                        else if (tags == Constant.LEADER_BOARD) _getListAppState.value =
                            GetListAppState.Loading(true)
                        else if (tags == Constant.OFFER) _getListAppState.value =
                            GetListAppState.Loading(true)
                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            if (tags == Constant.HOT_DISCOUNT) _getListHostDiscountAppState.value =
                                GetListAppState.Success(result.data)
                            else if (tags == Constant.RECOMMEND) _getListRecommendAppState.value =
                                GetListAppState.Success(result.data)
                            else if (tags == Constant.NO_INTERNET) _getListNoInternetAppState.value =
                                GetListAppState.Success(result.data)
                            else if (tags == Constant.LEADER_BOARD) _getListAppState.value =
                                GetListAppState.Success(result.data)
                            else if (tags == Constant.OFFER) _getListAppState.value =
                                GetListAppState.Success(result.data)
                        }
                    }

                    is Resource.Error -> {
                        if (tags == Constant.HOT_DISCOUNT) _getListHostDiscountAppState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                        else if (tags == Constant.RECOMMEND) _getListRecommendAppState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                        else if (tags == Constant.NO_INTERNET) _getListNoInternetAppState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                        else if (tags == Constant.LEADER_BOARD) _getListAppState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                        else if (tags == Constant.OFFER) _getListAppState.value =
                            GetListAppState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun getListAppByTwoParams(accessToken: String, tags: String, types: String) {
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

                        _getListAppState.value = GetListAppState.Loading(true)

                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
//                            _getListAppState.value = GetListAppState.Success(result.data)
                            _getListAppState.value = GetListAppState.Success(result.data)

                        }
                    }

                    is Resource.Error -> {
                        _getListAppState.value =
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
