package com.althaaf.dicodingevents.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.althaaf.dicodingevents.data.model.StateApi
import com.althaaf.dicodingevents.data.repository.EventRepository
import com.althaaf.dicodingevents.data.response.Event
import com.althaaf.dicodingevents.data.response.EventDetailResponse
import com.althaaf.dicodingevents.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val eventRepository: EventRepository): ViewModel() {
    private var _stateConnection: MutableLiveData<StateApi<Event>> = MutableLiveData()
    val stateConnection: LiveData<StateApi<Event>> = _stateConnection

    private var _isFavorite = MediatorLiveData<Boolean>()
    var isFavorite: LiveData<Boolean> = _isFavorite

    fun getDetailEvent(idEvent: Int) {
        _stateConnection.value = StateApi(isLoading = true)
        val client = ApiConfig.getApiService().getEventDetail(id = idEvent)
        client.enqueue(object: Callback<EventDetailResponse> {
            override fun onResponse(call: Call<EventDetailResponse>, response: Response<EventDetailResponse>) {
                _stateConnection.value = StateApi(isLoading = false)
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        _stateConnection.value = StateApi(data = responseBody.event, error = null)
                    }
                } else {
                    _stateConnection.value = StateApi(error = response.body()?.message)
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _stateConnection.value = StateApi(error = t.message)
            }

        })
    }

    fun setEventFavorite(eventEntity: com.althaaf.dicodingevents.data.local.EventEntity) {
        viewModelScope.launch {
            eventRepository.setFavoriteEvent(eventEntity)
        }
    }


    fun checkEventFavorite(id: Int) {
        Log.d("DetailViewModel", "Check Favorite ID: $id")
        _isFavorite.addSource(eventRepository.checkEventFavorite(id)) {
            _isFavorite.value = it
        }
    }

    fun deleteEventFavorite(eventEntity: com.althaaf.dicodingevents.data.local.EventEntity) {
        viewModelScope.launch {
            eventRepository.deleteFavoriteEvent(eventEntity)
        }
    }
}