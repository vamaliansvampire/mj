package com.mj.aiassistant.data.model

import java.time.LocalDateTime

data class Message(
    val id: String = "",
    val text: String,
    val sender: MessageSender,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class MessageSender {
    USER, MJ, SYSTEM
}
