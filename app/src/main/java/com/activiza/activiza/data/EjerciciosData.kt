package com.activiza.activiza.data

import com.google.gson.annotations.SerializedName

data class EjerciciosData(
    @SerializedName("id") var id:Int,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("descripcion") var descripcion:String,
    @SerializedName("repeticiones") var repeticiones:Int,
    @SerializedName("duracion") var duracion:Int?,
    @SerializedName("descanso") var descanso:Int,
    @SerializedName("media") var media:String
)
