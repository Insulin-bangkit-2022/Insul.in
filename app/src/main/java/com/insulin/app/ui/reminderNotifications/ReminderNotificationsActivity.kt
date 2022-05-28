package com.insulin.app.ui.reminderNotifications

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

class ReminderNotificationsActivity : AppCompatActivity(),
    TimePickerFragment.DialogTimeListener {

    private lateinit var binding: ActivityReminderNotificationsBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var reminderViewModel: AlarmReminderViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val defaultText = getString(R.string.reminder_empty_schedule)
        alarmReceiver = AlarmReceiver()

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }


        val pref = AlarmReminderPreferences.getInstance(datastore)
        reminderViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[AlarmReminderViewModel::class.java]

        reminderViewModel.getFrequencyReminder().observe(this) { freq ->
            binding.tvFrequencyValue.text = setTextReminder(freq)
            if(freq == getString(R.string.reminder_freq_daily)){
                binding.setDay.isVisible = false
                binding.tvDayValue.text = getString(R.string.day_monday)
            }else{
                binding.setDay.isVisible = true
            }
        }

        reminderViewModel.getDayReminder().observe(this) { day ->
            binding.tvDayValue.text = setTextReminder(day)
        }

        reminderViewModel.getTimeReminder().observe(this) { time ->
            binding.tvTimeValue.text = setTextReminder(time)

            /* load info set reminder */
            val day = binding.tvDayValue.text
            val template = when (binding.tvFrequencyValue.text) {
                getString(R.string.reminder_freq_weekly) -> String.format(
                    getString(R.string.reminder_info_set_weekly),
                    day,
                    time
                )
                getString(R.string.reminder_freq_daily) -> String.format(
                    getString(R.string.reminder_info_set_daily),
                    time
                )
                else -> getString(R.string.reminder_not_set)
            }
            binding.infoMessages.text = template
        }

        /* pick freq clicked */
        binding.setFreq.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val frequencyDay = arrayOf(
                getString(R.string.reminder_freq_daily),
                getString(R.string.reminder_freq_weekly),
            )
            builder.setSingleChoiceItems(frequencyDay, -1) { dialog, which ->
                if(frequencyDay[which] == getString(R.string.reminder_freq_daily)){
                    binding.setDay.isVisible = false
                    binding.tvDayValue.text = getString(R.string.day_monday)
                }else{
                    binding.setDay.isVisible = true
                }
                binding.tvFrequencyValue.text = frequencyDay[which]
                dialog.dismiss()
            }
            builder.create().show()
        }

        /* pick day clicked */
        binding.setDay.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dayDay = arrayOf(
                getString(R.string.day_monday),
                getString(R.string.day_tuesday),
                getString(R.string.day_wednesday),
                getString(R.string.day_thursday),
                getString(R.string.day_friday),
                getString(R.string.day_saturday),
                getString(R.string.day_sunday),
            )
            builder.setSingleChoiceItems(dayDay, -1) { dialog, which ->
                binding.tvDayValue.text = dayDay[which]
                dialog.dismiss()
            }
            builder.create().show()
        }

        /* pick time clicked */
        binding.setTime.setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
        }

        /* button activate clicked */
        binding.btnActive.setOnClickListener {
            val onceFrequency = binding.tvFrequencyValue.text.toString()
            val onceDay = binding.tvDayValue.text.toString()
            val onceTime = binding.tvTimeValue.text.toString()


            if (onceFrequency == defaultText || onceDay == defaultText || onceTime == defaultText) {
                Toast.makeText(
                    this,
                    getString(R.string.reminder_confirm_choose_schedule),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                reminderViewModel.saveAlarmReminder(onceFrequency, onceDay, onceTime)
                alarmReceiver.setOneTimeAlarm(
                    this, AlarmReceiver.TYPE_ONE_TIME, onceFrequency, onceDay, onceTime
                )
            }
        }
        binding.btnDelete.setOnClickListener {
            if (binding.infoMessages.text == getString(R.string.reminder_not_set)) {
                Toast.makeText(
                    this,
                    getString(R.string.reminder_not_set),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val alertDialogBuilder = AlertDialog.Builder(this@ReminderNotificationsActivity)
                alertDialogBuilder.setTitle(getString(R.string.reminder_confirm_delete_dialog_title))
                alertDialogBuilder.setMessage(getString(R.string.reminder_confirm_delete_dialog_message))
                alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { alertDialog, _ ->
                    alertDialog.dismiss()
                    alarmReceiver.cancelAlarm(this, AlarmReceiver.ID_REPEATING.toString())
                    reminderViewModel.removeAlarmReminder()
                }
                alertDialogBuilder.setNegativeButton(getString(R.string.no)) { alertDialog, _ ->
                    alertDialog.dismiss()
                }
                alertDialogBuilder.create().show()
            }

        }
    }

    private fun setTextReminder(text: String) =
        text.ifEmpty { getString(R.string.reminder_empty_schedule) }


    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            TIME_PICKER_ONCE_TAG -> binding.tvTimeValue.text = dateFormat.format(calendar.time)
        }
    }

    companion object {
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
    }

}