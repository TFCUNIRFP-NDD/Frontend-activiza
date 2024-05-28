package com.activiza.activiza.data

import com.google.gson.annotations.SerializedName

data class RutinaPostData (
    @SerializedName("id") var id:Int,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("descripcion") var descripcion:String,
    @SerializedName("ejercicios") var ejercicios:List<Int>,
    @SerializedName("genero") var genero:String,
    @SerializedName("objetivo") var objetivo:String,
    @SerializedName("lugar_entrenamiento") var lugar_entrenamiento:String,
    @SerializedName("media") var media:String,
    @SerializedName("duracion") var duracion:Int
)