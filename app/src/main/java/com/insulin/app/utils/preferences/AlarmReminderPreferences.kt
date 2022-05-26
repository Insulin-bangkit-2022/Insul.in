package com.insulin.app.utils.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlarmReminderPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val ALARM_REMINDER_FREQUENCY_KEY = stringPreferencesKey("alarm_reminder_frequency")
    private val ALARM_REMINDER_DAY_KEY = stringPreferencesKey("alarm_reminder_day")
    private val ALARM_REMINDER_TIME_KEY = stringPreferencesKey("alarm_reminder_time")

    // Store Alarm Reminder Data
    suspend fun saveAlarmReminder(frequency: String, day: String, time: String) {
        dataStore.edit { preferences ->
            preferences[ALARM_REMINDER_FREQUENCY_KEY] = frequency
            preferences[ALARM_REMINDER_DAY_KEY] = day
            preferences[ALARM_REMINDER_TIME_KEY] = time
        }
    }


    fun getFrequencyReminder(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ALARM_REMINDER_FREQUENCY_KEY] ?: ""
        }
    }

    fun getDayReminder(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ALARM_REMINDER_DAY_KEY] ?: ""
        }
    }

    fun getTimeReminder(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ALARM_REMINDER_TIME_KEY] ?: ""
        }
    }

    suspend fun removeAlarmReminder() {
        dataStore.edit { preferences ->
            preferences[ALARM_REMINDER_FREQUENCY_KEY] = ""
            preferences[ALARM_REMINDER_DAY_KEY] = ""
            preferences[ALARM_REMINDER_TIME_KEY] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AlarmReminderPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): AlarmReminderPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AlarmReminderPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}