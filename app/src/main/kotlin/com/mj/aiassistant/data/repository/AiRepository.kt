package com.mj.aiassistant.data.repository

import com.mj.aiassistant.data.model.ChatMessage
import com.mj.aiassistant.data.model.ChatRequest
import com.mj.aiassistant.data.remote.OpenAIService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepository @Inject constructor(
    private val openAIService: OpenAIService
) {
    suspend fun sendMessage(userMessage: String, apiKey: String, conversationHistory: List<ChatMessage>): String {
        return try {
            if (apiKey.isBlank()) {
                return "Error: No API key configured. Please set your OpenAI API key in Settings."
            }
            
            val messages = conversationHistory.toMutableList().apply {
                add(ChatMessage(role = "user", content = userMessage))
            }
            
            val request = ChatRequest(messages = messages)
            val response = openAIService.createChatCompletion(
                authorization = "Bearer $apiKey",
                request = request
            )
            
            response.choices.firstOrNull()?.message?.content 
                ?: "Error: No response from OpenAI"
        } catch (e: Exception) {
            "Error: ${e.message ?: "Unknown error occurred"}"
        }
    }
}
