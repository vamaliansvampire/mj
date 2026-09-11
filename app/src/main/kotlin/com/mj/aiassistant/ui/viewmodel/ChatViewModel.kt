package com.mj.aiassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mj.aiassistant.data.model.AssistantState
import com.mj.aiassistant.data.model.Message
import com.mj.aiassistant.data.model.MessageSender
import com.mj.aiassistant.data.repository.AiRepository
import com.mj.aiassistant.data.repository.ConversationRepository
import com.mj.aiassistant.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val aiRepository: AiRepository,
    private val conversationRepository: ConversationRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _assistantState = MutableStateFlow(AssistantState.IDLE)
    val assistantState: StateFlow<AssistantState> = _assistantState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            conversationRepository.messages.collect { messages ->
                _messages.value = messages
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _assistantState.value = AssistantState.THINKING

                // Add user message
                val userMessage = Message(
                    id = UUID.randomUUID().toString(),
                    text = text,
                    sender = MessageSender.USER,
                    timestamp = LocalDateTime.now()
                )
                conversationRepository.addMessage(userMessage)

                // Get API key
                val apiKey = preferencesRepository.openAIApiKey.first()

                // Get response from AI
                val response = aiRepository.sendMessage(
                    userMessage = text,
                    apiKey = apiKey,
                    conversationHistory = conversationRepository.getConversationHistory()
                )

                // Add assistant message
                val assistantMessage = Message(
                    id = UUID.randomUUID().toString(),
                    text = response,
                    sender = MessageSender.MJ,
                    timestamp = LocalDateTime.now()
                )
                conversationRepository.addMessage(assistantMessage)

                _assistantState.value = AssistantState.IDLE
            } catch (e: Exception) {
                val errorMessage = Message(
                    id = UUID.randomUUID().toString(),
                    text = "Error: ${e.message ?: "Unknown error"}",
                    sender = MessageSender.MJ,
                    timestamp = LocalDateTime.now(),
                    error = e.message
                )
                conversationRepository.addMessage(errorMessage)
                _assistantState.value = AssistantState.OFFLINE
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearConversation() {
        viewModelScope.launch {
            conversationRepository.clearConversation()
            _messages.value = emptyList()
            _assistantState.value = AssistantState.IDLE
        }
    }

    fun setAssistantState(state: AssistantState) {
        _assistantState.value = state
    }
}
