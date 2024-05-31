package com.activiza.activiza.data

import com.google.gson.annotations.SerializedName

data class RutinaData(
    @SerializedName("id") var id:Int,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("descripcion") var descripcion:String,
    @SerializedName("entrenador") var entrenador:String,
    @SerializedName("ejercicios") var ejercicios:List<EjerciciosData>,
    @SerializedName("media") var media:String,
    @SerializedName("duracion") var duracion:Int,
    @SerializedName("genero") var genero:String?,
    @SerializedName("lugar_entrenamiento") var lugar_entrenamiento:String?,
    @SerializedName("objetivo") var objetivo:String?
)
