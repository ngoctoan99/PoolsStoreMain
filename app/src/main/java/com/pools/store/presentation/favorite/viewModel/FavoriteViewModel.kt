package com.pools.store.presentation.favorite.viewModel

import androidx.lifecycle.viewModelScope
import com.pools.store.base.BaseViewModel
import com.pools.store.data.remote.MyApi
import com.pools.store.data.request.RequestGetListFavorite
import com.pools.store.data.usercase.GetListFavoriteUseCase
import com.pools.store.domain.model.ListFavoriteDomain
import com.pools.store.utils.CoroutineContextProvider
import com.pools.store.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject


sealed class GetListFavoriteAppState {
    data class Loading(val isLoading: Boolean = false) : GetListFavoriteAppState()
    data class Error(var error: String = "", var statusCode: Int?) : GetListFavoriteAppState()
    data class Success(val data: ListFavoriteDomain) : GetListFavoriteAppState()
}
@HiltViewModel
class FavoriteViewModel  @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val getListFavoriteAppUseCase: GetListFavoriteUseCase,
    private val myApi: MyApi
): BaseViewModel(contextProvider) {
    private val _getListAppState = MutableStateFlow<GetListFavoriteAppState>(  GetListFavoriteAppState.Loading(false))
    val  getListFavoriteAppState = _getListAppState.asStateFlow()


    fun getListFavoriteApp(accessToken : String) {
        launchCoroutineIO {
            getListFavoriteAppUseCase.execute(
                RequestGetListFavorite(accessToken)
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getListAppState.value = GetListFavoriteAppState.Loading(true)
                    }
                    is Resource.Success -> {

                        Timber.i("Resource.Success category ${result.data}")
                        result.data?.let {
                            _getListAppState.value = GetListFavoriteAppState.Success(result.data)
                        }

                    }
                    is Resource.Error -> {

                        _getListAppState.value = GetListFavoriteAppState.Error(result.message, result.statusCode)

                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}