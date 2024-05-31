package com.activiza.activiza.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import com.activiza.activiza.R
import com.activiza.activiza.data.DetallesUsuarioData
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.databinding.ActivityOnboarding3Binding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.viewmodel.OnboardingFunctions

class Onboarding3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboarding3Binding
    private lateinit var functions: OnboardingFunctions
    private var genero = "Hombre"
    private lateinit var db:ActivizaDataBaseHelper

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
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun guardarSesiones() {
        db = ActivizaDataBaseHelper(this)
        val lugar:String? = intent.getStringExtra("lugar")
        val altura: Double? = intent.getStringExtra("altura")?.toDouble()
        val peso:Double? = intent.getStringExtra("peso")?.toDouble()
        val genero:String? = intent.getStringExtra("genero")
        val objetivo:String? = intent.getStringExtra("objetivo")
        val nombre: String? = intent.getStringExtra("nombre")
        val usuariosDetalles:DetallesUsuarioData = DetallesUsuarioData(altura!!,peso!!,genero!!,objetivo!!, lugar!!)
        db.insertDetallesUsuario(usuariosDetalles,db.getUsuario()!!.token)

}

    private fun inicializarVariables() {
        functions = OnboardingFunctions()
        var genero = intent.getStringExtra("genero").toString()
        var nombre = intent.getStringExtra("nombre").toString()
        var peso = intent.getStringExtra("peso").toString()
        var objetivo = intent.getStringExtra("objetivo").toString()
        var lugar = intent.getStringExtra("lugar").toString()
        var altura = intent.getStringExtra("altura").toString()
        Log.i("info", genero)
//        if (genero == this.genero) {
//            binding.ivActivizaGenero.setImageResource(R.drawable.ic_male_man)
//        }else{
//            binding.ivActivizaGenero.setImageResource(R.drawable.ic_female_woman)
//        }
        binding.tvPresentacionOnboarding.text = "Bienvenid@, $nombre ahora dispones de las mejores rutinas adaptadas tus objetivos"
    }


}