package com.activiza.activiza.ui.view.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.activiza.activiza.R
import com.activiza.activiza.data.AuthData
import com.activiza.activiza.data.TokenResponse
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.domain.APIListener
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.view.OnboardingActivity
import com.activiza.activiza.ui.view.login.LoginActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    lateinit var db: ActivizaDataBaseHelper
    private lateinit var apiService: APIListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        db = ActivizaDataBaseHelper(this)

        // Configuración de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.163.215.184/activiza/") // Reemplaza con la URL base de tu API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(APIListener::class.java)

        comprobarDatos()
    }

    private fun comprobarDatos() {
        val user: UsuarioData? = db.getUsuario()
        if (user != null) {
            val authData = AuthData(
                user.nombre,
                user.password
            )
            authenticateUser(authData)
        } else {
            // Si no hay usuario registrado, redirige a la LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun authenticateUser(authData: AuthData) {
        apiService.authenticate(authData).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    tokenResponse?.token?.let { token ->
                        // Guardamos el token en SharedPreferences
                        val editor = getSharedPreferences("login", Context.MODE_PRIVATE).edit()
                        editor.putString("token", token)
                        editor.apply()

                        // Navegamos a la siguiente actividad
                        val intent = Intent(this@SplashActivity, OnboardingActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Error de autenticación
                    Log.d("Error","No autentificado")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                // Error de red
                Log.d("Error","No hay conexion")
            }
        })
    }
}