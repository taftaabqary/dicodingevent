package com.althaaf.dicodingevents.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface EventDao {
    @Insert
    suspend fun addEventFavorite(eventEntity: EventEntity)

    @Delete
    suspend fun deleteEventFavorite(eventEntity: EventEntity)

    @Query("SELECT * FROM event ORDER BY idEvent ASC")
    fun getEventFavorite(): LiveData<List<EventEntity>>

    @Query("SELECT EXISTS(SELECT * FROM event WHERE idEvent = :id)")
    fun getEventById(id: Int): LiveData<Boolean>
}