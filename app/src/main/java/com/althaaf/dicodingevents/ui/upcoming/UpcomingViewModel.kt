package com.althaaf.dicodingevents.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.dicodingevents.data.model.ApiResult
import com.althaaf.dicodingevents.data.repository.EventRepository
import com.althaaf.dicodingevents.data.response.ListEventsItem

class UpcomingViewModel(private val eventRepository: EventRepository): ViewModel() {
    fun getListEvent(active: Int, limit: Int?, query: String?): LiveData<ApiResult<List<ListEventsItem>>> {
        return eventRepository.getListEvents(active, limit, query)
    }
}