package com.activiza.activiza.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.activiza.activiza.R
import com.activiza.activiza.databinding.ActivityOnboardingBinding
import com.activiza.activiza.ui.viewmodel.OnboardingFunctions

class OnboardingActivity : AppCompatActivity() {

    //inicializamos el view binding
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var functions: OnboardingFunctions
    private var diccionarioErrors = mutableMapOf<String, Boolean>()
    private lateinit var peso:String
    private lateinit var name:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(sesionesCompletas()){
            var pantallaHome = Intent(this, HomeActivity::class.java)
            startActivity(pantallaHome)
        }
        initUI()
    }

    private fun initUI() {
        inicializarVariables()
        inicializarEventos()
    }

    private fun inicializarVariables() {
        functions = OnboardingFunctions()
        functions.pintarBarraNaranja(binding.viewOrangeLine, 20)
        name = "errorNombre"
        peso = "ErrorPeso"
        inicializarDiccionario()
    }
    private fun sesionesCompletas(): Boolean {
        val sharedPreferences = getSharedPreferences("datos_sesion", Context.MODE_PRIVATE)

        val genero = sharedPreferences.getString("genero", "")
        val nombre = sharedPreferences.getString("nombre", "")
        val peso = sharedPreferences.getString("peso", "")
        val objetivo = sharedPreferences.getString("objetivo", "")

        // Verifica si alguna sesión falta
        if (genero.isNullOrEmpty() || nombre.isNullOrEmpty() || peso.isNullOrEmpty() || objetivo.isNullOrEmpty()) {
            return false // Faltan una o más sesiones
        }

        // Si todas las sesiones tienen valores, devuelve true
        return true
    }

    private fun inicializarDiccionario() {
        diccionarioErrors[name] = false
        diccionarioErrors[peso] = false
    }

    private fun inicializarEventos() {
        binding.btnNext.setOnClickListener {
            if(ventanaSiguienteComprobacion()){
                navegarSiguienteIntent()
            }
        }
        binding.etName.setOnClickListener {
                comunName()
        }
        binding.etPeso.setOnClickListener {
                comunPeso()
        }
    }

    private fun navegarSiguienteIntent() {
        val intent = Intent(this, Onboarding2Activity::class.java).apply {
            putExtra("nombre", binding.etName.text.toString())
            putExtra("peso", binding.etPeso.text.toString())
        }
        startActivity(intent)
    }

    private fun ventanaSiguienteComprobacion():Boolean {
        val pesoText = binding.etPeso.text.toString()
        val nameText = binding.etName.text.toString()
        val peso = pesoText.toFloatOrNull()
        var comprobacion = true
        when{
            pesoText.isNullOrEmpty() -> {
                errorPeso("El peso no puede estar vacio")
                comprobacion = false
            }
            nameText.isNullOrEmpty() -> {
                errorName("El nombre no puede estar vacio")
                comprobacion = false
            }
            peso == null || peso<0 || peso>300 ->{
                errorPeso("El peso tiene que ser un número entre 0 y 300")
                comprobacion = false
            }
        }
        return comprobacion
    }

    private fun errorPeso(mensaje:String){
        binding.etPeso.setTextColor(getColor(R.color.error))
        binding.etPeso.setText(mensaje)
        binding.etPeso.clearFocus()
        diccionarioErrors[peso] = true
    }
    private fun errorName(mensaje:String){
        binding.etName.setTextColor(getColor(R.color.error))
        binding.etName.setText(mensaje)
        binding.etName.clearFocus()
        diccionarioErrors[name] = true
    }
    private fun comunPeso(){
        binding.etPeso.setTextColor(getColor(R.color.black))
        binding.etPeso.setText("")
        diccionarioErrors[peso] = false
    }
    private fun comunName(){
        binding.etName.setTextColor(getColor(R.color.black))
        binding.etName.setText("")
        diccionarioErrors[name] = false
    }

}