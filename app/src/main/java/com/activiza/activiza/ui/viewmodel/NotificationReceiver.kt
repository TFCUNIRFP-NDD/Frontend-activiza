package com.activiza.activiza.ui.viewmodel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.activiza.activiza.R
import com.activiza.activiza.data.UserPreferences
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.view.splash.SplashActivity

class NotificationReceiver : BroadcastReceiver(){

    private lateinit var userPreferences: UserPreferences
    lateinit var db: ActivizaDataBaseHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        // Verificar si el contexto es nulo
        if (context == null) {
            return
        }
        // Inicializa la base de datos
        if (!::db.isInitialized) {
            db = ActivizaDataBaseHelper(context)
        }

        // Obtiene el token del usuario
        val usuarioData: UsuarioData? = db.getUsuario()
        val token = usuarioData?.token
        // Inicializa las preferencias del usuario con el token obtenido
        val userPreferences = UserPreferences(context)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, "default")
            .setContentTitle("¡Notificación diaria!")
            .setContentText("Este es tu recordatorio de ejercicio diario.")
            .setSmallIcon(R.drawable.ic_weight)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Daily Notification", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notification)
    }
}