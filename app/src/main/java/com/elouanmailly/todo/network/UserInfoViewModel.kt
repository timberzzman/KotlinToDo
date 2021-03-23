package com.elouanmailly.todo.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel: ViewModel() {
    private val repository = UserInfoRepository()
    private val _userinfo = MutableLiveData<UserInfo>()
    val userinfo : LiveData<UserInfo> = _userinfo

    fun loadInfo() {
        viewModelScope.launch {
            val info = repository.refresh()
            if (info != null) {
                _userinfo.postValue(info!!)
            }
        }
    }

    fun updateAvatar(image: MultipartBody.Part) {
        viewModelScope.launch {
            val user = repository.updateAvatar(image)
            if (user != null) {
                _userinfo.postValue(user!!)
            }
        }
    }

    fun updateInfo(info: UserInfo) {
        viewModelScope.launch {
            val user = repository.update(info)
            if (user != null) {
                _userinfo.postValue(user!!)
            }
        }
    }
}