package com.pools.store.presentation.profile.viewModel

import androidx.lifecycle.viewModelScope
import com.pools.store.base.BaseViewModel
import com.pools.store.data.request.RequestGetProfile
import com.pools.store.data.request.RequestGetProfileData
import com.pools.store.data.usercase.GetProfileUseCase
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

@HiltViewModel
class ProfileViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
) : BaseViewModel(contextProvider) {



}