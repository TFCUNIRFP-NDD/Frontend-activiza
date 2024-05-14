package com.activiza.activiza.ui.permissions


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager(private val activity: AppCompatActivity) {

     val PERMISSION_REQUEST_CODE = 100

    fun requestStoragePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(

                activity,

                arrayOf(Manifest.permission.POST_NOTIFICATIONS),

                PERMISSION_REQUEST_CODE

            )

        }

    }

    fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isPostNotificationPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }

        return true // Si el SDK es inferior a Android 13, asumimos que el permiso está concedido

    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido
                    // Puedes implementar alguna lógica adicional aquí o notificar a la actividad
                } else {
                    // Permiso denegado
                    // Puedes implementar alguna lógica adicional aquí o notificar a la actividad
                }
            }
        }
    }
}
