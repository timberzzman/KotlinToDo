package com.elouanmailly.todo.network

import kotlinx.serialization.SerialName

data class LoginResponse(
    @SerialName("token")
    val email: String,
)
