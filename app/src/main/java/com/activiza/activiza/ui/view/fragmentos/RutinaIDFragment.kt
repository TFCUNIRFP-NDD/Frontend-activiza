package com.activiza.activiza.ui.view.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.activiza.activiza.R
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.databinding.FragmentRutinaIdBinding
import com.activiza.activiza.domain.APIListener
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class RutinaIDFragment : Fragment() {

    private var _binding: FragmentRutinaIdBinding? = null

    val args:RutinaIDFragmentArgs by navArgs()
    lateinit var rutina:RutinaData
    lateinit var db:ActivizaDataBaseHelper
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRutinaIdBinding.inflate(inflater, container, false)
        db = ActivizaDataBaseHelper(binding.btnAgregar.context)
        initUI()
        return binding.root
    }

    private fun anadirDatosRetrofit() {
        val binding = _binding ?: return  // Verificar si _binding es nulo y salir de la función si lo es

        // Utilizar el scope del ciclo de vida del fragmento para manejar las corrutinas
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Realizar la solicitud HTTP en el hilo de fondo
                val call = withContext(Dispatchers.IO) {
                    getRetrofit().create(APIListener::class.java)
                        .getRutina(args.id)
                        .execute()
                }

                // Verificar si la respuesta no es nula
                val rutinas = call.body()
                if (rutinas != null) {
                    Log.d("ejercicio",rutinas.toString())
                    rutina = rutinas
                    remplazarDatosRutina()
                } else {
                    // Manejar el caso donde la respuesta es nula
                    Log.e("Error", "La respuesta de la llamada Retrofit es nula")
                }
            } catch (e: IOException) {
                // Manejar el error al realizar la solicitud HTTP
                Log.e("Error", "Error al realizar la solicitud HTTP: ${e.message}")
                anadirDatosRetrofit()
            }
        }
    }

    private fun initUI() {
        // Inicializar la interfaz de usuario del fragmento si es necesario
        anadirDatosRetrofit()
        anadirEventos()
    }

    private fun anadirEventos() {
        binding.btnAgregar.setOnClickListener {
            db.borrarEjerciciosYRutinas()
            db.insertRutina(rutina)
            rutina.ejercicios.forEach{
                var ejercicio:EjerciciosData = EjerciciosData(it.id,it.nombre,it.descripcion,it.repeticiones,it.duracion,it.descanso,it.media)
                db.insertEjercicio(ejercicio,rutina.id)
            }
            findNavController().navigate(R.id.action_rutinaIDFragment_to_panelDeControlFragment)
        }
    }

    private fun remplazarDatosRutina() {
        binding.tvNameRutina.text = rutina.nombre
        binding.tvCategorias.text = rutina.tipo
        binding.tvDetalles.text = rutina.descripcion
        deUrlAImageView(rutina.media,binding.ivRutinaItem)
    }

    fun deUrlAImageView(url:String, imageView: ImageView){
        Picasso.get().load(url).into(imageView)
    }
    private fun getRetrofit(): Retrofit {
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