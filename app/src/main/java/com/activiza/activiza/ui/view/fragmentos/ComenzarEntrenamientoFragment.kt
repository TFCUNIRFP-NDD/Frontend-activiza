package com.activiza.activiza.ui.view.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.databinding.FragmentComenzarEntrenamientoBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.viewmodel.EntrenamientosAdapter
import com.activiza.activiza.ui.viewmodel.RutinasAdapter

class ComenzarEntrenamientoFragment : Fragment() {

    private var _binding: FragmentComenzarEntrenamientoBinding? = null
    private val binding get() = _binding!!
    lateinit var rutina: RutinaData
    lateinit var ejercicios: ArrayList<EjerciciosData>
    lateinit var db: ActivizaDataBaseHelper
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

    private fun initUI() {
        recogerRutina()
        adaptarLaVista()
    }

    private fun adaptarLaVista() {
        // Configurar el RecyclerView en el hilo principal
        binding.rvEjercicios.adapter = EntrenamientosAdapter(ejercicios,) { rutinaId ->
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

}