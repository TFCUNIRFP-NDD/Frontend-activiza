package com.activiza.activiza.ui.view.fragmentos


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.activiza.activiza.R
import com.activiza.activiza.data.DetallesUsuarioData
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.databinding.FragmentEntrenamientosBinding
import com.activiza.activiza.domain.APIListener
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.viewmodel.RutinasAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class EntrenamientosFragment : Fragment() {
    private var _binding: FragmentEntrenamientosBinding? = null
    private val binding get() = _binding!!

    // Variable de estado para manejar la flecha y la visibilidad de los ítems
    private var isArrowUp = false
    private lateinit var db: ActivizaDataBaseHelper
    private var rutina: RutinaData? = null

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
        mostrarPanelDeControl()
        inicializarVariables()
        inicializarEventos()
        // mostrarShimmer()
    }

    private fun mostrarPanelDeControl() {
        db = ActivizaDataBaseHelper(binding.tvRutinaName.context)
        rutina = db.obtenerPrimeraRutina()
        if (rutina != null) {
            binding.hsvSubMenu.visibility = View.VISIBLE
            binding.btnPanelDeControl.setOnClickListener {
                findNavController().navigate(R.id.action_entrenamientosFragment_to_panelDeControlFragment)
            }
            binding.btnEliminarRutina.setOnClickListener {
                db.borrarEjerciciosYRutinas()
                binding.hsvSubMenu.visibility = View.GONE
            }
        }
    }

    private fun inicializarVariables() {
        val detallesUsuario = db.getDetallesUsuario()
        binding.tvRutinaName.text = "Rutinas ${detallesUsuario!!.genero}".uppercase()
        anadirDatosRetrofit()
    }

    private fun anadirDatosRetrofit() {
        val binding = _binding ?: return  // Verificar si _binding es nulo y salir de la función si lo es

        // Iniciar el progressBar hasta que cargue los datos
        binding.pbCargaRutina.visibility = View.VISIBLE
        binding.ivArrow.isEnabled = false // Deshabilitar la flecha mientras se cargan los datos

        // Utilizar el scope del ciclo de vida del fragmento para manejar las corrutinas
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Recoger token de sql lite
                val token = db.obtenerToken()
                var call: Response<List<RutinaData>>? = null
                val usuario = db.getDetallesUsuario()
                // Realizar la solicitud HTTP en el hilo de fondo
                if(usuario?.genero.isNullOrEmpty() && usuario?.objetivo.isNullOrEmpty() && usuario?.lugar_entrenamiento.isNullOrEmpty()) {
                    call = withContext(Dispatchers.IO) {
                        getRetrofit().create(APIListener::class.java)
                            .getTodasRutinas("Token $token")
                            .execute()
                    }
                }else{
                    var genero:String = ""
                    when(usuario?.genero.toString()){
                        "Hombre" -> genero = "H"
                        "Mujer" -> genero = "M"
                    }
                    var lugar:String = ""
                    when(usuario?.lugar_entrenamiento.toString()){
                        "Gimnasio" -> lugar = "G"
                        "Casa" -> lugar = "C"
                    }
                    var objetivo:String = ""
                    when(usuario?.objetivo.toString()){
                        "Ganar masa muscular" -> objetivo = "MUSCULO"
                        "Perder peso" -> objetivo = "GRASA"
                    }
                    call = withContext(Dispatchers.IO) {
                        getRetrofit().create(APIListener::class.java)
                            .getRutinasFiltradas("Token $token",genero,lugar,objetivo)
                            .execute()
                    }
                }

                // Verificar si la respuesta no es nula
                val rutinas = call.body()
                if (rutinas != null) {
                    // Configurar el RecyclerView en el hilo principal
                    withContext(Dispatchers.Main) {
                        binding.rvRutinas.adapter = RutinasAdapter(rutinas) { rutinaId ->
                            findNavController().navigate(
                                EntrenamientosFragmentDirections.actionEntrenamientosFragmentToRutinaIDFragment(
                                    id = rutinaId
                                )
                            )
                        }
                        binding.rvRutinas.layoutManager = LinearLayoutManager(requireContext())
                    }
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
                binding.ivArrow.isEnabled = true // Habilitar la flecha después de que se carguen los datos
            }
        }
    }

    private fun inicializarEventos() {
        binding.ivArrow.setOnClickListener {
            // Cambiar la imagen y el estado de acuerdo con el estado actual
            if (isArrowUp) {
                binding.ivArrow.setImageResource(R.drawable.ic_arrow_down)
                // Aplicar la animación de entrada (desplazamiento hacia abajo)
                val slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_down)
                binding.rvRutinas.startAnimation(slideIn)
                binding.rvRutinas.visibility = View.VISIBLE
            } else {
                binding.ivArrow.setImageResource(R.drawable.ic_arrow_up)
                // Aplicar la animación de salida (desplazamiento hacia arriba)
                val slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_up)
                slideOut.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {
                        // Después de que la animación termine, ocultar el RecyclerView
                        binding.rvRutinas.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                binding.rvRutinas.startAnimation(slideOut)
            }
            // Cambiar el estado
            isArrowUp = !isArrowUp
        }
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
