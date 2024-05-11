package com.activiza.activiza.ui.view.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.activiza.activiza.R
import com.activiza.activiza.databinding.FragmentAnadirRutinaBinding
import com.activiza.activiza.databinding.FragmentEntrenamientosBinding
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
    }

    private fun recogerImagen() {
        binding.btnImageSubir.setOnClickListener {
            var query = ""
        if(!binding.etQuery.text.toString().isNullOrEmpty()){
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