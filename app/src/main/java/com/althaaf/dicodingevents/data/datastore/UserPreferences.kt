package com.althaaf.dicodingevents.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences private constructor(val dataStore: DataStore<Preferences>) {

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(switchValue: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = switchValue
        }
    }

    fun getNotificationSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[NOTIFICATION_KEY] ?: false
        }
    }

    suspend fun saveNotificationSetting(switchValue: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = switchValue
        }
    }

    fun getPeriodicKey(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PERIODIC_TASK_ID] ?: ""
        }
    }

    suspend fun savePeriodicKey(switchValue: String) {
        dataStore.edit { preferences ->
            preferences[PERIODIC_TASK_ID] = switchValue
        }
    }



    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_key")
        private val NOTIFICATION_KEY = booleanPreferencesKey("notification_key")
        private val PERIODIC_TASK_ID = stringPreferencesKey("periodic_key")

        @Volatile
        private var INSTANCE: UserPreferences? = null

        @JvmStatic
        fun getInstanceDataStore(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }

        }
    }
}