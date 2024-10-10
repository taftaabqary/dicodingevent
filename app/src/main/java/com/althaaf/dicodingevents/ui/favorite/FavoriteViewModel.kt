package com.althaaf.dicodingevents.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.dicodingevents.data.local.EventEntity
import com.althaaf.dicodingevents.data.repository.EventRepository

class FavoriteViewModel(private val eventRepository: EventRepository): ViewModel() {
    fun getFavoriteEvent(): LiveData<List<EventEntity>> {
        return eventRepository.getFavoriteEvent()
    }
}