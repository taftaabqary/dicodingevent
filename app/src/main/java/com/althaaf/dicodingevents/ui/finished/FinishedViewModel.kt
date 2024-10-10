package com.althaaf.dicodingevents.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.althaaf.dicodingevents.data.model.StateApi
import com.althaaf.dicodingevents.data.response.EventResponse
import com.althaaf.dicodingevents.data.response.ListEventsItem
import com.althaaf.dicodingevents.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel: ViewModel() {
    private var _stateConnection: MutableLiveData<StateApi<List<ListEventsItem>>> = MutableLiveData()
    val stateConnection: LiveData<StateApi<List<ListEventsItem>>> = _stateConnection

    init {
        Log.d(TAG, "Init FinishedViewModel")
        getFinishedEvents()
    }

    fun getFinishedEvents() {
        _stateConnection.value = StateApi(isLoading = true)
        val client = ApiConfig.getApiService().getListEvents(active = 0, q = null, limit = null)
        client.enqueue(object: Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _stateConnection.value = StateApi(isLoading = false)
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        _stateConnection.value = StateApi(data = responseBody.listEvents, error = null)
                    }
                } else {
                    _stateConnection.value = StateApi(error = response.body()?.message)
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.d(TAG, "Error onFailure FinishedViewModel")
                _stateConnection.value = StateApi(error = t.message)
            }

        })
    }


    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "Cleared FinishedViewModel")
    }


    companion object {
        private const val TAG = "FinishedViewModel"
    }
}