package com.activiza.activiza.ui.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.activiza.activiza.R
import com.activiza.activiza.databinding.ActivityHomeBinding
import com.activiza.activiza.domain.OnFragmentActionsListener
import com.activiza.activiza.ui.view.fragmentos.EntrenamientosFragment
import com.activiza.activiza.ui.view.fragmentos.FeelsFragment
import com.activiza.activiza.ui.view.fragmentos.SettingsFragment
import com.activiza.activiza.ui.viewmodel.FragmentFunctions

class HomeActivity : AppCompatActivity() {

    lateinit var binding:ActivityHomeBinding
    lateinit var fragments : FragmentFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        inicializarVariables()
        addEvents()
    }

    private fun inicializarVariables() {
        //Iniciamos los fragments
        fragments = FragmentFunctions()
        //Le pasamos a la interfaz el Fragment Manager
        fragments.setFragmentManager(supportFragmentManager)
        //Ahora ya esta listo para recibir fragmentos
        fragments.onClickChangeFragments(EntrenamientosFragment())
    }

    private fun addEvents() {
        /** Activar el boton para borrar las cookies
        binding.btnBorrarCookies.setOnClickListener {
            destruirSesiones()
        }
        **/
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itemEntrenamiento -> fragments.onClickChangeFragments(EntrenamientosFragment())
                R.id.itemFeel ->fragments.onClickChangeFragments(FeelsFragment())
                R.id.itemSettings ->fragments.onClickChangeFragments(SettingsFragment())
                else -> {

                }
            }
            true
        }
    }
    private fun destruirSesiones() {
        val sharedPreferences = getSharedPreferences("datos_sesion", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        // Eliminar todas las sesiones del usuario
        editor.clear()

        editor.apply()
    }

}