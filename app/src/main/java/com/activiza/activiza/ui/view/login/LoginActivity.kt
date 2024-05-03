package com.activiza.activiza.ui.view.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import com.activiza.activiza.data.AuthData
import com.activiza.activiza.data.TokenResponse
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.databinding.ActivityLoginBinding
import com.activiza.activiza.domain.APIListener
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.view.OnboardingActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiService: APIListener // Asegúrate de tener la interfaz APIService definida en tu código
    private lateinit var sharedPreferences:SharedPreferences
    lateinit var db: ActivizaDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        db = ActivizaDataBaseHelper(binding.tvRegister.context)

        // Configuración de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.163.215.184/activiza/") // Reemplaza con la URL base de tu API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(APIListener::class.java)

        // Funcionalidad al hacer clic en el botón Login
        binding.btnLogin.setOnClickListener {
            val emailNotEmpty = binding.etEmail.text.toString().isNotEmpty()
            val passwordNotEmpty = binding.etPassword.text.toString().isNotEmpty()

            when {
                emailNotEmpty && passwordNotEmpty -> {
                    // Si todos los campos están llenos y el formato de correo electrónico es válido, intentamos autenticar al usuario
                    val authData = AuthData(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                    authenticateUser(authData)
                }
                else -> {
                    // Si falta algún campo o el formato de correo electrónico no es válido, mostramos un mensaje de error
                    Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Funcionalidad al hacer clic en el enlace para registrarse
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun authenticateUser(authData: AuthData) {
        // Realizamos la llamada a la API para autenticar al usuario
        apiService.authenticate(authData).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    // Si la autenticación es exitosa, podemos manejar la respuesta aquí
                    val tokenResponse = response.body()
                    // Por ejemplo, podemos guardar el token en SharedPreferences y navegar a la siguiente actividad
                    tokenResponse?.token?.let { token ->
                        // Guardamos el token (aquí implementamos lógica de la bbdd sustituir usuario)
                        var usuarioData = UsuarioData(
                            token,
                            authData.username,
                            authData.password
                        )
                        var usuarioDataComprobacion = db.getUsuario()

                        if(usuarioDataComprobacion?.nombre != null) {
                            if (usuarioDataComprobacion?.password != usuarioData.password && usuarioData.nombre != usuarioDataComprobacion?.nombre) {
                                db.updateUsuario(usuarioData)
                            }
                        }else{
                            db.insertUsuario(usuarioData)
                        }
                        //recogemos el token de la base de datos
                        val editor = sharedPreferences.edit()
                        editor.putString("token", intent.getStringExtra(token))
                        // Luego, navegamos a la siguiente actividad
                        val intent = Intent(this@LoginActivity, OnboardingActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Si la autenticación falla, mostramos un mensaje de error
                    Toast.makeText(this@LoginActivity, "Error de autenticación", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                // Si ocurre un error en la llamada a la API, mostramos un mensaje de error
                Toast.makeText(this@LoginActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Aquí puedes agregar más métodos según sea necesario
}