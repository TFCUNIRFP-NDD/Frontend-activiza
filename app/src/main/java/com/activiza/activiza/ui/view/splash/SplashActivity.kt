package com.activiza.activiza.ui.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.activiza.activiza.R
import com.activiza.activiza.ui.view.OnboardingActivity
import com.activiza.activiza.ui.view.login.LoginActivity
import com.activiza.activiza.ui.view.login.RegisterActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            // Este código se ejecutará después de 2 segundos
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 2000 milisegundos = 2 segundos

    }
}