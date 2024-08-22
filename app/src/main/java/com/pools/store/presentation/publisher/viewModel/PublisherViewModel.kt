package com.pools.store.presentation.publisher.viewModel

import androidx.lifecycle.viewModelScope
import com.pools.store.base.BaseViewModel
import com.pools.store.data.remote.MyApi
import com.pools.store.data.request.RequestDetailPublisher
import com.pools.store.data.request.RequestDetailPublisherData
import com.pools.store.data.usercase.GetDetailPublisherUseCase
import com.pools.store.domain.model.DetailPublisherDomain
import com.pools.store.utils.CoroutineContextProvider
import com.pools.store.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

sealed class GetDetailPublisherState {
    data class Loading(val isLoading: Boolean = false) : GetDetailPublisherState()
    data class Error(var error: String = "", var statusCode: Int?) : GetDetailPublisherState()
    data class Success(val data: DetailPublisherDomain) : GetDetailPublisherState()
}
@HiltViewModel
class PublisherViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val getDetailPublisherUseCase: GetDetailPublisherUseCase,
    private val myApi: MyApi
) : BaseViewModel(contextProvider) {


    private val _getDetailPublisherState =
        MutableStateFlow<GetDetailPublisherState>(GetDetailPublisherState.Loading(false))
    val getDetailPublisherState = _getDetailPublisherState.asStateFlow()


    fun getDetailPublisher(accessToken : String, id: String) {
        launchCoroutineIO {
            getDetailPublisherUseCase.execute(
                RequestDetailPublisher(
                    bearer_token = accessToken,
                    data = RequestDetailPublisherData(
                        id = id
                    )
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getDetailPublisherState.value = GetDetailPublisherState.Loading(true)
                    }
                    is Resource.Success -> {

                        Timber.i("Resource.Success GetDetailPublisherState ${result.data}")
                        result.data?.let {
                            _getDetailPublisherState.value =
                                GetDetailPublisherState.Success(result.data)
                        }
                    }
                    is Resource.Error -> {
                        _getDetailPublisherState.value =
                            GetDetailPublisherState.Error(result.message, result.statusCode)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}
