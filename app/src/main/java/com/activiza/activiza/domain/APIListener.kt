package com.activiza.activiza.domain

import com.activiza.activiza.data.AuthData
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.data.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface APIListener {

    @GET("ejercicio/{ejercicio}")
    fun getEjercicio(@Path("ejercicio") ejercicio:String): Call<EjerciciosData>

    @GET("ejercicio")
    fun getTodosEjercicios(): Call<List<EjerciciosData>>

    @GET("rutina")
    fun getTodasRutinas(): Call<List<RutinaData>>

    @GET("rutina/{rutina}")
    fun getRutina(@Path("rutina") rutina:String): Call<RutinaData>

    @POST("api-token-auth/")
    fun authenticate(@Body authData: AuthData): Call<TokenResponse>
}