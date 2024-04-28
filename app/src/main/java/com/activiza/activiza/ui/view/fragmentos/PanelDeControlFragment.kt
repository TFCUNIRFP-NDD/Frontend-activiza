package com.activiza.activiza.ui.view.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.activiza.activiza.R
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.databinding.FragmentEntrenamientosBinding
import com.activiza.activiza.databinding.FragmentPanelDeControlBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.squareup.picasso.Picasso

class PanelDeControlFragment : Fragment() {

    private var _binding: FragmentPanelDeControlBinding? = null
    private val binding get() = _binding!!
    lateinit var rutina: RutinaData
    lateinit var db: ActivizaDataBaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPanelDeControlBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initUI()
        return rootView
    }

    private fun initUI() {
        recogerRutina()
        pintarDatos()
        inicializarEventos()
    }

    private fun inicializarEventos() {
        binding.btnEntrenamientos.setOnClickListener {
            findNavController().navigate(R.id.action_panelDeControlFragment_to_entrenamientosFragment)
        }
    }

    private fun pintarDatos() {
        binding.tvNameRutinaPanelControl.text = rutina.nombre
        binding.tvDescripcionPanelDeControl.text = rutina.descripcion
        deUrlAImageView(rutina.media,binding.ivRutinaPanelControl)
    }
    fun deUrlAImageView(url:String, imageView: ImageView){
        Picasso.get().load(url).into(imageView)
    }

    private fun recogerRutina() {
        db = ActivizaDataBaseHelper(binding.tvDescripcionPanelDeControl.context)
        rutina = db.obtenerPrimeraRutina()!!
    }

}