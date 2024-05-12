package com.activiza.activiza.ui.view.fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.activiza.activiza.R
import com.activiza.activiza.databinding.FragmentAnadirEjerciciosBinding
import com.activiza.activiza.databinding.FragmentAnadirRutinaBinding

class AnadirEjerciciosFragment : Fragment() {

    private var _binding: FragmentAnadirEjerciciosBinding? = null
    private val binding get() = _binding!!
    val args:AnadirEjerciciosFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAnadirEjerciciosBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initUI()
        return rootView
    }

    private fun initUI() {
        sustituirElementos()
    }

    @SuppressLint("SetTextI18n")
    private fun sustituirElementos() {
        binding.tvEjerciciosSeleccionados.text = "0/${args.numEjercicios}"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}