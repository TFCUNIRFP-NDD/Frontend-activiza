package com.activiza.activiza.ui.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.activiza.activiza.R
import com.activiza.activiza.databinding.ActivityLoginBinding
import com.activiza.activiza.ui.view.HomeActivity
import com.activiza.activiza.ui.view.OnboardingActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Funcionalidad al clickar en las cajas de email y password
        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if(validateEmailFormat()){
                    val intent = Intent(this, OnboardingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }

        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {

            }
        }

        //Funcionalidad boton Login
        binding.btnLogin.setOnClickListener {
            val emailNotEmpty = binding.etEmail.text.toString().isNotEmpty()
            val validEmailFormat = Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()
            val passwordNotEmpty = binding.etPassword.text.toString().isNotEmpty()

            when {
                emailNotEmpty && validEmailFormat && passwordNotEmpty -> {
                    val intent = Intent(this, OnboardingActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.tvRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
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