package com.pools.store.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel :ViewModel() {
    var isProfileChange = MutableLiveData<Boolean>()
    var isPointChange = MutableLiveData<Boolean>()
}