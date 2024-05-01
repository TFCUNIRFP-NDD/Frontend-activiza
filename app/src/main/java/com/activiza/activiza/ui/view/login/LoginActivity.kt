package com.activiza.activiza.ui.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.activiza.activiza.data.LoginRequest
import com.activiza.activiza.data.LoginResponse
import com.activiza.activiza.data.SessionManager
import com.activiza.activiza.databinding.ActivityLoginBinding
import com.activiza.activiza.domain.ApiClient
import com.activiza.activiza.ui.view.OnboardingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        //Funcionalidad al clickar en las cajas de email y password
        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmailFormat()
            }
        }

        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {

            }
        }

        //Funcionalidad boton Login
        binding.btnLogin.setOnClickListener {
            val validEmailFormat = Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && validEmailFormat && password.isNotEmpty()) {
                apiClient.getApiServiceLogin().login(LoginRequest(email, password))
                    .enqueue(object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            // Manejar el error al iniciar sesión
                            Toast.makeText(applicationContext, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            val loginResponse = response.body()

                            // Verificar si la respuesta es exitosa y guardar el token de autenticación
                            if (loginResponse != null && loginResponse.statusCode == 200) {
                                sessionManager.saveAuthToken(loginResponse.authToken)

                                // Continuar con la lógica de la aplicación, por ejemplo, navegar a otra actividad
                                val intent = Intent(this@LoginActivity, OnboardingActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(applicationContext, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            } else {
                Toast.makeText(applicationContext, "Por favor, rellena los campos de email y contraseña", Toast.LENGTH_SHORT).show()
            }
        }


        binding.tvRegister.setOnClickListener {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }

    //Valida si el formato de email es correcto
    private fun validateEmailFormat(): Boolean {
        val validEmailFormat =
            Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()
        if (!validEmailFormat) {
            binding.tiEmail.error = "Por favor, introduce un email válido"
        } else {
            binding.tiEmail.error = null
        }
        return validEmailFormat
    }

    //Falta añadir funciones para comprobar si el email y la contraseña existen
}