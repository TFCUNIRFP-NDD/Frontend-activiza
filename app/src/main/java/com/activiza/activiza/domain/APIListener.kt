package com.activiza.activiza.domain

import com.activiza.activiza.core.Constants
import com.activiza.activiza.data.AuthData
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.LoginRequest
import com.activiza.activiza.data.LoginResponse
import com.activiza.activiza.data.RegisterRequest
import com.activiza.activiza.data.RegisterResponse
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.data.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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
    fun getRutina(@Path("rutina") rutina:Int): Call<RutinaData>

    @POST("api-token-auth/")

    fun authenticate(@Body authData: AuthData): Call<TokenResponse>
}

interface ApiService {
    @POST(Constants.LOGIN_URL)
    @FormUrlEncoded
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}

interface ApiServiceRegister {
    @POST(Constants.REGISTER_URL)
    @FormUrlEncoded
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>


}