package com.activiza.activiza.ui.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.activiza.activiza.R
import com.activiza.activiza.databinding.ActivityHomeBinding
import com.activiza.activiza.ui.view.fragmentos.EntrenamientosFragment
import com.activiza.activiza.ui.view.fragmentos.FeelsFragment
import com.activiza.activiza.ui.view.fragmentos.SettingsFragment

class HomeActivity : AppCompatActivity() {

    lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        initUI()
    }

    private fun initUI() {
        inicializarVariables()
        addEvents()
    }

    private fun inicializarVariables() {

    }

    private fun addEvents() {
        /** Activar el boton para borrar las cookies
        binding.btnBorrarCookies.setOnClickListener {
            destruirSesiones()
        }
        **/
    }
    private fun destruirSesiones() {
        val sharedPreferences = getSharedPreferences("datos_sesion", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        // Eliminar todas las sesiones del usuario
        editor.clear()

        editor.apply()
    }

}