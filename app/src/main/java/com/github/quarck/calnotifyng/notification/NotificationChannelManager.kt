//
//   Calendar Notifications Plus
//   Copyright (C) 2018 Sergey Parshin (s.parshin.sc@gmail.com)
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 3 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software Foundation,
//   Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
//

package com.github.quarck.calnotifyng.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import com.github.quarck.calnotifyng.Consts
import com.github.quarck.calnotifyng.NotificationSettingsSnapshot
import com.github.quarck.calnotifyng.R
import com.github.quarck.calnotifyng.Settings
import com.github.quarck.calnotifyng.logs.DevLog
import com.github.quarck.calnotifyng.utils.notificationManager


object NotificationChannelManager {

    const val NOTIFICATION_CHANNEL_ID_DEFAULT = "com.github.calnotifyng.notify.cal"
    const val NOTIFICATION_CHANNEL_ID_ALARM = "com.github.calnotifyng.notify.calalrm"
    const val NOTIFICATION_CHANNEL_ID_SILENT = "com.github.calnotifyng.notify.calquiet"

    const val NOTIFICATION_CHANNEL_ID_REMINDER = "com.github.calnotifyng.notify.rem"
    const val NOTIFICAITON_CHANNEL_ID_REMINDER_ALARM = "com.github.calnotifyng.notify.remalrm"

    fun createDefaultNotificationChannelDebug(context: Context): String {

        val channelId = NOTIFICATION_CHANNEL_ID_DEFAULT

        val settings = Settings(context)

        val notificationChannel =
                NotificationChannel(
                        channelId,
                        context.getString(R.string.debug_notifications),
                        NotificationManager.IMPORTANCE_DEFAULT
                )

        // Configure the notification channel.
        notificationChannel.description = context.getString(R.string.debug_notifications_description)

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Consts.DEFAULT_LED_COLOR

        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern = settings.vibrationPattern

        notificationChannel.importance = NotificationManager.IMPORTANCE_DEFAULT;

        val attribBuilder = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)

        attribBuilder.setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)

        notificationChannel.setSound(settings.ringtoneURI, attribBuilder.build())

        context.notificationManager.createNotificationChannel(notificationChannel)

        return channelId
    }

    enum class SoundState {
        Normal,
        Alarm,
        Silent
    }

    fun createNotificationChannelForPurpose(
            context: Context,
            isReminder: Boolean,
            soundState: SoundState
    ): String {

        val channelId: String
        val channelName: String
        val channelDesc: String

        val settings = Settings(context)

        var importance = NotificationManager.IMPORTANCE_DEFAULT

        if (!isReminder) {
            // Regular notification - NOT a reminder
            // Non-repost - initial notification
            when (soundState) {
                NotificationChannelManager.SoundState.Normal -> {
                    channelId = NOTIFICATION_CHANNEL_ID_DEFAULT
                    channelName = context.getString(R.string.notification_channel_default)
                    channelDesc = context.getString(R.string.notification_channel_default_desc)
                    importance = NotificationManager.IMPORTANCE_DEFAULT
                }
                NotificationChannelManager.SoundState.Alarm -> {
                    channelId = NOTIFICATION_CHANNEL_ID_ALARM
                    channelName = context.getString(R.string.notification_channel_alarm)
                    channelDesc = context.getString(R.string.notification_channel_alarm_desc)
                    importance = NotificationManager.IMPORTANCE_HIGH
                }
                NotificationChannelManager.SoundState.Silent -> {
                    channelId = NOTIFICATION_CHANNEL_ID_SILENT
                    channelName = context.getString(R.string.notification_channel_silent)
                    channelDesc = context.getString(R.string.notification_channel_silent_desc)
                    importance = NotificationManager.IMPORTANCE_LOW
                }
            }
        }
        else { // if (!isReminder) {
            // Reminder notification
            // isRepost is ignored
            if (soundState == SoundState.Alarm) {
                // use alarm reminder channel
                channelId = NOTIFICAITON_CHANNEL_ID_REMINDER_ALARM
                channelName = context.getString(R.string.notification_channel_alarm_reminders)
                channelDesc = context.getString(R.string.notification_channel_alarm_reminders_desc)
                importance = NotificationManager.IMPORTANCE_HIGH
            }
            else { // if (soundState == SoundState.Alarm) {
                // use regular channel - there are no silent reminders
                channelId = NOTIFICATION_CHANNEL_ID_REMINDER
                channelName = context.getString(R.string.notification_channel_reminders)
                channelDesc = context.getString(R.string.notification_channel_reminders_desc)
                importance = NotificationManager.IMPORTANCE_DEFAULT
            }
        }

        DevLog.info(context, LOG_TAG, "Notification channel for state $soundState, is reminder: $isReminder," +
                " -> channel ID $channelId, importance $importance")

        // Configure the notification channel.
        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationChannel.description = channelDesc

        // If we don't enable it now (at channel creation) - no way to enable it later
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Consts.DEFAULT_LED_COLOR

        val attribBuilder = AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)

        if (soundState == SoundState.Alarm) {
            attribBuilder
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setLegacyStreamType(AudioManager.STREAM_ALARM)
                    .setUsage(AudioAttributes.USAGE_ALARM)

            DevLog.info(context, LOG_TAG, "Alarm attributes applied")
        }
        else {
            attribBuilder.setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
        }

        if (soundState != SoundState.Silent) {
            if (!isReminder) {
                notificationChannel.setSound(settings.ringtoneURI, attribBuilder.build())

                if (settings.vibraOn) {
                    notificationChannel.enableVibration(true)
                    notificationChannel.vibrationPattern = settings.vibrationPattern
                } else {
                    notificationChannel.enableVibration(false)
                }
            }
            else {
                notificationChannel.setSound(settings.reminderRingtoneURI, attribBuilder.build())

                if (settings.reminderVibraOn) {
                    notificationChannel.enableVibration(true)
                    notificationChannel.vibrationPattern = settings.reminderVibrationPattern
                } else {
                    notificationChannel.enableVibration(false)
                }
            }
        }

        if (isReminder) {
            notificationChannel.setShowBadge(false)
        }

        context.notificationManager.createNotificationChannel(notificationChannel)

        return channelId
    }

    private const val LOG_TAG = "NotificationChannelManager"
}