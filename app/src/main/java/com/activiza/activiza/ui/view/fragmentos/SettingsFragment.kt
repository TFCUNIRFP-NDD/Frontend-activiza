package com.activiza.activiza.ui.view.fragmentos


import android.content.Context
import com.google.android.material.slider.RangeSlider
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.activiza.activiza.R
import com.activiza.activiza.data.UserPreferences
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.databinding.FragmentSettingsBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.view.login.LoginActivity


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    lateinit var db: ActivizaDataBaseHelper
    private lateinit var userPreferences: UserPreferences
    private var mediaPlayer: MediaPlayer? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ActivizaDataBaseHelper(context)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val rootView = binding.root

        userPreferences = UserPreferences(requireContext())

        // Obtener el estado guardado del modo oscuro desde las preferencias compartidas
        val isDarkModeEnabled = userPreferences.darkModeEnabled

        // Aplicar el estado del modo oscuro al switch
        binding.switchDarkMode.isChecked = isDarkModeEnabled

        // Aplicar el tema oscuro según el estado guardado
        if (isDarkModeEnabled) {
            enableDarkMode()
        } else {
            disableDarkMode()
        }

// Establece el color de los iconos en funcion del modo oscuro
        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.ivDelete.setImageResource(R.drawable.ic_delete_white)
                binding.ivDarkMode.setImageResource(R.drawable.ic_dark_mode_white)
                binding.ivQr.setImageResource(R.drawable.ic_qr_white)
                binding.ivUser.setImageResource(R.drawable.ic_user_perfil_white)
                binding.ivNotification.setImageResource(R.drawable.ic_notifications_white)
                binding.ivVolumen.setImageResource(R.drawable.ic_volume_white)


            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.ivDelete.setImageResource(R.drawable.ic_delete)
                binding.ivDarkMode.setImageResource(R.drawable.ic_dark_mode)
                binding.ivQr.setImageResource(R.drawable.ic_qr)
                binding.ivUser.setImageResource(R.drawable.ic_user_perfil)
                binding.ivNotification.setImageResource(R.drawable.ic_notifications)
                binding.ivVolumen.setImageResource(R.drawable.ic_volume)
            }
        }

        initUI()
        return rootView
    }

    private fun initUI() {
        binding.switchDarkMode.isChecked = userPreferences.darkModeEnabled
        binding.switchNotification.isChecked = userPreferences.notificationsEnabled
        binding.rsVolumen.setValues(userPreferences.volume.toFloat())

        val clickSound = MediaPlayer.create(requireContext(), R.raw.switch2)


        // Configurar listeners para los cambios en la configuración
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            userPreferences.darkModeEnabled = isChecked
            if(isChecked){
                enableDarkMode()
            }else{
                disableDarkMode()
            }

            clickSound.start()

        }

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            userPreferences.notificationsEnabled = isChecked

            clickSound.start()

        }


        binding.rsVolumen.addOnChangeListener { _, value, _ ->
            setSystemVolume(value.toInt())

        }

        binding.rsVolumen.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                //Cuando empiece a mover el volumen cambiar valores
                binding.rsVolumen.addOnChangeListener { slider, value, fromUser ->
                    userPreferences.volume = value.toInt()
                }
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Reproduce un sonido de prueba cuando se suelta el slider
                playTestSound()
            }
        })

        binding.tvEliminarCuenta.setOnClickListener {
            val usuarioData: UsuarioData? = db.getUsuario()
            if (usuarioData != null) {
                val token = usuarioData.token
                db.borrarUsuario(token)
                clearPreferences(requireContext())
                // Desactiva el modo oscuro
                disableDarkMode()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {

            }
        }

        binding.tvPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_perfilFragment)
        }
        binding.tvMostrarQR.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_qrFragment)
        }

    }

    private fun enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)



    }

    private fun disableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    }


    private fun setSystemVolume(volumePercentage: Int) {
        val audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volume = (1 + (Math.log(volumePercentage.toDouble()) / Math.log(100.0)) * (maxVolume - 1)).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    }


    private fun playTestSound() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.test_sound)
        val audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volume = currentVolume / maxVolume.toFloat()
        mediaPlayer?.setVolume(volume, volume)
        mediaPlayer?.start()
    }



    fun clearPreferences(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}




