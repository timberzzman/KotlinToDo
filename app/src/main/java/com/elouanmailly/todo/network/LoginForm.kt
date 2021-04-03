package com.elouanmailly.todo.network

import kotlinx.serialization.SerialName

data class LoginForm(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val firstName: String,
)
