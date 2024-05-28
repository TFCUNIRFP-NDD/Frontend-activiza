package com.activiza.activiza.data

import com.google.gson.annotations.SerializedName

data class AuthData(
    val username: String,
    val password: String
)

data class LoginRequest(
    @SerializedName("email")
    val email:String,
    @SerializedName("password")
    val password:String
)

data class LoginResponse(
    @SerializedName("status_code")
    var statusCode: Int,
    @SerializedName("auth_token")
    var authToken: String,
    @SerializedName("user")
    var user: User
)

data class RegisterRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class RegisterResponse(
    @SerializedName("token")
    var token: String,
    @SerializedName("status_code")
    var statusCode: Int
)