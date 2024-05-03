package com.activiza.activiza.ui.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
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

    lateinit var binding: ActivityHomeBinding
    lateinit var db: ActivizaDataBaseHelper
    lateinit var navHostFragment: NavHostFragment
    private var elementosPermitidos = setOf(
        R.id.entrenamientosFragment,
        R.id.panelDeControlFragment,
        R.id.feelsFragment,
        R.id.settingsFragment
    )
    private var ultimoFragmentoVisitado: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
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

        // Verificar si el elemento está permitido
        if (destinationId !in elementosPermitidos) {
            // Elemento no permitido, mostrar mensaje o realizar alguna acción
            Toast.makeText(this, "Elemento no permitido", Toast.LENGTH_SHORT).show()
            return false
        }

        // Guardar el ID del último fragmento visitado
        ultimoFragmentoVisitado = destinationId

        // Si el elemento es permitido, continuar con la navegación
        navController.navigate(destinationId)
        return true
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

    override fun onBackPressed() {
        // Si hay un fragmento anterior, navegar a él
        if (ultimoFragmentoVisitado != null) {
            finish()
        } else {
            // Si no hay un fragmento anterior, ejecutar el comportamiento predeterminado
            super.onBackPressed()
        }
    }
}