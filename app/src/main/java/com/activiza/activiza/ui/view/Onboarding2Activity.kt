package com.activiza.activiza.ui.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.activiza.activiza.R
import com.activiza.activiza.databinding.ActivityOnboarding2Binding
import com.activiza.activiza.ui.viewmodel.OnboardingFunctions

class Onboarding2Activity : AppCompatActivity() {

    private lateinit var binding : ActivityOnboarding2Binding
    private lateinit var functions: OnboardingFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboarding2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        inicializarVariables()
        functions.pintarBarraNaranja(binding.viewOrangeLine, 60)
        inicializarEventos()
    }

    private fun inicializarEventos() {
        binding.btnNext.setOnClickListener {
            var objetivoChecked = binding.rgObjetivo.checkedRadioButtonId
            var generoChecked = binding.rgPersona.checkedRadioButtonId
            if(generoChecked != -1 && objetivoChecked != -1){
                navegarSiguienteIntent()
            }else{
                showError("Selecciona todas las opciones")
            }
        }
    }

    private fun navegarSiguienteIntent() {
        //Basicamente lo que hace es recoger del RadioGroup la opcion marcada y de la opcion el texto en String
        val objetivoButton = binding.root.findViewById<RadioButton>(binding.rgObjetivo.checkedRadioButtonId).text.toString()
        val generoButton = binding.root.findViewById<RadioButton>(binding.rgPersona.checkedRadioButtonId).text.toString()
        val lugarButton = binding.root.findViewById<RadioButton>(binding.rgLugar.checkedRadioButtonId).text.toString()
        val intent = Intent(this, Onboarding3Activity::class.java).apply {
            putExtra("nombre", intent.getStringExtra("nombre").toString())
            putExtra("peso", intent.getStringExtra("peso").toString())
            putExtra("altura", intent.getStringExtra("altura").toString())
            putExtra("objetivo", objetivoButton)
            putExtra("genero", generoButton)
            putExtra("lugar", lugarButton)
        }
        startActivity(intent)
    }

    private fun inicializarVariables() {
        val nombre = intent.getStringExtra("nombre").toString()
        val peso = intent.getStringExtra("peso").toString()
        val altura = intent.getStringExtra("altura").toString()
        binding.tvPresentacionOnboarding.setText("Cúal es tu objetivo?")
        functions = OnboardingFunctions()
    }

    private fun showError(mensaje:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null) // Puedes agregar un listener si necesitas realizar alguna acción al hacer clic en "Aceptar"
        builder.show()
    }

}