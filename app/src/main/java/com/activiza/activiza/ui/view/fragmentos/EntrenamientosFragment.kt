package com.activiza.activiza.ui.view.fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.activiza.activiza.R
import com.activiza.activiza.databinding.FragmentEntrenamientosBinding
import com.activiza.activiza.domain.APIListener
import com.activiza.activiza.domain.OnFragmentActionsListener
import com.activiza.activiza.ui.view.HomeActivity
import com.activiza.activiza.ui.viewmodel.FragmentFunctions
import com.activiza.activiza.ui.viewmodel.RutinasAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class EntrenamientosFragment : Fragment() {
    private var _binding: FragmentEntrenamientosBinding? = null
    private val binding get() = _binding!!

    //cambiar la posicion de > < que hay pero hacia arriba y abajo por defecto esta abajo
    private var isArrowUp = false // Variable de estado
    private var genero:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntrenamientosBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initUI()
        return rootView
    }

    private fun initUI() {
        inicializarVariables()
        inicializarEventos()
    }

    private fun inicializarVariables() {
        val sharedPreferences = requireContext().getSharedPreferences("datos_sesion", Context.MODE_PRIVATE)
        genero = sharedPreferences.getString("genero", "").toString()
        binding.tvRutinaName.text = "Rutinas $genero".uppercase()
        anadirDatosRetrofit()

    }

    private fun anadirDatosRetrofit() {
        val binding = _binding ?: return  // Verificar si _binding es nulo y salir de la función si lo es

        // Iniciar el progressBar hasta que cargue los datos
        binding.pbCargaRutina.visibility = View.VISIBLE

        // Utilizar el scope del ciclo de vida del fragmento para manejar las corrutinas
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Realizar la solicitud HTTP en el hilo de fondo
                val call = withContext(Dispatchers.IO) {
                    getRetrofit().create(APIListener::class.java)
                        .getTodasRutinas()
                        .execute()
                }

                // Verificar si la respuesta no es nula
                val rutinas = call.body()
                if (rutinas != null) {
                    // Configurar el RecyclerView en el hilo principal
                    binding.rvRutinas.adapter = RutinasAdapter(rutinas) { rutinaId ->
                        findNavController().navigate(R.id.action_entrenamientosFragment_to_rutinaIDFragment)
                    }
                    binding.rvRutinas.layoutManager = LinearLayoutManager(requireContext())
                } else {
                    // Manejar el caso donde la respuesta es nula
                    Log.e("Error", "La respuesta de la llamada Retrofit es nula")
                }
            } catch (e: IOException) {
                // Manejar el error al realizar la solicitud HTTP
                Log.e("Error", "Error al realizar la solicitud HTTP: ${e.message}")
                anadirDatosRetrofit()
            } finally {
                // Ocultar el progressBar después de cargar los datos o manejar el error
                binding.pbCargaRutina.visibility = View.GONE
            }
        }
    }


    private fun inicializarEventos() {
        binding.ivArrow.setOnClickListener {
            // Cambiar la imagen y el estado de acuerdo con el estado actual
            if (isArrowUp) {
                binding.ivArrow.setImageResource(R.drawable.ic_arrow_down)
            } else {
                binding.ivArrow.setImageResource(R.drawable.ic_arrow_up)
            }
            // Cambiar el estado
            isArrowUp = !isArrowUp
        }
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://34.163.215.184/activiza/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}