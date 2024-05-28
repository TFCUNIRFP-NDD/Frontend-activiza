package com.activiza.activiza.ui.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.activiza.activiza.R
import com.activiza.activiza.data.DetallesUsuarioData
import com.activiza.activiza.data.UserPreferences
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.databinding.ActivityHomeBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.view.fragmentos.EntrenamientosFragment
import com.activiza.activiza.ui.view.fragmentos.FeelsFragment
import com.activiza.activiza.ui.view.fragmentos.PanelDeControlFragment
import com.activiza.activiza.ui.view.fragmentos.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var db: ActivizaDataBaseHelper
    lateinit var userPreferences: UserPreferences
    private lateinit var navHostFragment: NavHostFragment
    private val elementosPermitidos = setOf(
        R.id.entrenamientosFragment,
        R.id.panelDeControlFragment,
        R.id.anadirRutinaFragment,
        R.id.feelsFragment,
        R.id.settingsFragment
    )

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

        // Si el elemento es permitido, continuar con la navegación
        navController.navigate(destinationId)
        return true
    }

    private fun inicializarVariables() {
        db = ActivizaDataBaseHelper(this)
        inicializarBottomMenu()
    }

    private fun inicializarBottomMenu() {
        val usuarioData: UsuarioData? = db.getUsuario()
        val token = usuarioData?.token
        userPreferences = UserPreferences(this)

        if (usuarioData != null) {
            val bottomNavigationMenu: BottomNavigationView = binding.bottomNavigationView
            if (usuarioData.entrenador) {
                bottomNavigationMenu.inflateMenu(R.menu.bottom_menu_entrenador)
            } else {
                bottomNavigationMenu.inflateMenu(R.menu.bottom_menu)
            }
        } else {
            // Manejo de caso donde no se obtiene el usuario
            // Por ejemplo, mostrar un mensaje de error o tomar una acción alternativa
            Log.d("Error", "No se han encontrado entrenadores")
        }
    }

    private fun addEvents() {
    }

    override fun onBackPressed() {
        // Obtener el fragmento actual y su ID de destino
        val currentDestination = navHostFragment.navController.currentDestination?.id

        // Verificar si el fragmento actual está en los elementos permitidos del menú inferior
        if (currentDestination != null && currentDestination in elementosPermitidos) {
            // Si el fragmento actual está en los elementos permitidos, cerrar la aplicación
            finish()
        } else {
            // Si el fragmento actual no está en los elementos permitidos, ejecutar el comportamiento predeterminado
            super.onBackPressed()
        }
    }
}
