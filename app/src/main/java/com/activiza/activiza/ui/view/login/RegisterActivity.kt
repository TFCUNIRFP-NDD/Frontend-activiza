package com.activiza.activiza.ui.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MotionEvent
import android.widget.Toast
import com.activiza.activiza.R
import com.activiza.activiza.databinding.ActivityRegisterBinding
import com.activiza.activiza.ui.view.OnboardingActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



        ////Funcionalidad al clickar en las cajas de email, password y nombre
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

        //Funcionalidad boton Login
        binding.btnRegister.setOnClickListener {
            val nameNotEmpty = binding.etNameSignup.text.toString().isNotEmpty()
            val emailNotEmpty = binding.etEmailSignup.text.toString().isNotEmpty()
            val passwordNotEmpty = binding.etPasswordSignup.text.toString().isNotEmpty()
            val validEmailFormat =
                Patterns.EMAIL_ADDRESS.matcher(binding.etEmailSignup.text.toString()).matches()
            val validPasswordFormat = "^.*(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=,!]).*$".toRegex()



            when {
                nameNotEmpty && emailNotEmpty && passwordNotEmpty -> {
                    if (validEmailFormat && validPasswordFormat.matches(binding.etPasswordSignup.text.toString())) {
                        intent = Intent(this, OnboardingActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        if (!validEmailFormat) {
                            Toast.makeText(
                                this,
                                "Por favor, introduce un email válido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        if (!validPasswordFormat.matches(binding.etPasswordSignup.text.toString())) {
                            Toast.makeText(
                                this,
                                "Por favor, introduce una contraseña válida",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                else -> {
                    Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT)
                        .show()
                }
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

