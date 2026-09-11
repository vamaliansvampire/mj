package com.mj.aiassistant.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mj_preferences")

@Singleton
class PreferencesRepository @Inject constructor(private val context: Context) {
    private object PreferenceKeys {
        val OPENAI_API_KEY = stringPreferencesKey("openai_api_key")
        val USER_NAME = stringPreferencesKey("user_name")
        val VOICE_ENABLED = stringPreferencesKey("voice_enabled")
        val SPEECH_RATE = stringPreferencesKey("speech_rate")
        val THEME = stringPreferencesKey("theme")
        val MEMORY_ENABLED = stringPreferencesKey("memory_enabled")
    }

    val openAIApiKey: Flow<String> = context.dataStore.data
        .map { it[PreferenceKeys.OPENAI_API_KEY] ?: "" }

    val userName: Flow<String> = context.dataStore.data
        .map { it[PreferenceKeys.USER_NAME] ?: "User" }

    val voiceEnabled: Flow<String> = context.dataStore.data
        .map { it[PreferenceKeys.VOICE_ENABLED] ?: "true" }

    val speechRate: Flow<String> = context.dataStore.data
        .map { it[PreferenceKeys.SPEECH_RATE] ?: "1.0" }

    val theme: Flow<String> = context.dataStore.data
        .map { it[PreferenceKeys.THEME] ?: "dark" }

    val memoryEnabled: Flow<String> = context.dataStore.data
        .map { it[PreferenceKeys.MEMORY_ENABLED] ?: "true" }

    suspend fun setOpenAIApiKey(apiKey: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.OPENAI_API_KEY] = apiKey
        }
    }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_NAME] = name
        }
    }

    suspend fun setVoiceEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.VOICE_ENABLED] = enabled.toString()
        }
    }

    suspend fun setSpeechRate(rate: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.SPEECH_RATE] = rate.toString()
        }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME] = theme
        }
    }

    suspend fun setMemoryEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.MEMORY_ENABLED] = enabled.toString()
        }
    }
}
