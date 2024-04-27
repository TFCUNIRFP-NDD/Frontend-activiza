package com.activiza.activiza.data

import com.google.gson.annotations.SerializedName

data class RutinaData(
    @SerializedName("id") var id:Int,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("tipo") var tipo:String,
    @SerializedName("descripcion") var descripcion:String,
    //@SerializedName("entrenador") var entrenador:Int,
    @SerializedName("ejercicios") var ejercicios:List<EjerciciosData>
)
