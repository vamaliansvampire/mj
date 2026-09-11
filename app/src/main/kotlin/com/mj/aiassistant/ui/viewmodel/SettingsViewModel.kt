package com.mj.aiassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mj.aiassistant.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _userName = MutableStateFlow("User")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _voiceEnabled = MutableStateFlow(true)
    val voiceEnabled: StateFlow<Boolean> = _voiceEnabled.asStateFlow()

    private val _speechRate = MutableStateFlow(1.0f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _theme = MutableStateFlow("dark")
    val theme: StateFlow<String> = _theme.asStateFlow()

    private val _memoryEnabled = MutableStateFlow(true)
    val memoryEnabled: StateFlow<Boolean> = _memoryEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.openAIApiKey.collect { key ->
                _apiKey.value = key
            }
        }
        viewModelScope.launch {
            preferencesRepository.userName.collect { name ->
                _userName.value = name
            }
        }
        viewModelScope.launch {
            preferencesRepository.voiceEnabled.collect { enabled ->
                _voiceEnabled.value = enabled.toBoolean()
            }
        }
        viewModelScope.launch {
            preferencesRepository.speechRate.collect { rate ->
                _speechRate.value = rate.toFloatOrNull() ?: 1.0f
            }
        }
        viewModelScope.launch {
            preferencesRepository.theme.collect { t ->
                _theme.value = t
            }
        }
        viewModelScope.launch {
            preferencesRepository.memoryEnabled.collect { enabled ->
                _memoryEnabled.value = enabled.toBoolean()
            }
        }
    }

    fun setApiKey(key: String) {
        viewModelScope.launch {
            preferencesRepository.setOpenAIApiKey(key)
        }
    }

    fun setUserName(name: String) {
        viewModelScope.launch {
            preferencesRepository.setUserName(name)
        }
    }

    fun setVoiceEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setVoiceEnabled(enabled)
        }
    }

    fun setSpeechRate(rate: Float) {
        viewModelScope.launch {
            preferencesRepository.setSpeechRate(rate)
        }
    }

    fun setTheme(t: String) {
        viewModelScope.launch {
            preferencesRepository.setTheme(t)
        }
    }

    fun setMemoryEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setMemoryEnabled(enabled)
        }
    }
}
