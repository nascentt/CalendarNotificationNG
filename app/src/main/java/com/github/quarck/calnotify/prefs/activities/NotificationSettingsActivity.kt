//
//   Calendar Notifications Plus
//   Copyright (C) 2016 Sergey Parshin (s.parshin.sc@gmail.com)
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

package com.github.quarck.calnotify.prefs.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.quarck.calnotify.R
import com.github.quarck.calnotify.Settings
import com.github.quarck.calnotify.notification.NotificationChannelManager
import com.github.quarck.calnotify.prefs.MaxRemindersPreference
import com.github.quarck.calnotify.prefs.ReminderPatternPreference
import com.github.quarck.calnotify.prefs.preferences

class NotificationSettingsActivity : AppCompatActivity(){

    lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        settings = Settings(this)

        preferences(this) {

            switch(R.string.snooze_on_swipe, R.string.snooze_on_swipe_summary_v2) {
                initial(settings.notificationSwipeDoesSnooze)
                onChange{settings.notificationSwipeDoesSnooze = it }
            }

            header(R.string.main_notifications)

            item(R.string.regular_notification_settings) {
                NotificationChannelManager.launchSystemSettingForChannel(this@NotificationSettingsActivity,
                        NotificationChannelManager.SoundState.Normal,
                        false)
            }

            item(R.string.quiet_hours_notification_settings){
                NotificationChannelManager.launchSystemSettingForChannel(this@NotificationSettingsActivity,
                        NotificationChannelManager.SoundState.Silent,
                        false)
            }

            item (R.string.alarm_notification_settings) {
                NotificationChannelManager.launchSystemSettingForChannel(this@NotificationSettingsActivity,
                        NotificationChannelManager.SoundState.Alarm,
                        false)
            }

            header(R.string.reminder_notifications)

            switch(R.string.enable_reminders, R.string.enable_reminders_summary) {

                initial(settings.remindersEnabled)

                onChange{settings.remindersEnabled = it}

                depending {
                    item(R.string.reminder_notification_settings) {
                        NotificationChannelManager.launchSystemSettingForChannel(this@NotificationSettingsActivity,
                                NotificationChannelManager.SoundState.Normal, true)

                    }

                    item(R.string.alarm_reminder_notification_settings) {
                        NotificationChannelManager.launchSystemSettingForChannel(this@NotificationSettingsActivity,
                                NotificationChannelManager.SoundState.Alarm, true)

                    }

                    item(R.string.remind_interval) {
                        ReminderPatternPreference(this@NotificationSettingsActivity, settings,
                                this@NotificationSettingsActivity.layoutInflater).create().show()
                    }

                    item(R.string.max_reminders) {
                        MaxRemindersPreference(this@NotificationSettingsActivity, settings,
                                this@NotificationSettingsActivity.layoutInflater).create().show()
                    }
                }
            }

            header(R.string.other)

            switch(R.string.add_empty_action_to_the_end_title,
                    R.string.add_empty_action_to_the_end_summary) {

                initial(settings.notificationAddEmptyAction)
                onChange{ settings.notificationAddEmptyAction = it }
            }

            switch(R.string.use_alarm_stream, R.string.use_alarm_stream_summary) {
                initial(settings.notificationUseAlarmStream)
                onChange{settings.notificationUseAlarmStream = it}
            }
        }
    }
}
