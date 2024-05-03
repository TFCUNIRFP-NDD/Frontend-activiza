package com.activiza.activiza.ui.view.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.activiza.activiza.R
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.EntrenamientoData
import com.activiza.activiza.databinding.FragmentComenzarEntrenamientoBinding
import com.activiza.activiza.databinding.FragmentEjercicioDetalladoBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EjercicioDetalladoFragment : Fragment() {

    val args:RutinaIDFragmentArgs by navArgs()
    private var _binding: FragmentEjercicioDetalladoBinding? = null
    private val binding get() = _binding!!
    lateinit var ejercicio: EjerciciosData
    lateinit var db: ActivizaDataBaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEjercicioDetalladoBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initUI()
        return rootView
    }

    private fun initUI() {
        recogerRutina()
        inicializarEventos()
    }

    private fun inicializarEventos() {
        binding.btnCompletar.setOnClickListener {
            var idEntrenamiento = db.obtenerIdEntrenamientoPorIdEjercicio(args.id)
            db.marcarEntrenamientoComoCompletado(idEntrenamiento,obtenerFechaActual())
            findNavController().popBackStack()
        }
    }
    fun obtenerFechaActual(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun recogerRutina() {
        db = ActivizaDataBaseHelper(binding.tvTituloEjercicioDetallado.context)
        ejercicio = db.getEjercicio(args.id)!!
        if(ejercicio.repeticiones != 0) {
            binding.tvRepeticionesCircle.text = ejercicio.repeticiones.toString()
        }else{
            binding.tvRepeticionesCircle.text = getString(R.string.no)
        }
        if(ejercicio.descanso != 0) {
            binding.tvDescansoCircle.visibility = View.VISIBLE
            binding.tvDescansoCircle.text = ejercicio.descanso.toString()
        }else{
            binding.tvDescansoCircle.visibility = View.INVISIBLE
            binding.ivDescansoCircle.visibility = View.VISIBLE
        }
        if (ejercicio.duracion != 0) {
            binding.tvMinutosCircle.text = ejercicio.duracion.toString()
        }else{
            binding.tvMinutosCircle.text = getString(R.string.no)
        }
        binding.tvTituloEjercicioDetallado.text = ejercicio.nombre
        binding.tvDescripcionEjercicioDetallado.text = ejercicio.descripcion
        deUrlAImageView(ejercicio.media,binding.ivImagenDetalladaEjercicio)

    }
    fun deUrlAImageView(url:String, imageView: ImageView){
        Picasso.get().load(url).into(imageView)
    }

}