package com.insulin.app.ui.reminderNotifications

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.insulin.app.R
import com.insulin.app.databinding.ActivityReminderNotificationsBinding
import com.insulin.app.ui.viemodel.AlarmReminderViewModel
import com.insulin.app.ui.viemodel.ViewModelFactory
import com.insulin.app.utils.TimePickerFragment
import com.insulin.app.utils.preferences.AlarmReminderPreferences
import java.text.SimpleDateFormat
import java.util.*

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "reminder")

class ReminderNotificationsActivity : AppCompatActivity(), View.OnClickListener, TimePickerFragment.DialogTimeListener {

    private lateinit var binding: ActivityReminderNotificationsBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var reminderViewModel: AlarmReminderViewModel
    val defaultText = "Klik untuk menyetel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AlarmReminderPreferences.getInstance(datastore)
        reminderViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AlarmReminderViewModel::class.java]

        reminderViewModel.getFrequencyReminder().observe(this) { freq ->
            binding.tvFrequencyDay.text = setTextReminder(freq)

        }

        reminderViewModel.getDayReminder().observe(this) { day ->
            binding.tvDayDay.text = setTextReminder(day)
        }

        reminderViewModel.getTimeReminder().observe(this) { time ->
            binding.timeTime.text = setTextReminder(time)
        }

        binding.tvFrequencyDay.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val frequencyDay = arrayOf(
                "Setiap Minggu",
                "Setiap Bulan"
            )
            builder.setSingleChoiceItems(frequencyDay, -1) { dialog, which ->
                binding.tvFrequencyDay.text = frequencyDay[which]
                dialog.dismiss()
            }
            builder.create().show()
        }

        binding.tvDayDay.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dayDay = arrayOf(
                "Hari",
                "Senin",
                "Selasa",
                "Rabu",
                "Kamis",
                "Jumat",
                "Sabtu",
                "Minggu"
            )
            builder.setSingleChoiceItems(dayDay, -1) { dialog, which ->
                binding.tvDayDay.text = dayDay[which]
                dialog.dismiss()
            }
            builder.create().show()
        }
        binding.timeTime.setOnClickListener(this)
        binding.btnActive.setOnClickListener(this)
        binding.btnDelete.setOnClickListener {
            alarmReceiver.cancelAlarm(this, AlarmReceiver.ID_REPEATING.toString())
            reminderViewModel.removeAlarmReminder()
        }
        alarmReceiver = AlarmReceiver()
    }

    fun setTextReminder(text: String) : String {
        return if (text.isEmpty()) defaultText else text
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.time_time -> {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            R.id.btn_active -> {
                val onceFrequency = binding.tvFrequencyDay.text.toString()
                val onceDay = binding.tvDayDay.text.toString()
                val onceTime = binding.timeTime.text.toString()

                reminderViewModel.saveAlarmReminder(onceFrequency, onceDay, onceTime)

                if (onceFrequency == defaultText || onceDay == defaultText || onceTime == defaultText) {
                    Toast.makeText(this, "Harap memilih waktu pengingat deteksi", Toast.LENGTH_SHORT).show()
                } else {
                    alarmReceiver.setOneTimeAlarm(this, AlarmReceiver.TYPE_ONE_TIME,
                        onceTime, onceFrequency)
                }

            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            TIME_PICKER_ONCE_TAG -> binding.timeTime.text = dateFormat.format(calendar.time)
        }
    }

    companion object {
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
    }

}