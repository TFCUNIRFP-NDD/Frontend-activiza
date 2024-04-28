package com.activiza.activiza.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.activiza.activiza.R
import com.activiza.activiza.databinding.ActivityOnboarding3Binding
import com.activiza.activiza.ui.viewmodel.OnboardingFunctions

class Onboarding3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboarding3Binding
    private lateinit var functions: OnboardingFunctions
    private var genero = "Hombre"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboarding3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        inicializarVariables()
        functions.pintarBarraNaranja(binding.viewOrangeLine, 95)
        inicializarEventos()
    }

    private fun inicializarEventos() {
        binding.btnNext.setOnClickListener {
            siguienteIntent()
        }
    }

    private fun siguienteIntent() {
        guardarSesiones()
        val homeAcitivity = Intent(this,HomeActivity::class.java)
        startActivity(homeAcitivity)
    }

    private fun guardarSesiones() {
        val sharedPreferences = getSharedPreferences("datos_sesion", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("genero", intent.getStringExtra("genero"))
        editor.putString("nombre", intent.getStringExtra("nombre"))
        editor.putString("peso", intent.getStringExtra("peso"))
        editor.putString("objetivo", intent.getStringExtra("objetivo"))

        editor.apply()
    }

    private fun inicializarVariables() {
        functions = OnboardingFunctions()
        var genero = intent.getStringExtra("genero").toString()
        var nombre = intent.getStringExtra("nombre").toString()
        var peso = intent.getStringExtra("peso").toString()
        var objetivo = intent.getStringExtra("objetivo").toString()
        Log.i("info", genero)
        if (genero == this.genero) {
            binding.ivActivizaGenero.setImageResource(R.drawable.ic_male_man)
        }else{
            binding.ivActivizaGenero.setImageResource(R.drawable.ic_female_woman)
        }
        binding.tvPresentacionOnboarding.text = "Entonces $nombre eres de genero $genero pesas $peso y tu objetivo es $objetivo"
    }
}