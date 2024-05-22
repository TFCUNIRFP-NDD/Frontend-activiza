package com.activiza.activiza.ui.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.activiza.activiza.data.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class QrFragmentModel {
    suspend fun downloadQrCode(token: String): Bitmap? {
        return try {
            val ApiClient = RetrofitInstance.api
            val responseBody = ApiClient.getQrCode("Token $token")
            responseBodyToBitmap(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun responseBodyToBitmap(body: ResponseBody): Bitmap? {
        return withContext(Dispatchers.IO) {
            BitmapFactory.decodeStream(body.byteStream())
        }
    }
}