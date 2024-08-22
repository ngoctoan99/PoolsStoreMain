package com.pools.store.presentation.main

import androidx.lifecycle.viewModelScope
import com.pools.store.base.BaseViewModel
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.data.request.RequestGetLanguage
import com.pools.store.data.request.RequestGetLanguageData
import com.pools.store.data.request.RequestGetProfile
import com.pools.store.data.request.RequestGetProfileData
import com.pools.store.data.request.RequestPostUser
import com.pools.store.data.request.RequestPostUserData
import com.pools.store.data.usercase.GetLanguageUseCase
import com.pools.store.data.usercase.GetProfileUseCase
import com.pools.store.data.usercase.PostUserUseCase
import com.pools.store.domain.model.LanguageDomain
import com.pools.store.domain.model.PostUserDomain
import com.pools.store.domain.model.ProfileDomain

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

sealed class PostUserState {
    data class Loading(val isLoading: Boolean = false) : PostUserState()
    data class Error(var error: String = "", var statusCode: Int?) : PostUserState()
    data class Success(val data: PostUserDomain) : PostUserState()
}


sealed class GetLanguageState {
    data class Loading(val isLoading: Boolean = false) : GetLanguageState()
    data class Error(var error: String = "", var statusCode: Int?) : GetLanguageState()
    data class Success(val data: LanguageDomain) : GetLanguageState()
}
@HiltViewModel
class MainViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val getProfileUseCase: GetProfileUseCase,
    private val postUserUseCase: PostUserUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    val preferencesHelper: CachePreferencesHelper,
    ) : BaseViewModel(contextProvider) {
    private val _getProfileState =
        MutableStateFlow<GetProfileState>(GetProfileState.Loading(false))
    val getProfileState = _getProfileState.asStateFlow()

    private val _postUserState =
        MutableStateFlow<PostUserState>(PostUserState.Loading(false))
    val postUserState = _postUserState.asStateFlow()

    private val _getLanguageState =
        MutableStateFlow<GetLanguageState>(GetLanguageState.Loading(false))
    val getLanguageState = _getLanguageState.asStateFlow()

    fun getProfile(accessToken : String) {
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


    fun postUser(accessToken : String , fcmToken: String) {
        launchCoroutineIO {
            postUserUseCase.execute(
                RequestPostUser(
                    bearer_token = accessToken,
                    data = RequestPostUserData(
                        fcmToken = fcmToken,
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _postUserState.value = PostUserState.Loading(true)
                    }
                    is Resource.Success -> {

                        Timber.i("Resource.Success PostUserState ${result.data}")
                        result.data?.let {

                            _postUserState.value =
                                PostUserState.Success(result.data)
                        }
                    }
                    is Resource.Error -> {
                        _postUserState.value =
                            PostUserState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }


    fun getLanguage(accessToken: String, language: String){
        launchCoroutineIO {
            getLanguageUseCase.execute(
                RequestGetLanguage(
                    bearer_token = accessToken,
                    data = RequestGetLanguageData(
                      language = language
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getLanguageState.value = GetLanguageState.Loading(true)
                    }
                    is Resource.Success -> {

                        Timber.i("Resource.Success getLanguage ${result.data}")
                        result.data?.let {
                            _getLanguageState.value =
                                GetLanguageState.Success(result.data)
                        }
                    }
                    is Resource.Error -> {
                        _getLanguageState.value =
                            GetLanguageState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}