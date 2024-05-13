package com.activiza.activiza.ui.view.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.activiza.activiza.R
import com.activiza.activiza.databinding.FragmentAnadirRutinaBinding
import com.activiza.activiza.databinding.FragmentEntrenamientosBinding
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AnadirRutinaFragment : Fragment() {

    private var _binding: FragmentAnadirRutinaBinding? = null
    private val binding get() = _binding!!
    val args:AnadirRutinaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnadirRutinaBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initUI()
        return rootView
    }

    private fun initUI() {
        recogerImagen()
        comprobarImagen()
        initEvents()
    }

    private fun initEvents() {
        binding.btnSiguiente.setOnClickListener {
            //Logica del siguiente fragmento
            if(comprobarCampos()){
                var numeroEjercicios = binding.etNumeroEjercicios.text.toString()
                findNavController().navigate(AnadirRutinaFragmentDirections.actionAnadirRutinaFragmentToAnadirEjerciciosFragment(
                    numEjercicios = numeroEjercicios.toInt(),
                    nombre = binding.etNombreAnadirRutina.text.toString(),
                    descripcion = binding.etDescripcionAnadirRutina.text.toString(),
                    genero = binding.etGeneroAnadirRutina.text.toString(),
                    objetivo = binding.etObjetivoAnadirRutina.toString(),
                    lugar = binding.etLugarAnadirRutina.toString(),
                    imagenUrl = args.url
                ))
            }
        }
    }

    private fun comprobarCampos(): Boolean {
        val miString = "\"\""
        var nombre = binding.etNombreAnadirRutina.text.toString()
        var descripcion = binding.etDescripcionAnadirRutina.text.toString()
        var ejercicios = binding.etNumeroEjercicios.text.toString()
        var genero = binding.etGeneroAnadirRutina.text.toString()
        var objetivo = binding.etObjetivoAnadirRutina.text.toString()
        var lugar = binding.etLugarAnadirRutina.text.toString()
        val errores = mutableListOf<String>()

        // Verificación de que query no sea igual a miString
        if (args.url.equals(miString)) {
            errores.add("El campo query no puede ser igual a $miString")
        }

        // Verificación de que los demás campos no estén vacíos
        if (nombre.isEmpty()) {
            errores.add("El campo nombre no puede estar vacío")
        }
        if (descripcion.isEmpty()) {
            errores.add("El campo descripción no puede estar vacío")
        }
        if (ejercicios.isEmpty()) {
            errores.add("El campo ejercicios no puede estar vacío")
        }
        if (genero.isEmpty() || (genero != "M" && genero != "H")) {
            errores.add("El campo género solo puede ser M o H")
        }
        if (objetivo.isEmpty() || (objetivo != "GRASA" && objetivo != "MUSCULO")) {
            errores.add("El campo objetivo solo puede ser GRASA o MUSCULO")
        }
        if (lugar.isEmpty() || (lugar != "C" && lugar != "G")) {
            errores.add("El campo lugar solo puede ser C o G")
        }
        if (ejercicios.toIntOrNull() ?: 0 > 99) {
            errores.add("El campo ejercicios no puede ser mayor a 99")
        }

        Log.d("erroresListaVer",errores.toString())

        // Si hay errores
        if (errores.isNotEmpty()) {
            for (error in errores) {
                when {
                    error.contains("query") -> {
                        binding.etQuery.error = error
                    }
                    error.contains("nombre") -> {
                        binding.etNombreAnadirRutina.error = error
                    }
                    error.contains("descripción") -> {
                        binding.etDescripcionAnadirRutina.error = error
                    }
                    error.contains("ejercicios") -> {
                        binding.etNumeroEjercicios.error = error
                    }
                    error.contains("género") -> {
                        binding.etGeneroAnadirRutina.error = error
                    }
                    error.contains("objetivo") -> {
                        binding.etObjetivoAnadirRutina.error = error
                    }
                    error.contains("lugar") -> {
                        binding.etLugarAnadirRutina.error = error
                    }
                    // Añade más casos según necesites para otros campos
                }
            }
            return false
        }

        return true
    }

    private fun comprobarImagen() {
        //Como por defecto nos saca "" si no devuelve nada pero a un string no le puedes meter "" se le mete \" y comprobamos si tiene una imagen
        val miString = "\"\""
        Log.d("InfoImagen", args.url)
        if(!args.url.equals(miString)){
            deUrlAImageView(args.url,binding.ivImageSelected)
            binding.ivImageSelected.visibility = View.VISIBLE
        }
    }
    fun deUrlAImageView(url:String, imageView: ImageView){
        Picasso.get().load(url).into(imageView)
    }
    private fun recogerImagen() {
        binding.btnImageSubir.setOnClickListener {
            var query = ""
        if(!binding.etQuery.text.toString().isEmpty()){
            query = binding.etQuery.text.toString()
        }
            findNavController().navigate(AnadirRutinaFragmentDirections.actionAnadirRutinaFragmentToSeleccionarImagenFragment(
                query = query
            ))
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}