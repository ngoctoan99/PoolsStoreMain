package com.pools.store.presentation.login.viewModel

import androidx.lifecycle.viewModelScope
import com.pools.store.base.BaseViewModel
import com.pools.store.data.request.RequestGetLanguage
import com.pools.store.data.request.RequestGetLanguageData
import com.pools.store.data.usercase.GetLanguageUseCase
import com.pools.store.presentation.main.GetLanguageState
import com.pools.store.utils.CoroutineContextProvider
import com.pools.store.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val getLanguageUseCase: GetLanguageUseCase,) : BaseViewModel(contextProvider) {

    private val _getLanguageState =
        MutableStateFlow<GetLanguageState>(GetLanguageState.Loading(false))
    val getLanguageState = _getLanguageState.asStateFlow()


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