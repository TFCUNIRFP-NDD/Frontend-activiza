package com.activiza.activiza.ui.view.fragmentos

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.activiza.activiza.R
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.data.UsuarioData
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

    val args: RutinaIDFragmentArgs by navArgs()
    lateinit var rutina: RutinaData
    lateinit var db: ActivizaDataBaseHelper
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRutinaIdBinding.inflate(inflater, container, false)
        db = ActivizaDataBaseHelper(binding.btnAgregar.context)
        initUI()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        // Inicializar la interfaz de usuario del fragmento si es necesario
        anadirDatosRetrofit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                    Log.d("ejercicio", rutinas.toString())
                    rutina = rutinas
                    remplazarDatosRutina()
                    anadirEventos() // Llamar a anadirEventos() después de que se haya inicializado rutina
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun anadirEventos() {
        binding.btnAgregar.setOnClickListener {
            db.borrarEjerciciosYRutinas()
            if (!::rutina.isInitialized) {
                try {
                    rutina = db.obtenerPrimeraRutina()!!
                } catch (e: Exception) {
                    Log.d("error", e.toString())
                }
            }
            db.insertRutina(rutina)
            rutina.ejercicios.forEach {
                val ejercicio = EjerciciosData(
                    it.id, it.nombre, it.descripcion, it.repeticiones,
                    it.duracion, it.descanso, it.media
                )
                db.insertEjercicio(ejercicio, rutina.id)
            }
            findNavController().navigate(R.id.action_rutinaIDFragment_to_panelDeControlFragment)
        }
        binding.btnEliminar.setOnClickListener {
            deleteDatosRetrofit()
        }
        binding.btnModificarRutina.setOnClickListener {
            // Implementar la funcionalidad de modificación de rutina
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteDatosRetrofit() {
        // Utilizar el scope del ciclo de vida del fragmento para manejar las corrutinas
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Realizar la solicitud HTTP en el hilo de fondo
                val call = withContext(Dispatchers.IO) {
                    getRetrofit().create(APIListener::class.java)
                        .deleteRutina(args.id)
                        .execute()
                }

                // Verificar si la respuesta no es nula
                val rutinas = call.body()
                if (rutinas != null) {
                    Log.d("deleteEjercicio", rutinas.toString())
                    findNavController().popBackStack()
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

    private fun remplazarDatosRutina() {
        binding.tvNameRutina.text = rutina.nombre
        binding.tvDetalles.text = rutina.descripcion
        var genero:String = ""
        var lugar:String = ""
        var objetivo:String = ""
        when(rutina.genero){
            "H" -> genero = "hombre"
            "M" -> genero = "mujer"
        }
        when(rutina.lugar_entrenamiento){
            "C" -> lugar = "casa"
            "G" -> lugar = "gimnasio"
        }
        when(rutina.objetivo){
            "GRASA" -> objetivo = "perdida de grasa"
            "MUSCULO" -> objetivo = "ganancia de masa muscular"
        }
        binding.tvCategorias.text = "$lugar, $objetivo, $genero"
        deUrlAImageView(rutina.media, binding.ivRutinaItem)
        val usuarioData: UsuarioData? = db.getUsuario()
        if (usuarioData?.entrenador == true) {
            binding.btnEliminar.visibility = View.VISIBLE
            binding.btnModificarRutina.visibility = View.VISIBLE
        }
        // Hasta la implementación se queda en gone
        binding.btnModificarRutina.visibility = View.GONE
    }

    fun deUrlAImageView(url: String, imageView: ImageView) {
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
