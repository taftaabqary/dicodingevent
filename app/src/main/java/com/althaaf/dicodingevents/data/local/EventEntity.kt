package com.althaaf.dicodingevents.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey
    @field:ColumnInfo(name = "idEvent")
    val id: Int,

    @field:ColumnInfo(name = "title")
    val title: String,

    @field:ColumnInfo(name = "location")
    val location: String,

    @field:ColumnInfo(name = "image")
    val image: String,

    @field:ColumnInfo(name = "category")
    val category: String,

    @field:ColumnInfo(name = "owner")
    val owner: String,

    @field:ColumnInfo(name = "date")
    val date: String,
): Parcelable
