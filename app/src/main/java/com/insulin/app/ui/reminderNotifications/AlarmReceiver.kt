package com.insulin.app.ui.reminderNotifications

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.insulin.app.R
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.utils.Helper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION", "SameParameterValue")
@RequiresApi(Build.VERSION_CODES.M)
class AlarmReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        /* when broadcast alarm reminder recieved -> show notification */
        val type = intent.getStringExtra(EXTRA_TYPE)
        val title = intent.getStringExtra(EXTRA_NOTIF_TITLE) ?: EXTRA_NOTIF_TITLE
        val message = intent.getStringExtra(EXTRA_NOTIF_MESSAGE) ?: EXTRA_NOTIF_MESSAGE
        val notifId =
            if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) ID_ONETIME else ID_REPEATING
        showAlarmNotification(context, notifId, title, message)
    }

    @SuppressLint("NewApi")
    fun setOneTimeAlarm(
        context: Context,
        type: String,
        frequency: String,
        day: String,
        time: String
    ) {
        /* init instance */
        if (isDateInvalid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager



        /* split time (HH:mm) into HH & mm */
        val timeArray = time.split(":").toTypedArray()
        val calendar = Calendar.getInstance()

        /* get int value of day name for set exact day reminder */
        val dayInt = when (day) {
            context.getString(R.string.day_monday) -> Calendar.MONDAY
            context.getString(R.string.day_tuesday) -> Calendar.TUESDAY
            context.getString(R.string.day_wednesday) -> Calendar.WEDNESDAY
            context.getString(R.string.day_thursday) -> Calendar.THURSDAY
            context.getString(R.string.day_friday) -> Calendar.FRIDAY
            context.getString(R.string.day_saturday) -> Calendar.SATURDAY
            else -> Calendar.SUNDAY
        }

        /* get frequency of set reminder -> for set interval */
        val freqInDay = when (frequency) {
            context.getString(R.string.reminder_freq_weekly) -> 7
            else -> 1
        }

        /* set next reminder as user inputs */
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        /* if time set lower than now -> set for next freq*/
        val now = Calendar.getInstance().time
        when (freqInDay) {
            /* everyday */
            1 -> {
                /* check if reminder for today is passed -> if passed, set for tomorrow on the same time */
                if (calendar.time.before(now)) {
                    calendar.set(Calendar.DATE, now.date + 1)
                }
            }
            7 -> {
                /* check if reminder for today is passed -> if passed, set for tomorrow on the same time */
                if (calendar.time.before(now)) {
                    calendar.add(Calendar.DATE, freqInDay)
                    calendar.set(Calendar.DAY_OF_WEEK, dayInt)
                }
            }
        }

        /* set interval for remind user based incoming freq (in days) */
        val interval = AlarmManager.INTERVAL_DAY * freqInDay

        /* init intent broadcast*/
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_NOTIF_TITLE, context.getString(R.string.reminder_notification_title))
        intent.putExtra(EXTRA_NOTIF_MESSAGE,  context.getString(R.string.reminder_notification_message))
        intent.putExtra(EXTRA_TYPE, type)
        val pendingIntent =
            PendingIntent.getBroadcast(context, ID_ONETIME, intent, PendingIntent.FLAG_IMMUTABLE)

        /* ask permission to set exact alarm for android 12 above */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val intentPermission = Intent().apply {
                    action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                }
                context.startActivity(intentPermission)
            }
        }

        /* show notif on set time & repeat as interval set */
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            interval,
            pendingIntent
        )
        /* show info for incoming reminder */
        Toast.makeText(
            context,
            String.format(
                context.getString(R.string.reminder_set_for),
                Helper.reformatDateToSimpleDate(calendar.time)
            ),
            Toast.LENGTH_SHORT
        )
            .show()
    }

    private fun showAlarmNotification(
        context: Context,
        notifId: Int,
        notifTitle: String,
        notifMessage: String,
    ) {
        /* init notification instance */
        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        /* when user click notification -> open detection activity */
        val resultIntent = Intent(context, DetectionActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        /* set notification */
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_app_logo
                )
            )
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(notifTitle)
            .setContentText(notifMessage)
            .setColor(ContextCompat.getColor(context, R.color.primary_dark))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentIntent(resultPendingIntent)
            .addAction(
                R.drawable.ic_diagnose,
                context.getString(R.string.text_action_detection),
                resultPendingIntent
            )
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        /* add notification channel for android above version oreo */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        /* show notification */
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    /* remove active alarm -> reset reminder */
    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode =
            if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) ID_ONETIME else ID_REPEATING
        val pendingIntent =
            PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, context.getString(R.string.reminder_removed), Toast.LENGTH_SHORT)
            .show()
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        @Suppress("SameParameterValue")
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    companion object {
        const val TYPE_ONE_TIME = "OneTimeAlarm"
        const val EXTRA_NOTIF_MESSAGE = "message"
        const val EXTRA_NOTIF_TITLE = "title"
        const val EXTRA_TYPE = "type"

        // Siapkan 2 id untuk 2 macam alarm, onetime dan repeating
        private const val ID_ONETIME = 100
        const val ID_REPEATING = 101

        private const val TIME_FORMAT = "HH:mm"
    }
}