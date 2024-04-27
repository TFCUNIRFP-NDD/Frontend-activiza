package com.activiza.activiza.ui.view.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activiza.activiza.R
import com.activiza.activiza.databinding.FragmentEntrenamientosBinding
import com.activiza.activiza.databinding.FragmentRutinaIdBinding


class RutinaIDFragment : Fragment() {

    private var _binding: FragmentRutinaIdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRutinaIdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        // Aquí puedes inicializar la interfaz de usuario del fragmento si es necesario
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}