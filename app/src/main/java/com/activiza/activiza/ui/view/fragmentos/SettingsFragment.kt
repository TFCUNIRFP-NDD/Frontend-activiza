package com.activiza.activiza.ui.view.fragmentos

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.activiza.activiza.R
import com.activiza.activiza.data.UserPreferences
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.databinding.FragmentSettingsBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.view.HomeActivity
import com.activiza.activiza.ui.view.login.LoginActivity
import com.activiza.activiza.ui.view.splash.SplashActivity
import com.activiza.activiza.ui.viewmodel.NotificationReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    lateinit var db: ActivizaDataBaseHelper
    lateinit var notificationReceiver: SplashActivity
    private lateinit var userPreferences: UserPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ActivizaDataBaseHelper(context)
        userPreferences = (context as HomeActivity).userPreferences

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val rootView = binding.root


//        val token = db.obtenerToken()
        val usuarioData: UsuarioData? = db.getUsuario()
        val token = usuarioData?.token
        userPreferences = UserPreferences(requireContext(), token ?: "")

        initUI()
        return rootView
    }

    private fun initUI() {
        binding.switchDarkMode.isChecked = userPreferences.darkModeEnabled
        binding.switchNotification.isChecked = userPreferences.notificationsEnabled
        binding.switchVibration.isChecked = userPreferences.vibrationEnabled
        binding.rsVolumen.setValues(userPreferences.volume.toFloat())

        // Configurar listeners para los cambios en la configuración
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            userPreferences.darkModeEnabled = isChecked

        }

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            userPreferences.notificationsEnabled = isChecked

        }

        binding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            userPreferences.vibrationEnabled = isChecked

        }

        binding.rsVolumen.addOnChangeListener { _, value, _ ->

            // Obtiene el valor del volumen como un porcentaje (entre 0 y 100)
            val volumePercentage = value.toInt()
            // Calcula el valor del volumen basado en el porcentaje
            val maxVolume =
                (requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager).getStreamMaxVolume(
                    AudioManager.STREAM_MUSIC
                )
            val volume = (maxVolume * volumePercentage) / 100
            // Establece el volumen del sistema
            val audioManager =
                requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)

        }


        binding.tvEliminarCuenta.setOnClickListener {
            val usuarioData: UsuarioData? = db.getUsuario()

            if (usuarioData != null) {
                val token = usuarioData.token
                db.borrarUsuario(token)
                clearPreferences(requireContext(), token)
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {

            }
        }

        binding.tvCerrarSesion.setOnClickListener {
            val usuarioData: UsuarioData? = db.getUsuario()
            val token = usuarioData?.token
            db.borrarUsuario(token!!)
            clearPreferences(requireContext(), token.orEmpty())
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }

        binding.tvPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_perfilFragment)
        }
        binding.tvMostrarQR.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_qrFragment)
        }

    }


    fun clearPreferences(context: Context, token: String) {
        val sharedPreferences =
            context.getSharedPreferences("user_preferences_$token", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putString("token", token)
        editor.apply()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}




