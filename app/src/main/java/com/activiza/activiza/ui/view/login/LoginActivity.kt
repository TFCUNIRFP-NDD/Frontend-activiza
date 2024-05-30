package com.activiza.activiza.ui.view.login


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.activiza.activiza.R
import com.activiza.activiza.data.AuthData
import com.activiza.activiza.data.DetallesUsuarioData
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
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var db: ActivizaDataBaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        db = ActivizaDataBaseHelper(binding.layoutRegister.context)

        //Cambio de vista de layout LOGIN/REGISTER
        binding.tvTitleSignup.visibility = View.GONE
        binding.tvSignIn.setOnClickListener {
            showLoginLayout()
        }

        binding.tvSignUp.setOnClickListener {
            showRegisterLayout()
        }




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
                    Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


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
            } else {
                binding.tiPasswordSignup.setHelperTextEnabled(true)
                binding.tiPasswordSignup.helperText = "La contraseña debe tener al menos: " +
                        "8 caracteres de longitud, una letra mayúscula,una letra minúscula,un número y un carácter especial como @, #,$,/ etc."
            }
        }

        //Funcionalidad boton SignUp
        binding.btnRegister.setOnClickListener {
            val nameNotEmpty = binding.etNameSignup.text.toString().isNotEmpty()
            val emailNotEmpty = binding.etEmailSignup.text.toString().isNotEmpty()
            val passwordNotEmpty = binding.etPasswordSignup.text.toString().isNotEmpty()
            val validEmailFormat =
                Patterns.EMAIL_ADDRESS.matcher(binding.etEmailSignup.text.toString()).matches()
            val validPasswordFormat =
                "^.*(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=,!.?]).*$".toRegex()

            when {
                nameNotEmpty && emailNotEmpty && passwordNotEmpty -> {
                    if (validEmailFormat && validPasswordFormat.matches(binding.etPasswordSignup.text.toString())) {
                        val authData = AuthData(
                            binding.etEmailSignup.text.toString(),
                            binding.etPasswordSignup.text.toString()
                            // Suponemos que el usuario no es entrenador por defecto
                        )
                        registerUser(authData)
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

        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.layoutLogin.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.dark_blue
                    )
                )
                binding.layoutRegister.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.dark_blue
                    )
                )
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                binding.layoutLogin.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )

                binding.layoutRegister.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
            }
        }
    }

    // ----- FUNCIÓN DE LOGIN ------

    fun authenticateUser(authData: AuthData) {
        // Realizamos la llamada a la API para autenticar al usuario
        apiService.authenticate(authData).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    // Si la autenticación es exitosa, podemos manejar la respuesta aquí
                    val tokenResponse = response.body()
                    // Por ejemplo, podemos guardar el token en SharedPreferences y navegar a la siguiente actividad
                    tokenResponse?.let { TokenResponse ->
                        // Guardamos el token (aquí implementamos lógica de la bbdd sustituir usuario)
                        var usuarioData = UsuarioData(
                            TokenResponse.token,
                            authData.username,
                            authData.password,
                            TokenResponse.entrenador
                        )
                        var usuarioDataComprobacion = db.getUsuario()

                        if (usuarioDataComprobacion?.nombre != null) {
                            if (usuarioDataComprobacion?.password != usuarioData.password && usuarioData.nombre != usuarioDataComprobacion?.nombre) {
                                db.updateUsuario(usuarioData)
                            }
                        } else {
                            db.insertUsuario(usuarioData)
                        }
                        //recogemos el token de la base de datos
                        val editor = sharedPreferences.edit()
                        editor.putString("token", intent.getStringExtra(TokenResponse.token))
                        editor.apply()

                        // Luego, navegamos a la siguiente actividad
                        val intent = Intent(this@LoginActivity, OnboardingActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Si la autenticación falla, mostramos un mensaje de error
                    Log.e("david", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                    Toast.makeText(this@LoginActivity, "Error de autenticación", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                // Si ocurre un error en la llamada a la API, mostramos un mensaje de error
                Toast.makeText(this@LoginActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    //----- FUNCIONES DE REGISTRO -------

    fun registerUser(authData: AuthData) {
        // Realizamos la llamada a la API para registrar al usuario
        apiService.registerUser(authData).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    // Si el registro es exitoso, manejamos la respuesta aquí
                    val tokenResponse = response.body()
                    tokenResponse?.let { tokenResp ->
                        // Creamos un objeto UsuarioData con el token y los datos de autenticación
                        val usuarioData = UsuarioData(
                            tokenResp.token,
                            authData.username,
                            authData.password,
                            tokenResp.entrenador
                        )
                        // Insertamos o actualizamos el usuario en la base de datos
                        db.insertUsuario(usuarioData)

                        // Guardamos el token en SharedPreferences
                        val editor = sharedPreferences.edit()
                        editor.putString("token", intent.getStringExtra(tokenResp.token))
                        //editor.putString("token", tokenResp.token)
                        editor.apply()


                        // Navegamos a la siguiente actividad
                        val intent = Intent(this@LoginActivity, OnboardingActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Si el registro falla, mostramos un mensaje de error
                    Log.e("david", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                    Toast.makeText(this@LoginActivity, "Error en el registro", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                // Si ocurre un error en la llamada a la API, mostramos un mensaje de error
                Toast.makeText(this@LoginActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
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
        val validPasswordFormat =
            "^.*(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=,!]).*$".toRegex()
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
        if (binding.etNameSignup.text.toString().isNotEmpty()) {
            binding.tiName.error = null
            usernameEmpty = false
        } else {
            binding.tiName.error = "Este campo no puede estar vacío"
            usernameEmpty = true
        }
        return usernameEmpty
    }


    //-----FUNCIONES DE CAMBIO DE LAYOUT-----
    private fun showLoginLayout() {
        binding.layoutLogin.visibility = View.VISIBLE
        binding.tvTitleSignin.visibility = View.VISIBLE
        binding.layoutRegister.visibility = View.GONE
        binding.tvTitleSignup.visibility = View.GONE

    }

    private fun showRegisterLayout() {

        binding.layoutLogin.visibility = View.GONE
        binding.tvTitleSignup.visibility = View.VISIBLE
        binding.layoutRegister.visibility = View.VISIBLE
        binding.tvTitleSignin.visibility = View.GONE
    }
}