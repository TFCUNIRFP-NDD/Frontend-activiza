package com.activiza.activiza.data

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id") val id: Int,
    @SerializedName("autor") val autor: String,
    @SerializedName("titulo") val titulo: String?,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("media") val media: String?
)
