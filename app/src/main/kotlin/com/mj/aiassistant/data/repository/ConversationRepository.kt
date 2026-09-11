package com.mj.aiassistant.data.repository

import android.content.Context
import com.google.gson.Gson
import com.mj.aiassistant.data.model.Message
import com.mj.aiassistant.data.model.MessageSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepository @Inject constructor(private val context: Context) {
    private val gson = Gson()
    private val conversationFile = File(context.filesDir, "conversation.json")
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    init {
        loadConversation()
    }

    private fun loadConversation() {
        if (conversationFile.exists()) {
            try {
                val json = conversationFile.readText()
                val messages = gson.fromJson(json, Array<Message>::class.java).toList()
                _messages.value = messages
            } catch (e: Exception) {
                _messages.value = emptyList()
            }
        }
    }

    suspend fun addMessage(message: Message) {
        withContext(Dispatchers.IO) {
            val updated = _messages.value + message
            _messages.value = updated
            saveConversation(updated)
        }
    }

    suspend fun clearConversation() {
        withContext(Dispatchers.IO) {
            _messages.value = emptyList()
            conversationFile.delete()
        }
    }

    private suspend fun saveConversation(messages: List<Message>) {
        withContext(Dispatchers.IO) {
            try {
                val json = gson.toJson(messages)
                conversationFile.writeText(json)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getConversationHistory(): List<com.mj.aiassistant.data.model.ChatMessage> {
        return _messages.value
            .filter { it.sender != MessageSender.SYSTEM }
            .map { message ->
                com.mj.aiassistant.data.model.ChatMessage(
                    role = when (message.sender) {
                        MessageSender.USER -> "user"
                        MessageSender.MJ -> "assistant"
                        MessageSender.SYSTEM -> "system"
                    },
                    content = message.text
                )
            }
    }
}
