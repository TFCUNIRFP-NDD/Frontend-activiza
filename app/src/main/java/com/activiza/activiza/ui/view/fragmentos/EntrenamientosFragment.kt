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
    lateinit var fragmentListener : OnFragmentActionsListener

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
        //Iniciar el progressBar hasta que cargue los datos
        binding.pbCargaRutina.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val call = getRetrofit().create(APIListener::class.java)
                    .getTodasRutinas()
                    .execute()
                val rutinas = call.body()
                if (rutinas != null) {
                    withContext(Dispatchers.Main) {
                        // Configurar RecyclerView
                        val adapter = RutinasAdapter(rutinas) { rutinaId ->
                            fragmentRutinaCompleta(rutinaId)
                        }
                        binding.rvRutinas.adapter = adapter
                        binding.rvRutinas.layoutManager = LinearLayoutManager(requireContext())
                        //Ocultar el progressBar en ocultar los datos
                        binding.pbCargaRutina.visibility = View.GONE
                    }
                } else {
                    Log.e("Error", "La respuesta de la llamada Retrofit es nula")
                }
            } catch (e: IOException) {
                Log.e("Error", "Error al realizar la solicitud HTTP: ${e.message}")
                // Manejar el error según corresponda
            }
        }
    }
    private fun fragmentRutinaCompleta(id: Int) {
        fragmentListener = HomeActivity()
        val rutinaIDFragment = RutinaIDFragment()
        fragmentListener.onClickChangeFragments(rutinaIDFragment)
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