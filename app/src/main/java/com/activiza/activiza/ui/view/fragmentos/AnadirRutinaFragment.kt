package com.activiza.activiza.ui.view.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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
    val args: AnadirRutinaFragmentArgs by navArgs()

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
            if (comprobarCampos()) {
                val numeroEjerciciosText = binding.etNumeroEjercicios.text.toString()
                val duracionText = binding.etDuracionAnadirRutina.text.toString()
                try {
                    val numeroEjercicios = numeroEjerciciosText.toInt()
                    val duracion = duracionText.toInt()

                    findNavController().navigate(
                        AnadirRutinaFragmentDirections.actionAnadirRutinaFragmentToAnadirEjerciciosFragment(
                            numEjercicios = numeroEjercicios,
                            nombre = binding.etNombreAnadirRutina.text.toString(),
                            descripcion = binding.etDescripcionAnadirRutina.text.toString(),
                            genero = binding.etGeneroAnadirRutina.text.toString(),
                            objetivo = binding.etObjetivoAnadirRutina.text.toString(),
                            lugar = binding.etLugarAnadirRutina.text.toString(),
                            imagenUrl = args.url,
                            duracion = duracion.toString()
                        )
                    )
                } catch (e: NumberFormatException) {
                    // Handle the exception (e.g., show an error message)
                    Toast.makeText(context, "Please enter valid numbers for exercises and duration", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun comprobarCampos(): Boolean {
        val miString = "\"\""
        val nombre = binding.etNombreAnadirRutina.text.toString()
        val descripcion = binding.etDescripcionAnadirRutina.text.toString()
        val ejercicios = binding.etNumeroEjercicios.text.toString()
        val genero = binding.etGeneroAnadirRutina.text.toString()
        val objetivo = binding.etObjetivoAnadirRutina.text.toString()
        val lugar = binding.etLugarAnadirRutina.text.toString()
        val duracion = binding.etDuracionAnadirRutina.text.toString()
        val errores = mutableListOf<String>()

        if (args.url == miString) {
            errores.add("El campo query no puede ser igual a $miString")
        }

        if (nombre.isEmpty()) {
            errores.add("El campo nombre no puede estar vacío")
        }
        if (descripcion.isEmpty()) {
            errores.add("El campo descripción no puede estar vacío")
        }
        if (ejercicios.isEmpty()) {
            errores.add("El campo ejercicios no puede estar vacío")
        } else {
            try {
                val ejerciciosInt = ejercicios.toInt()
                if (ejerciciosInt !in 1..99) {
                    errores.add("El campo ejercicios debe ser un número entre 1 y 99")
                }
            } catch (e: NumberFormatException) {
                errores.add("El campo ejercicios no es un número válido")
            }
        }
        if (duracion.isEmpty()) {
            errores.add("El campo duración no puede estar vacío")
        } else {
            try {
                val duracionInt = duracion.toInt()
                if (duracionInt !in 1..99) {
                    errores.add("El campo duración debe ser un número entre 1 y 99")
                }
            } catch (e: NumberFormatException) {
                errores.add("El campo duración no es un número válido")
            }
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
        Log.d("erroresListaVer", errores.toString())

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
                    error.contains("duración") -> {
                        binding.etDuracionAnadirRutina.error = error
                    }
                }
            }
            return false
        }
        return true
    }

    private fun comprobarImagen() {
        val miString = "\"\""
        Log.d("InfoImagen", args.url)
        if (args.url != miString) {
            deUrlAImageView(args.url, binding.ivImageSelected)
            binding.ivImageSelected.visibility = View.VISIBLE
        }
    }

    private fun recogerImagen() {
        binding.btnImageSubir.setOnClickListener {
            val query = binding.etQuery.text.toString()
            findNavController().navigate(
                AnadirRutinaFragmentDirections.actionAnadirRutinaFragmentToSeleccionarImagenFragment(
                    query = query
                )
            )
        }
    }

    fun deUrlAImageView(url: String, imageView: ImageView) {
        Picasso.get().load(url).into(imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
