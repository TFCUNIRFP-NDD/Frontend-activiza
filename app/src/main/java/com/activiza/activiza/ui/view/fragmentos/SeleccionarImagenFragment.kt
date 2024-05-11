package com.activiza.activiza.ui.view.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.activiza.activiza.databinding.FragmentSeleccionarImagenBinding
import com.activiza.activiza.domain.APIListener
import com.activiza.activiza.ui.viewmodel.ImagenesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SeleccionarImagenFragment : Fragment() {
    private var _binding: FragmentSeleccionarImagenBinding? = null
    private val binding get() = _binding!!
    val args:SeleccionarImagenFragmentArgs by navArgs()
    companion object {
        const val TOKEN_API_PEXELS = "tkGjeRqPu7jbvTV1X9TMchp1URNxJ0JiIsqxbqBqduu42YO2qM6HqSc2"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSeleccionarImagenBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initUI()
        return rootView
    }

    private fun initUI() {
        anadirDatosRetrofit()
    }
    private fun anadirDatosRetrofit() {
        val binding = _binding ?: return  // Verify if _binding is null and exit the function if it is

        // Start the progressBar until the data loads
        binding.pbProgressBarImagenes.visibility = View.VISIBLE

        // Use the fragment's lifecycle scope to handle coroutines
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Retrieve token from SQLite
                val token = TOKEN_API_PEXELS
                Log.d("infoQueryImagen",args.query)
                // Make the HTTP request on the background thread
                val call = withContext(Dispatchers.IO) {
                    getRetrofit().create(APIListener::class.java)
                        .getTodasImagenes("$token", args.query)
                        .execute()
                }

                // Check if the response is not null
                val response = call.body()
                if (response != null) {
                    // Extract the list of photos from the response
                    val photos = response.photos
                    // Configure the RecyclerView on the main thread
                    binding.rvImagenes.adapter = ImagenesAdapter(photos) { fotoUrl ->
                        findNavController().navigate(SeleccionarImagenFragmentDirections.actionSeleccionarImagenFragmentToAnadirRutinaFragment(
                            url = fotoUrl
                        ))
                    }
                    binding.rvImagenes.layoutManager = LinearLayoutManager(requireContext())
                } else {
                    // Handle the case where the response is null
                    Log.e("Error", "The Retrofit call response is null")
                }
            } catch (e: IOException) {
                // Handle the error while making the HTTP request
                Log.e("Error", "Error making HTTP request: ${e.message}")
                // Retry the HTTP request in case of failure
                anadirDatosRetrofit()
            } finally {
                // Hide the progressBar after loading the data or handling the error
                binding.pbProgressBarImagenes.visibility = View.GONE
            }
        }
    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}