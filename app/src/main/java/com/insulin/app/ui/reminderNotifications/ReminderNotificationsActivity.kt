package com.insulin.app.ui.reminderNotifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.insulin.app.databinding.ActivityReminderNotificationsBinding

class ReminderNotificationsActivity : AppCompatActivity() {

    private var binding: ActivityReminderNotificationsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderNotificationsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // test
    }
}