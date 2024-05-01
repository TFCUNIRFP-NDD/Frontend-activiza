package com.activiza.activiza.ui.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.activiza.activiza.data.RegisterRequest
import com.activiza.activiza.data.RegisterResponse
import com.activiza.activiza.data.SessionManager
import com.activiza.activiza.databinding.ActivityRegisterBinding
import com.activiza.activiza.domain.ApiClient
import com.activiza.activiza.ui.view.OnboardingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        //Funcionalidad al clickar en las cajas de email, password y nombre
        binding.etEmailSignup.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmailFormat()
            }
        }

        binding.etNameSignup.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                usernameEmpty()
            }
        }


        binding.etPasswordSignup.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.tiPasswordSignup.setHelperTextEnabled(false)

                validatePasswordFormat()
            }else{
                binding.tiPasswordSignup.setHelperTextEnabled(true)
                binding.tiPasswordSignup.helperText = "La contraseña debe tener al menos: " +
                        "8 caracteres de longitud, una letra mayúscula,una letra minúscula,un número y un carácter especial como @, #,$,/ etc."
            }
        }

        ///Funcionalidad boton Register
        binding.btnRegister.setOnClickListener {
            val name = binding.etNameSignup.text.toString()
            val email = binding.etEmailSignup.text.toString()
            val password = binding.etPasswordSignup.text.toString()
            val validEmailFormat = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val validPasswordFormat = "^.*(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=,!]).*$".toRegex()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                if (validEmailFormat && validPasswordFormat.matches(password)) {
                    val registerRequest = RegisterRequest(name, email, password)
                    apiClient.getApiServiceRegister().register(registerRequest)
                        .enqueue(object : Callback<RegisterResponse> {
                            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                                // Manejar el error al registrar
                                Toast.makeText(applicationContext, "Error al registrar", Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                                val registerResponse = response.body()

                                // Verificar si la respuesta es exitosa y guardar el token de autenticación
                                if (registerResponse != null && registerResponse.statusCode == 200) {
                                    sessionManager.saveAuthToken(registerResponse.token)

                                    // Registro exitoso, redirigir a la actividad principal
                                    val intent = Intent(this@RegisterActivity, OnboardingActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // Error al registrar
                                    Toast.makeText(applicationContext, "Error al registrar: ${registerResponse?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                } else {
                    if (!validEmailFormat) {
                        Toast.makeText(this, "Por favor, introduce un email válido", Toast.LENGTH_SHORT).show()
                    }
                    if (!validPasswordFormat.matches(password)) {
                        Toast.makeText(this, "Por favor, introduce una contraseña válida", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }



    }

    //Valida si el formato de email es correcto
    private fun validateEmailFormat(): Boolean {
        val validEmailFormat =
            Patterns.EMAIL_ADDRESS.matcher(binding.etEmailSignup.text.toString()).matches()
        if (!validEmailFormat) {
            binding.tiEmailSignup.error = "Por favor, introduce un email válido"
        } else {
            binding.tiEmailSignup.error = null
        }
        return validEmailFormat
    }


    //Valida si el formato de la password es correcto
    private fun validatePasswordFormat(): Boolean {
        val validPasswordFormat = "^.*(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=,!]).*$".toRegex()
        val validPassword = validPasswordFormat.matches(binding.etPasswordSignup.text.toString())
        //val validPassword = validPasswordFormat.find(binding.etPasswordSignup.text.toString()) != null

        if (!validPassword) {
            binding.tiPasswordSignup.error = "Formato no válido"
            binding.tiPasswordSignup.setHelperTextEnabled(false)

        } else {
            binding.tiPasswordSignup.error = null


        }
        return validPassword
    }

    //Comprueba si el campo de nombre está vacío
    private fun usernameEmpty(): Boolean {
        var usernameEmpty = true
        if (binding.etNameSignup.text.toString().isNotEmpty()){
            binding.tiName.error = null
            usernameEmpty = false
        } else {
            binding.tiName.error = "Este campo no puede estar vacío"
            usernameEmpty = true
        }
        return usernameEmpty
    }

}

