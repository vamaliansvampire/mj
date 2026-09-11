package com.mj.aiassistant.data.model

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<ChatMessage>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 1000
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatResponse(
    val id: String,
    val choices: List<ChatChoice>
)

data class ChatChoice(
    val message: ChatMessage,
    val finish_reason: String
)
