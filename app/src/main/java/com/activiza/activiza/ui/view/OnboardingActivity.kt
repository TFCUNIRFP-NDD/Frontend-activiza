package com.activiza.activiza.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.activiza.activiza.R
import com.activiza.activiza.data.DetallesUsuarioData
import com.activiza.activiza.databinding.ActivityOnboardingBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.viewmodel.OnboardingFunctions

class OnboardingActivity : AppCompatActivity() {

    //inicializamos el view binding
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var functions: OnboardingFunctions
    private var diccionarioErrors = mutableMapOf<String, Boolean>()
    private lateinit var peso:String
    private lateinit var name:String
    private lateinit var altura:String
    private lateinit var db:ActivizaDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(sesionesCompletas()){
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
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
        altura = "ErrorAltura"
        inicializarDiccionario()
    }
    private fun sesionesCompletas(): Boolean {
        db = ActivizaDataBaseHelper(this)
        val detallesUsuario:DetallesUsuarioData? = db.getDetallesUsuario()
        // Verifica si alguna sesión falta
        if (detallesUsuario == null) {
            return false // Faltan una o más sesiones
        }

        // Si todas las sesiones tienen valores, devuelve true
        return true
    }

    private fun inicializarDiccionario() {
        diccionarioErrors[name] = false
        diccionarioErrors[peso] = false
        diccionarioErrors[altura] =  false
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

        binding.etAltura.setOnClickListener {
                comunAltura()
        }
    }

    private fun navegarSiguienteIntent() {
        val intent = Intent(this, Onboarding2Activity::class.java).apply {
            putExtra("nombre", binding.etName.text.toString())
            putExtra("peso", binding.etPeso.text.toString())
            putExtra("altura", binding.etAltura.text.toString())
        }
        startActivity(intent)
    }

    private fun ventanaSiguienteComprobacion():Boolean {
        val pesoText = binding.etPeso.text.toString()
        val nameText = binding.etName.text.toString()
        val alturaText = binding.etAltura.text.toString()
        val peso = pesoText.toFloatOrNull()
        val altura = alturaText.toFloatOrNull()
        var comprobacion = true
        when{
            pesoText.isEmpty() -> {
                errorPeso("No puede estar vacío")
                comprobacion = false
            }
            peso == null || peso < 30 || peso > 250 -> {
                errorPeso("Tiene que estar entre 30 y 250 kg")
                comprobacion = false
            }
            nameText.isEmpty() -> {
                errorName("No puede estar vacío")
                comprobacion = false
            }
            alturaText.isEmpty() -> {
                errorAltura("No puede estar vacío")
                comprobacion = false
            }
            altura == null || altura < 100 || altura > 230 -> {
                errorAltura("Tiene que estar entre 100 y 230 cm")
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
    private fun errorAltura(mensaje:String){
        binding.etAltura.setTextColor(getColor(R.color.error))
        binding.etAltura.setText(mensaje)
        binding.etAltura.clearFocus()
        diccionarioErrors[altura] = true
    }
    private fun comunPeso(){
        binding.etPeso.setTextColor(getColor(R.color.blue_light))
        binding.etPeso.setText("")
        diccionarioErrors[peso] = false
    }
    private fun comunName(){
        binding.etName.setTextColor(getColor(R.color.blue_light))
        binding.etName.setText("")
        diccionarioErrors[name] = false
    }
    private fun comunAltura(){
        binding.etAltura.setTextColor(getColor(R.color.blue_light))
        binding.etAltura.setText("")
        diccionarioErrors[altura] = false
    }

}