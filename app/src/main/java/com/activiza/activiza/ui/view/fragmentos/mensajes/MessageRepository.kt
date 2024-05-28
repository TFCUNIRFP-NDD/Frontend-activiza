package com.activiza.activiza.ui.view.fragmentos.mensajes

import android.content.Context
import android.util.Log
import com.activiza.activiza.data.Message
import com.activiza.activiza.data.RetrofitInstance
import com.activiza.activiza.domain.ActivizaDataBaseHelper

class MessageRepository(context: Context) {
    private val api = RetrofitInstance.api
    private val dbHelper = ActivizaDataBaseHelper(context)

    suspend fun getMessages(): List<Message> {
        val token = "Token "+dbHelper.obtenerToken()
        return api.getMessages(token)
    }

    suspend fun sendMessage(message: Message): Message? {
        if(message.media.isNullOrEmpty()){
            message.media = "none"
        }
        if(message.titulo.isNullOrEmpty()){
            message.titulo = "none"
        }
        message.autor = dbHelper.getUsuario()!!.nombre
        val token = "Token ${dbHelper.obtenerToken()}"
        val response = api.sendMessage(token, message)
        Log.d("publicacionInfo", message.toString())
        return if (response.isSuccessful) response.body() else null
    }
}