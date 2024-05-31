package com.activiza.activiza.domain

import com.activiza.activiza.core.Constants
import com.activiza.activiza.data.AuthData
import com.activiza.activiza.data.DetallesUsuarioData
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.Message
import com.activiza.activiza.data.PexelsResponse
import com.activiza.activiza.data.Photo
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.data.RutinaPostData
import com.activiza.activiza.data.TokenResponse
import com.activiza.activiza.data.UsuarioData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIListener {
    companion object{
        const val tokenRegister = "d4eaf059188e9ab480b125344186d06d9ce920de"
        const val TOKEN_API_PEXELS = "tkGjeRqPu7jbvTV1X9TMchp1URNxJ0JiIsqxbqBqduu42YO2qM6HqSc2"
    }

    @GET("ejercicio/{ejercicio}")
    fun getEjercicio(@Path("ejercicio") ejercicio:String): Call<EjerciciosData>

    @GET("ejercicio")
    fun getTodosEjercicios(@Header("Authorization") token: String): Call<List<EjerciciosData>>

    @GET("rutina")
    fun getTodasRutinas(@Header("Authorization") token: String): Call<List<RutinaData>>
    @GET("rutina")
    fun getRutinasFiltradas(@Header("Authorization") token: String, @Query("genero") genero:String, @Query("lugar_entrenamiento") lugar_entrenamiento:String, @Query("objetivo") objetivo:String): Call<List<RutinaData>>
    @GET("rutina/{rutina}")
    fun getRutina(@Path("rutina") rutina:Int): Call<RutinaData>

    @DELETE("rutina/{rutina}")
    fun deleteRutina(@Path("rutina") rutina:Int): Call<RutinaData>

    @POST("api-token-auth/")

    fun authenticate(@Body authData: AuthData): Call<TokenResponse>

    @Headers("Authorization: Token $tokenRegister")
    @POST("api/user/")
    fun registerUser(@Body authData: AuthData): Call<TokenResponse>

    @Headers("Authorization: Token {new_token}")
    @POST("api/cliente/")
    fun updateClientData(@Body detallesUsuarioData: DetallesUsuarioData): Call<TokenResponse>

    @POST("rutina/")
    fun postRutina(@Header("Authorization") token: String, @Body rutinaPostData: RutinaPostData): Call<RutinaData>

    @GET("search")
    fun getTodasImagenes(@Header("Authorization") token : String, @Query("query") query: String): Call<PexelsResponse>

    @GET("api/publicaciones/")
    suspend fun getMessages(@Header("Authorization") token : String): List<Message>

    @POST("api/publicaciones/")
    suspend fun sendMessage(@Header("Authorization") token: String,@Body message: Message): Response<Message>

    @GET("generate-qr-code/")
    suspend fun getQrCode(@Header("Authorization") token : String): ResponseBody
}