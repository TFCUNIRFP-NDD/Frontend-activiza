package com.activiza.activiza.ui.view.fragmentos

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activiza.activiza.R
import com.activiza.activiza.databinding.FragmentSettingsBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
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



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initUI()
        return  rootView
    }

    private fun initUI() {
        db = ActivizaDataBaseHelper(requireContext())
        val token = db.obtenerToken()

        binding.rsVolumen.addOnChangeListener { _, value, _ ->
            ajustarVolumen(requireContext(), value.toInt())

            val resultado = db.actualizarVolumenUsuario(token, value.toInt())
            if (resultado) {
                // El volumen se actualizó correctamente en la base de datos y el sistema
            } else {
                // Hubo un error al actualizar el volumen
            }
        }

        binding.tvEliminarCuenta.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                eliminarCuenta(token)
            }
        }

        binding.tvCerrarSesion.setOnClickListener {
            cerrarSesion(token)
        }

        binding.switchDarkMode.setOnClickListener {
            saveOptions("darkMode", binding.switchDarkMode.isChecked)
        }

        binding.switchNotification.setOnClickListener {
            if(binding.switchNotification.isChecked) {
                saveOptions("notifications", binding.switchNotification.isChecked)
                cancelarProgramacionNotificacion()
            }else{
                saveOptions("notifications", !binding.switchNotification.isChecked)
                val splashActivity = activity as? SplashActivity
                splashActivity?.programarNotificacionDiaria()

            }

        }

        binding.switchVibration.setOnClickListener {
            saveOptions("vibration", binding.switchVibration.isChecked)
        }
    }

    private fun saveOptions(optionName: String, isChecked: Boolean) {
        val token = db.obtenerToken()
        if (token.isNotEmpty()) {
            val userSettings = db.getSettings(token)
            if (userSettings != null) {
                // Actualizar la configuración según el nombre de la opción
                val updatedSettings = when (optionName) {
                    "darkMode" -> userSettings.copy(darkMode = !isChecked)
                    "notifications" -> userSettings.copy(notifications = isChecked)
                    "vibration" -> userSettings.copy(vibration = isChecked)
                    else -> userSettings // No se realiza ninguna actualización
                }
                // Guardar la configuración actualizada en la base de datos
                db.actualizarSettings(token, updatedSettings)


            }
        }
    }

    fun ajustarVolumen(context: Context, nuevoVolumen: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volumenCalculado = (nuevoVolumen / 100.0 * maxVolume).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumenCalculado, 0)
    }

    private  fun eliminarCuenta(token: String) {
        val token = db.obtenerToken()
        CoroutineScope(Dispatchers.IO).launch {
            db.eliminarConfiguracionUsuario(token)
            db.eliminarUsuario(token)
        }
    }

    private fun cerrarSesion(token: String){

    }

    private fun cancelarProgramacionNotificacion() {
        val intent = Intent(activity, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT?:0)
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.cancel(pendingIntent)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}