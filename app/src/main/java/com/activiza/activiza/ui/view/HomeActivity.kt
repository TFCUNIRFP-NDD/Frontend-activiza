package com.activiza.activiza.ui.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.activiza.activiza.R
import com.activiza.activiza.databinding.ActivityHomeBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.view.fragmentos.EntrenamientosFragment
import com.activiza.activiza.ui.view.fragmentos.FeelsFragment
import com.activiza.activiza.ui.view.fragmentos.PanelDeControlFragment
import com.activiza.activiza.ui.view.fragmentos.SettingsFragment

class HomeActivity : AppCompatActivity() {

    lateinit var binding:ActivityHomeBinding
    lateinit var db:ActivizaDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        // Configurar el listener para manejar la selección del ícono del menú de navegación
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            handleBottomNavigation(menuItem, navController)
        }

        initUI()

    }


    private fun initUI() {
        inicializarVariables()
        addEvents()
    }
    private fun handleBottomNavigation(menuItem: MenuItem, navController: NavController): Boolean {
        val destinationId = menuItem.itemId
        val handled = when (destinationId) {
            R.id.entrenamientosFragment,
            R.id.panelDeControlFragment,
            R.id.rutinaIDFragment,
            R.id.ejercicioDetalladoFragment,
            R.id.comenzarEntrenamientoFragment -> {
                // Si el destino es uno de los fragmentos esperados, navegamos a ese destino
                navController.navigate(destinationId)
                true
            }
            R.id.feelsFragment -> {
                navController.navigate(destinationId)
                true // Indica que el evento ha sido manejado
            }
            R.id.settingsFragment -> {
                navController.navigate(destinationId)
                true // Indica que el evento ha sido manejado
            }
            else -> false // No se maneja el evento para otros ítems de menú
        }

        // Si el evento no ha sido manejado, seleccionamos manualmente el ícono del menú
        if (!handled) {
            menuItem.isChecked = true
        }

        return handled
    }


    private fun inicializarVariables() {
        db = ActivizaDataBaseHelper(this)
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