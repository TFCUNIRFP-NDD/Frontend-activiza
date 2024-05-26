package com.activiza.activiza.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    var notificationsEnabled: Boolean
        get() = sharedPreferences.getBoolean("notifications", true)
        set(value) = sharedPreferences.edit().putBoolean("notifications", value).apply()

    var darkModeEnabled: Boolean
        get() = sharedPreferences.getBoolean("dark_mode", false)
        set(value) = sharedPreferences.edit().putBoolean("dark_mode", value).apply()

    var vibrationEnabled: Boolean        get() = sharedPreferences.getBoolean("vibration", true)
        set(value) = sharedPreferences.edit().putBoolean("vibration", value).apply()

    var volume: Int
        get() = sharedPreferences.getInt("volume", 50) // Valor por defecto a la mitad
        set(value) = sharedPreferences.edit().putInt("volume", value).apply()

}

