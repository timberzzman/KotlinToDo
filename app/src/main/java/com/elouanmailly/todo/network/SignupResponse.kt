package com.elouanmailly.todo.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    @SerialName("token")
    val token: String,
)
