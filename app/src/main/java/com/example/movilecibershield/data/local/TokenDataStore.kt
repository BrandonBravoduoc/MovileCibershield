package com.example.movilecibershield.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_prefs"
)

class TokenDataStore(context: Context) {

    private val dataStore = context.dataStore
    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    val tokenFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[TOKEN_KEY] }

    suspend fun saveToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }

    suspend fun getTokenOnce(): String? {
        return dataStore.data.first()[TOKEN_KEY]
    }
}



