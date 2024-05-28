package com.activiza.activiza.data

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id") val id: Int,
    @SerializedName("autor") var autor: String,
    @SerializedName("titulo") var titulo: String?,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("media") var media: String?
)
