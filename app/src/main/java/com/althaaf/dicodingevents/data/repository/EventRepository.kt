package com.althaaf.dicodingevents.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.althaaf.dicodingevents.data.datastore.UserPreferences
import com.althaaf.dicodingevents.data.local.EventDao
import com.althaaf.dicodingevents.data.local.EventEntity
import com.althaaf.dicodingevents.data.model.ApiResult
import com.althaaf.dicodingevents.data.response.ListEventsItem
import com.althaaf.dicodingevents.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class EventRepository(private val apiService: ApiService, private val dataStore: UserPreferences, private val eventDao: EventDao) {

    fun getListEvents(active: Int, limit: Int?, query: String?) : LiveData<ApiResult<List<ListEventsItem>>> {
        return liveData {
            emit(ApiResult.Loading)
            val response = apiService.getListEventsV2(active = active, limit = limit, q = query)
            val responseBody = response.listEvents

            if(responseBody != null && !response.error) {
                if(responseBody.isEmpty()) emit(ApiResult.Empty) else emit(ApiResult.Success(responseBody))
            } else {
                emit(ApiResult.Error(response.message))
            }
        }
    }

    fun getFavoriteEvent(): LiveData<List<EventEntity>> {
        return eventDao.getEventFavorite()
    }

    fun checkEventFavorite(id: Int): LiveData<Boolean> {
        Log.d("EventRepository", "ID: $id")
        return eventDao.getEventById(id)
    }

    suspend fun setFavoriteEvent(eventEntity: EventEntity) {
        eventDao.addEventFavorite(eventEntity)
    }

    suspend fun deleteFavoriteEvent(eventEntity: EventEntity) {
        eventDao.deleteEventFavorite(eventEntity)
    }

    fun getDarkModeSetting(): Flow<Boolean> {
        return dataStore.getThemeSetting()
    }

    suspend fun setDarkModeSetting(isDarkMode: Boolean) {
        dataStore.saveThemeSetting(isDarkMode)
    }

    fun getNotificationSetting(): Flow<Boolean> {
        return dataStore.getNotificationSetting()
    }

    suspend fun setNotificationSetting(isEnable: Boolean) {
        dataStore.saveNotificationSetting(isEnable)
    }

    fun getPeriodicKey(): Flow<String> {
        return dataStore.getPeriodicKey()
    }

    suspend fun savePeriodicKey(id: String) {
        dataStore.savePeriodicKey(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: EventRepository? = null

        @JvmStatic
        fun getInstanceRepository(apiService: ApiService, dataStore: UserPreferences, eventDao: EventDao): EventRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = EventRepository(apiService, dataStore, eventDao)
                INSTANCE = instance
                instance
            }
        }
    }
}