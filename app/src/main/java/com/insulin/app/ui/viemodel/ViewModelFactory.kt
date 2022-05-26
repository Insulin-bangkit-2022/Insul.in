package com.insulin.app.ui.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.insulin.app.utils.preferences.AlarmReminderPreferences
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: AlarmReminderPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmReminderViewModel::class.java)) {
            return AlarmReminderViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}