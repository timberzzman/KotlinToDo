package com.elouanmailly.todo.network

import okhttp3.MultipartBody

class UserInfoRepository {
    private val userWebService = Api.userService

    suspend fun refresh(): UserInfo? {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun updateAvatar(image: MultipartBody.Part): UserInfo? {
        val response = userWebService.updateAvatar(image)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun update(info: UserInfo): UserInfo? {
        val response = userWebService.update(info)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}

