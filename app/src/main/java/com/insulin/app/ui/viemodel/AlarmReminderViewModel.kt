package com.insulin.app.ui.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.insulin.app.utils.preferences.AlarmReminderPreferences
import kotlinx.coroutines.launch

class AlarmReminderViewModel(private val pref: AlarmReminderPreferences) : ViewModel() {

    fun saveAlarmReminder(frequency: String, day: String, time: String) {
        viewModelScope.launch {
            pref.saveAlarmReminder(frequency, day, time)
        }
    }

    fun getFrequencyReminder(): LiveData<String> {
        return pref.getFrequencyReminder().asLiveData()
    }

    fun getDayReminder(): LiveData<String> {
        return pref.getDayReminder().asLiveData()
    }

    fun getTimeReminder(): LiveData<String> {
        return pref.getTimeReminder().asLiveData()
    }

    fun removeAlarmReminder() {
        viewModelScope.launch {
            pref.removeAlarmReminder()
        }
    }
}