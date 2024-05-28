package com.activiza.activiza.ui.viewmodel

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.activiza.activiza.R
import com.activiza.activiza.data.Message
import com.activiza.activiza.ui.view.fragmentos.mensajes.MessageRepository
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MessageRepository(application)

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        fetchMessages()
    }

    fun fetchMessages() {
        viewModelScope.launch {
            try {
                val messages = repository.getMessages()
                _messages.value = messages
            } catch (e: Exception) {
                _error.value = "Error fetching messages"
            }
        }
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            try {
                val sentMessage = repository.sendMessage(message)
                sentMessage?.let {
                    _messages.value = _messages.value.orEmpty() + it
                    playSendMessageSound()
                }
            } catch (e: Exception) {
                _error.value = "Error sending message"
            }
        }
    }

    private fun playSendMessageSound() {
        // Aquí puedes reproducir el sonido que desees
        // Por ejemplo:
        val mediaPlayer = MediaPlayer.create(getApplication(), R.raw.mensaje)
        mediaPlayer.start()
    }
}