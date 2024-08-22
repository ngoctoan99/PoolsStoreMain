package com.pools.store.presentation.detailGame.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.pools.store.base.BaseViewModel
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.data.request.RequestDetailApp
import com.pools.store.data.request.RequestDetailAppData
import com.pools.store.data.request.RequestGetListApp
import com.pools.store.data.request.RequestGetListAppData
import com.pools.store.data.request.RequestGetListPreview
import com.pools.store.data.request.RequestGetListPreviewData
import com.pools.store.data.request.RequestGetProfile
import com.pools.store.data.request.RequestGetProfileData
import com.pools.store.data.request.RequestMyRating
import com.pools.store.data.request.RequestMyRatingData
import com.pools.store.data.request.RequestPostDownload
import com.pools.store.data.request.RequestPostDownloadData
import com.pools.store.data.request.RequestPutListFavorite
import com.pools.store.data.request.RequestPutListFavoriteData
import com.pools.store.data.request.RequestRatingPost
import com.pools.store.data.request.RequestRatingPostData
import com.pools.store.data.usercase.GetDetailAppUseCase
import com.pools.store.data.usercase.PutAddFavoriteUseCase
import com.pools.store.data.usercase.GetListAppUseCase
import com.pools.store.data.usercase.GetListPreviewUseCase
import com.pools.store.data.usercase.GetMyRatingUseCase
import com.pools.store.data.usercase.GetProfileUseCase
import com.pools.store.data.usercase.PostDownloadUseCase
import com.pools.store.data.usercase.PostRatingUseCase
import com.pools.store.data.usercase.PutRemoveFavoriteUseCase
import com.pools.store.domain.model.DetailAppDomain
import com.pools.store.domain.model.ListFavoriteDomain
import com.pools.store.domain.model.ListPreviewDomain
import com.pools.store.domain.model.MyRatingDomain
import com.pools.store.domain.model.PostDownloadDomain
import com.pools.store.domain.model.RatingPostDomain
import com.pools.store.presentation.home.viewModel.GetListAppState
import com.pools.store.presentation.main.GetProfileState
import com.pools.store.utils.CoroutineContextProvider
import com.pools.store.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject


sealed class GetListPreviewState {
    data class Loading(val isLoading: Boolean = false) : GetListPreviewState()
    data class Error(var error: String = "", var statusCode: Int?) : GetListPreviewState()
    data class Success(val data: ListPreviewDomain) : GetListPreviewState()
}

sealed class PostRatingState {
    data class Loading(val isLoading: Boolean = false) : PostRatingState()
    data class Error(var error: String = "", var statusCode: Int?) : PostRatingState()
    data class Success(val data: RatingPostDomain) : PostRatingState()
}


sealed class GetDetailAppState {
    data class Loading(val isLoading: Boolean = false) : GetDetailAppState()
    data class Error(var error: String = "", var statusCode: Int?) : GetDetailAppState()
    data class Success(val data: DetailAppDomain) : GetDetailAppState()
}

sealed class GetMyRatingState {
    data class Loading(val isLoading: Boolean = false) : GetMyRatingState()
    data class Error(var error: String = "", var statusCode: Int?) : GetMyRatingState()
    data class Success(val data: MyRatingDomain) : GetMyRatingState()
}


sealed class PostDownloadState {
    data class Loading(val isLoading: Boolean = false) : PostDownloadState()
    data class Error(var error: String = "", var statusCode: Int?) : PostDownloadState()
    data class Success(val data: PostDownloadDomain) : PostDownloadState()
}

sealed class GetListFavoriteState {
    data class Loading(val isLoading: Boolean = false) : GetListFavoriteState()
    data class Error(var error: String = "", var statusCode: Int?) : GetListFavoriteState()
    data class Success(val data: ListFavoriteDomain) : GetListFavoriteState()
}

@HiltViewModel
class DetailGameViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val getListPreviewUseCase: GetListPreviewUseCase,
    private val postRatingUseCase: PostRatingUseCase,
    private val getListAppUseCase: GetListAppUseCase,
    private val getDetailAppUseCase: GetDetailAppUseCase,
    private val getMyRatingUseCase: GetMyRatingUseCase,
    private val postDownloadUseCase: PostDownloadUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val putAddFavoriteUseCase: PutAddFavoriteUseCase,
    private val putRemoveFavoriteUseCase: PutRemoveFavoriteUseCase,
    val preferencesHelper: CachePreferencesHelper,
) : BaseViewModel(contextProvider) {

    private val _getListPreviewState =
        MutableStateFlow<GetListPreviewState>(GetListPreviewState.Loading(false))
    val getListPreviewState = _getListPreviewState.asStateFlow()


    private val _postRatingState =
        MutableStateFlow<PostRatingState>(PostRatingState.Loading(false))
    val postRatingState = _postRatingState.asStateFlow()

    private val _getListAppState =
        MutableStateFlow<GetListAppState>(GetListAppState.Loading(false))
    val getListAppState = _getListAppState.asStateFlow()


    private val _getDetailAppState =
        MutableStateFlow<GetDetailAppState>(GetDetailAppState.Loading(false))
    val getDetailAppState = _getDetailAppState.asStateFlow()

    private val _getMyRatingState =
        MutableStateFlow<GetMyRatingState>(GetMyRatingState.Loading(false))
    val getMyRatingState = _getMyRatingState.asStateFlow()


    private val _postDownloadState =
        MutableStateFlow<PostDownloadState>(PostDownloadState.Loading(false))
    val postDownloadState = _postDownloadState.asStateFlow()


    private val _getProfileState =
        MutableStateFlow<GetProfileState>(GetProfileState.Loading(false))
    val getProfileState = _getProfileState.asStateFlow()


    private val _putListAddFavorite =
        MutableStateFlow<GetListFavoriteState>(GetListFavoriteState.Loading(false))
    val putAddFavorite = _putListAddFavorite.asStateFlow()

    private val _putRemoveListAddFavorite =
        MutableStateFlow<GetListFavoriteState>(GetListFavoriteState.Loading(false))
    val putRemoveFavorite = _putRemoveListAddFavorite.asStateFlow()


    var isCanCallAPIPostDownload = false


    var isCanCallAPIPostRating = false


    fun getListPreview(accessToken: String, apkId: String) {
        launchCoroutineIO {
            getListPreviewUseCase.execute(
                RequestGetListPreview(
                    bearer_token = accessToken,
                    data = RequestGetListPreviewData(
                        pageCurrent = 1,
                        pageSize = 10,
                        noLimit = false,
                        createdAt = -1,
                        apkId = apkId
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getListPreviewState.value = GetListPreviewState.Loading(true)
                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success getListPreview  ${result.data}")
                        result.data?.let {
                            _getListPreviewState.value =
                                GetListPreviewState.Success(result.data)
                        }
                    }

                    is Resource.Error -> {
                        _getListPreviewState.value =
                            GetListPreviewState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }


    fun postRating(accessToken: String, star: Int, description: String, apkId: String) {

        isCanCallAPIPostRating = true
        launchCoroutineIO {
            postRatingUseCase.execute(
                RequestRatingPost(
                    bearer_token = accessToken,
                    data = RequestRatingPostData(
                        star, description, apkId
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _postRatingState.value = PostRatingState.Loading(true)
                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success postRating  ${result.data}")
                        result.data?.let {
                            _postRatingState.value =
                                PostRatingState.Success(result.data)
                        }
                    }

                    is Resource.Error -> {
                        _postRatingState.value =
                            PostRatingState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }


    fun getListApp(accessToken: String) {
        launchCoroutineIO {
            getListAppUseCase.execute(
                RequestGetListApp(
                    bearer_token = accessToken,
                    data = RequestGetListAppData(
                        pageCurrent = 1,
                        pageSize = 10,
                        noLimit = false,
                        createdAt = -1
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
                            _getListAppState.value =
                                GetListAppState.Success(result.data)
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


    fun getDetailApp(accessToken: String, id: String) {
        launchCoroutineIO {
            getDetailAppUseCase.execute(
                RequestDetailApp(
                    bearer_token = accessToken,
                    data = RequestDetailAppData(
                        id = id
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getDetailAppState.value = GetDetailAppState.Loading(true)
                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success DetailAppState ${result.data}")
                        result.data?.let {
                            _getDetailAppState.value =
                                GetDetailAppState.Success(result.data)
                        }
                    }

                    is Resource.Error -> {
                        _getDetailAppState.value =
                            GetDetailAppState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }


    fun getMyRating(accessToken: String, apkId: String) {
        launchCoroutineIO {
            getMyRatingUseCase.execute(
                RequestMyRating(
                    bearer_token = accessToken,
                    data = RequestMyRatingData(
                        apkId = apkId
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getMyRatingState.value = GetMyRatingState.Loading(true)
                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success GetMyRatingState ${result.data}")
                        result.data?.let {
                            _getMyRatingState.value =
                                GetMyRatingState.Success(result.data)
                        }
                    }

                    is Resource.Error -> {
                        _getMyRatingState.value =
                            GetMyRatingState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }


    fun postRatingDownload(accessToken: String, apkId: String) {

        isCanCallAPIPostDownload = true
        launchCoroutineIO {
            postDownloadUseCase.execute(
                RequestPostDownload(
                    bearer_token = accessToken,
                    data = RequestPostDownloadData(
                        apkId = apkId
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _postDownloadState.value = PostDownloadState.Loading(true)
                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success PostDownloadState  ${result.data}")
                        result.data?.let {
                            _postDownloadState.value =
                                PostDownloadState.Success(result.data)
                        }
                    }

                    is Resource.Error -> {
                        _postDownloadState.value =
                            PostDownloadState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }


    fun getProfile(accessToken: String) {
        launchCoroutineIO {
            getProfileUseCase.execute(
                RequestGetProfile(
                    bearer_token = accessToken,
                    data = RequestGetProfileData(
                        deviceId = "string",
                        fcmToken = "string",
                        platform = "string",
                        platformVersion = "string",
                        deviceName = "",
                        ipAddress = "",
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getProfileState.value = GetProfileState.Loading(true)
                    }

                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            preferencesHelper.pointUser = it.point
                            preferencesHelper.nameUser = it.fullName
                            preferencesHelper.avatarUser = it.avatar
                            preferencesHelper.codeUser = it.userId
                            _getProfileState.value =
                                GetProfileState.Success(result.data)
                        }
                    }

                    is Resource.Error -> {
                        _getProfileState.value =
                            GetProfileState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun addFavoriteApp(accessToken : String, apkId: String) {
        launchCoroutineIO {
            putAddFavoriteUseCase.execute(
                RequestPutListFavorite(
                        bearer_token = accessToken,
                        data = RequestPutListFavoriteData(
                            apkId = apkId
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _putListAddFavorite.value = GetListFavoriteState.Loading(true)
                    }
                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            _putListAddFavorite.value = GetListFavoriteState.Success(result.data)
                        }

                    }
                    is Resource.Error -> {
                        _putListAddFavorite.value = GetListFavoriteState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun removeFavoriteApp(accessToken : String, apkId: String) {
        launchCoroutineIO {
            putRemoveFavoriteUseCase.execute(
                RequestPutListFavorite(
                    bearer_token = accessToken,
                    data = RequestPutListFavoriteData(
                        apkId = apkId
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _putRemoveListAddFavorite.value = GetListFavoriteState.Loading(true)
                    }
                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            _putRemoveListAddFavorite.value = GetListFavoriteState.Success(result.data)
                        }

                    }
                    is Resource.Error -> {

                        _putRemoveListAddFavorite.value = GetListFavoriteState.Error(result.message, result.statusCode)

                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}