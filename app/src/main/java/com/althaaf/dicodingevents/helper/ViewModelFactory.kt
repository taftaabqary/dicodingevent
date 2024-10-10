package com.althaaf.dicodingevents.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.dicodingevents.data.repository.EventRepository
import com.althaaf.dicodingevents.di.Injection
import com.althaaf.dicodingevents.ui.detail.DetailViewModel
import com.althaaf.dicodingevents.ui.favorite.FavoriteViewModel
import com.althaaf.dicodingevents.ui.setting.SettingViewModel
import com.althaaf.dicodingevents.ui.upcoming.UpcomingViewModel

class ViewModelFactory private constructor(private val eventRepository: EventRepository): ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if(modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(eventRepository) as T
        } else if(modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(eventRepository) as T
        } else if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(eventRepository) as T
        } else if(modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = ViewModelFactory(Injection.provideRepository(context))
                INSTANCE = instance
                instance
            }
        }
    }
}