package com.activiza.activiza.ui.view.fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.activiza.activiza.databinding.FragmentAnadirEjerciciosBinding
import com.activiza.activiza.domain.APIListener
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.viewmodel.EjerciciosAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class AnadirEjerciciosFragment : Fragment() {

    private var _binding: FragmentAnadirEjerciciosBinding? = null
    private val binding get() = _binding!!
    val args:AnadirEjerciciosFragmentArgs by navArgs()
    lateinit var db: ActivizaDataBaseHelper
    // Definir un MutableList de enteros
    var list: MutableList<Int> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAnadirEjerciciosBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initUI()
        return rootView
    }

    private fun initUI() {
        sustituirElementos()
        anadirDatosRetrofit()
        initEvents()
    }

    @SuppressLint("SetTextI18n")
    private fun anadirDatosRetrofit() {
        val binding = _binding ?: return  // Verificar si _binding es nulo y salir de la función si lo es

        // Iniciar el progressBar hasta que cargue los datos
        binding.pbCargarEjerciciosAnadir.visibility = View.VISIBLE

        // Utilizar el scope del ciclo de vida del fragmento para manejar las corrutinas
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                //Recoger token de sql lite
                val token = db.obtenerToken()
                // Realizar la solicitud HTTP en el hilo de fondo
                val call = withContext(Dispatchers.IO) {
                    getRetrofit().create(APIListener::class.java)
                        .getTodosEjercicios("Token $token")
                        .execute()
                }

                // Verificar si la respuesta no es nula
                val ejercicio = call.body()
                if (ejercicio != null) {
                    // Configurar el RecyclerView en el hilo principal
                    binding.rvEjerciciosAnadir.adapter = EjerciciosAdapter(ejercicio, args.numEjercicios) { ejercicioId ->
                        // Agregar el número X si está presente, de lo contrario, eliminarlo
                        if (ejercicioId in list) {
                            list.removeAll { it == ejercicioId }
                            binding.tvEjerciciosSeleccionados.text = "${list.size}/${args.numEjercicios}"
                        } else {
                            if(list.size != args.numEjercicios) {
                                list.add(ejercicioId)
                                binding.tvEjerciciosSeleccionados.text =
                                    "${list.size}/${args.numEjercicios}"
                            }
                        }
                        Log.d("AnadidoEjercicio",list.toString())
                    }
                    binding.rvEjerciciosAnadir.layoutManager = LinearLayoutManager(requireContext())
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
                binding.pbCargarEjerciciosAnadir.visibility = View.GONE
            }
        }
    }

    private fun initEvents() {
        binding.btnComenzarRutina.setOnClickListener {

        }
    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://34.163.215.184/activiza/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @SuppressLint("SetTextI18n")
    private fun sustituirElementos() {
        db = ActivizaDataBaseHelper(binding.tvEjerciciosSeleccionados.context)
        binding.tvEjerciciosSeleccionados.text = "0/${args.numEjercicios}"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}