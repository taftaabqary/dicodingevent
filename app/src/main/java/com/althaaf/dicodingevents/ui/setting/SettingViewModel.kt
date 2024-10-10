package com.althaaf.dicodingevents.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.althaaf.dicodingevents.data.repository.EventRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val eventRepository: EventRepository): ViewModel() {
    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            eventRepository.setDarkModeSetting(isDarkMode)
        }
    }

    fun getDarkModeSetting(): LiveData<Boolean> {
        return eventRepository.getDarkModeSetting().asLiveData()
    }

    fun setDailyReminder(isEnabled: Boolean) {
        viewModelScope.launch {
            eventRepository.setNotificationSetting(isEnabled)
        }
    }

    fun getPeriodicKey(): LiveData<String> {
        return eventRepository.getPeriodicKey().asLiveData()
    }

    fun savePeriodicKey(id: String) {
        viewModelScope.launch {
            eventRepository.savePeriodicKey(id)
        }
    }

    fun getNotificationSetting(): LiveData<Boolean> {
        return eventRepository.getNotificationSetting().asLiveData()
    }
}