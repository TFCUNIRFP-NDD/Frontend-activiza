package com.activiza.activiza.ui.view.splash


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.activiza.activiza.R
import com.activiza.activiza.data.AuthData
import com.activiza.activiza.data.TokenResponse
import com.activiza.activiza.data.UserPreferences
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.databinding.FragmentSettingsBinding
import com.activiza.activiza.domain.APIListener
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.activiza.activiza.ui.view.OnboardingActivity
import com.activiza.activiza.ui.view.login.LoginActivity
import com.activiza.activiza.ui.viewmodel.NotificationReceiver
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class SplashActivity : AppCompatActivity() {
    lateinit var db: ActivizaDataBaseHelper
    private lateinit var apiService: APIListener
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userPreferences: UserPreferences
    companion object{
        const val HOUR_OF_DAY = 9
        const val MINUTE = 30
        const val PREF_LAST_NOTIFICATION_DATE = "last_notification_date"
        const val CHANNEL_ID ="canal_notificaciones"
        const val NOTIFICATION_ID = 1

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        db = ActivizaDataBaseHelper(this)
        userPreferences = UserPreferences(this)

        // Configuración de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.163.215.184/activiza/") // Reemplaza con la URL base de tu API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(APIListener::class.java)

        comprobarDatos()



        // Programar la notificación diaria si aún no se ha enviado hoy
        if (userPreferences.notificationsEnabled && !isNotificationAlreadySentToday() && db.obtenerPrimeraRutina() != null) {
            programarNotificacionDiaria()
        }

        // Comprobar si el modo oscuro está activado y cambiar el tema en consecuencia
        val isDarkModeEnabled = userPreferences.darkModeEnabled
        if (isDarkModeEnabled) {
            enableDarkMode()
        } else {
            disableDarkMode()
        }

        //Establecemos la imagen y las letras en funcion del modo oscuro
        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = nightMode == Configuration.UI_MODE_NIGHT_YES

        val imageResourceId = if (isNightMode) R.drawable.img_run_white else R.drawable.img_run_svg
        val textColorResourceId = if (isNightMode) R.color.splash_background else R.color.blue_light


       val imagen = findViewById<ImageView>(R.id.ivRun)
        imagen.setImageResource(imageResourceId)

        val textView = findViewById<TextView>(R.id.tvName)
        textView.setTextColor(resources.getColor(textColorResourceId))
    }

     fun programarNotificacionDiaria() {
        // Programar la notificación diaria
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY)
            set(Calendar.MINUTE, MINUTE)
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(applicationContext, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        // Guardar la fecha de la última notificación enviada
        saveLastNotificationDate()
    }


    private fun isNotificationAlreadySentToday(): Boolean {
        // Obtener la fecha de la última notificación enviada
        val sharedPreferences = getSharedPreferences("notificacion_diaria", Context.MODE_PRIVATE)
        val lastNotificationDate = sharedPreferences.getLong(PREF_LAST_NOTIFICATION_DATE, 0)

        // Verificar si la última notificación fue enviada hoy
        val today = Calendar.getInstance()
        val lastNotification = Calendar.getInstance().apply { timeInMillis = lastNotificationDate }
        return today.get(Calendar.YEAR) == lastNotification.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == lastNotification.get(Calendar.DAY_OF_YEAR)
    }

    private fun saveLastNotificationDate() {
        // Guardar la fecha de la última notificación enviada como un valor long en SharedPreferences
        val sharedPreferences = getSharedPreferences("notificacion_diaria", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong(PREF_LAST_NOTIFICATION_DATE, System.currentTimeMillis())
        editor.apply()
    }

    private fun comprobarDatos() {
        val user: UsuarioData? = db.getUsuario()
        if (user != null) {
            val authData = AuthData(
                user.nombre,
                user.password
            )
            authenticateUser(authData)
        } else {
            // Si no hay usuario registrado, redirige a la LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun authenticateUser(authData: AuthData) {
        apiService.authenticate(authData).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    tokenResponse?.token?.let { token ->
                        // Guardamos el token en SharedPreferences
                        val editor = getSharedPreferences("login", Context.MODE_PRIVATE).edit()
                        editor.putString("token", token)
                        editor.apply()

                        // Navegamos a la siguiente actividad
                        val intent = Intent(this@SplashActivity, OnboardingActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Error de autenticación
                    Log.d("Error","No autentificado")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                // Error de red
                Log.d("Error","No hay conexion")
            }
        })
    }

    private fun enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
    private fun disableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
