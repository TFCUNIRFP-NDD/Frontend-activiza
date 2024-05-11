package com.activiza.activiza.domain

import com.activiza.activiza.data.AuthData
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.PexelsResponse
import com.activiza.activiza.data.Photo
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.data.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIListener {

    @GET("ejercicio/{ejercicio}")
    fun getEjercicio(@Path("ejercicio") ejercicio:String): Call<EjerciciosData>

    @GET("ejercicio")
    fun getTodosEjercicios(): Call<List<EjerciciosData>>

    @GET("rutina")
    fun getTodasRutinas(@Header("Authorization") token: String): Call<List<RutinaData>>

    @GET("rutina/{rutina}")
    fun getRutina(@Path("rutina") rutina:Int): Call<RutinaData>

    @POST("api-token-auth/")
    fun authenticate(@Body authData: AuthData): Call<TokenResponse>

    @GET("search")
    fun getTodasImagenes(@Header("Authorization") token : String, @Query("query") query: String): Call<PexelsResponse>
}