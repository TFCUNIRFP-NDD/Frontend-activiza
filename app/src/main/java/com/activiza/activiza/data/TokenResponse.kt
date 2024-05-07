package com.activiza.activiza.data

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token") var token:String,
    @SerializedName("entrenador") var entrenador:Boolean
)
