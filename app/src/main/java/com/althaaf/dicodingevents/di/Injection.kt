package com.althaaf.dicodingevents.di

import android.content.Context
import com.althaaf.dicodingevents.data.datastore.UserPreferences
import com.althaaf.dicodingevents.data.datastore.dataStore
import com.althaaf.dicodingevents.data.local.EventRoomDatabase
import com.althaaf.dicodingevents.data.repository.EventRepository
import com.althaaf.dicodingevents.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreferences.getInstanceDataStore(context.dataStore)
        val database = EventRoomDatabase.getInstanceDatabase(context.applicationContext)
        val eventDao = database.eventDao()
        return EventRepository.getInstanceRepository(apiService, dataStore, eventDao)
    }
}