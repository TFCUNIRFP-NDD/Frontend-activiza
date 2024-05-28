package com.activiza.activiza.ui.view.fragmentos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.activiza.activiza.R
import com.activiza.activiza.data.RetrofitInstance
import com.activiza.activiza.databinding.FragmentQrBinding
import com.activiza.activiza.databinding.FragmentSettingsBinding
import com.activiza.activiza.domain.APIListener
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.viewmodel.QrFragmentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody


class QrFragment : Fragment() {

    private lateinit var db: ActivizaDataBaseHelper
    private var _binding: FragmentQrBinding? = null
    private val binding get() = _binding!!
    private val qrFragmentModel: QrFragmentModel = QrFragmentModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrBinding.inflate(inflater, container, false)
        val rootView = binding.root
        db = ActivizaDataBaseHelper(binding.tvQr.context)
        val imageViewQR = binding.tvQr

        // Llamar a la función downloadQrCode en una coroutine
        lifecycleScope.launch {
            val bitmap = qrFragmentModel.downloadQrCode(db.obtenerToken())
            bitmap?.let {
                imageViewQR.setImageBitmap(it)
            }
        }

        return rootView
    }
}