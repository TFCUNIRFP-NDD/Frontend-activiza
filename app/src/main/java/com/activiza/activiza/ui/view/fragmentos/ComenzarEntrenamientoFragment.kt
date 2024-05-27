package com.activiza.activiza.ui.view.fragmentos

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.databinding.FragmentComenzarEntrenamientoBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.viewmodel.EntrenamientosAdapter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import kotlin.time.times

class ComenzarEntrenamientoFragment : Fragment() {

    private var _binding: FragmentComenzarEntrenamientoBinding? = null
    private val binding get() = _binding!!
    lateinit var rutina: RutinaData
    lateinit var ejercicios: ArrayList<EjerciciosData>
    lateinit var db: ActivizaDataBaseHelper
    companion object{
        const val COMPLETAR = "completar"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentComenzarEntrenamientoBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initUI()
        return rootView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        recogerRutina()
        adaptarLaVista()
        reemplazarElementos()
        inicializarEventos()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun reemplazarElementos() {
        val fechaActual = LocalDate.now()
        val year = fechaActual.year
        val month = fechaActual.monthValue
        val day = fechaActual.dayOfMonth
        binding.tvDiaDeEntreno.text = "$day/$month/$year"

        val cantidadEntrenamientosCompletados = db.obtenerCantidadEntrenamientosCompletados()

        val porcentaje = ((cantidadEntrenamientosCompletados.toDouble() / ejercicios.size) * 100).toInt()
        binding.tvPorcentajeEjercicio.text = "$porcentaje%"
        if(db.obtenerEstadoDeRutina(rutina.id, obtenerFechaActual())){
            binding.btnComenzarRutina.text = COMPLETAR
        }
    }

    private fun inicializarEventos() {
        binding.btnComenzarRutina.setOnClickListener {
            if (binding.btnComenzarRutina.text.toString() == COMPLETAR.toString()) {
                db.marcarComoCompletadoCalendario(rutina.id,obtenerFechaActual())
                findNavController().popBackStack()
            } else {
                for (ejercicio in ejercicios) {
                    if (!db.todosEntrenamientosCompletadosParaEjercicio(ejercicio.id, obtenerFechaActual())) {
                        findNavController().navigate(
                            ComenzarEntrenamientoFragmentDirections.actionComenzarEntrenamientoFragmentToEjercicioDetalladoFragment(
                                id = ejercicio.id
                            )
                        )
                        break // Sale del bucle después de encontrar el primer ejercicio no completado
                    }
                }
            }
        }
    }





    private fun adaptarLaVista() {
        // Configurar el RecyclerView en el hilo principal
        binding.rvEjercicios.adapter = EntrenamientosAdapter(ejercicios) { rutinaId ->
            findNavController().navigate(ComenzarEntrenamientoFragmentDirections.actionComenzarEntrenamientoFragmentToEjercicioDetalladoFragment(
                id = rutinaId
            ))
        }
        binding.rvEjercicios.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun recogerRutina() {
        db = ActivizaDataBaseHelper(binding.tvDiaDeEntreno.context)
        rutina = db.obtenerPrimeraRutina()!!
        ejercicios = db.getEjerciciosDeRutina(rutina.id)
    }
    fun obtenerFechaActual(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}