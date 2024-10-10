package com.althaaf.dicodingevents.data.retrofit

import com.althaaf.dicodingevents.data.response.EventDetailResponse
import com.althaaf.dicodingevents.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getListEvents(
        @Query("active") active: Int,
        @Query("q") q: String?,
        @Query("limit") limit: Int?,
    ): Call<EventResponse>

    @GET("events")
    suspend fun getListEventsV2(
        @Query("active") active: Int,
        @Query("q") q: String?,
        @Query("limit") limit: Int?,
    ): EventResponse

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int
    ): Call<EventDetailResponse>
}