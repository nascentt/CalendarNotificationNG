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

package com.github.quarck.calnotifyng.prefs.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.github.quarck.calnotifyng.R
import com.github.quarck.calnotifyng.Settings
import com.github.quarck.calnotifyng.notification.NotificationChannelManager
import com.github.quarck.calnotifyng.prefs.MaxRemindersPreference
import com.github.quarck.calnotifyng.prefs.PrefsRoot
import com.github.quarck.calnotifyng.prefs.ReminderPatternPreference
import com.github.quarck.calnotifyng.utils.findOrThrow

class NotificationSettingsActivity : AppCompatActivity(){

    lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pref_notification)
        settings = Settings(this)

        PrefsRoot(layoutInflater, findOrThrow<LinearLayout>(R.id.notification_pref_root)) {
            header(resources.getString(R.string.main_notifications))

            item(resources.getString(R.string.regular_notification_settings)) {
                NotificationChannelManager.launchSystemSettingForChannel(this@NotificationSettingsActivity,
                        NotificationChannelManager.SoundState.Normal,
                        false)
            }

            item(resources.getString(R.string.quiet_hours_notification_settings)){
                NotificationChannelManager.launchSystemSettingForChannel(this@NotificationSettingsActivity,
                        NotificationChannelManager.SoundState.Silent,
                        false)
            }

            item (resources.getString(R.string.alarm_notification_settings)) {
                NotificationChannelManager.launchSystemSettingForChannel(this@NotificationSettingsActivity,
                        NotificationChannelManager.SoundState.Alarm,
                        false)
            }

            header(resources.getString(R.string.reminder_notifications))

            switch(resources.getString(R.string.enable_reminders),
                    resources.getString(R.string.enable_reminders_summary)) {

                initial(settings.remindersEnabled)

                onChange{settings.remindersEnabled = it}

                depending {
                    item(resources.getString(R.string.reminder_notification_settings)) {
                        NotificationChannelManager.launchSystemSettingForChannel(this@NotificationSettingsActivity,
                                NotificationChannelManager.SoundState.Normal,
                                true)

                    }

                    item(resources.getString(R.string.alarm_reminder_notification_settings)) {
                        NotificationChannelManager.launchSystemSettingForChannel(this@NotificationSettingsActivity,
                                NotificationChannelManager.SoundState.Alarm,
                                true)

                    }

                    item(resources.getString(R.string.remind_interval)) {
                        ReminderPatternPreference(this@NotificationSettingsActivity, settings,
                                this@NotificationSettingsActivity.layoutInflater).create().show()
                    }

                    item(resources.getString(R.string.max_reminders)) {
                        MaxRemindersPreference(this@NotificationSettingsActivity, settings,
                                this@NotificationSettingsActivity.layoutInflater).create().show()
                    }
                }
            }

            header(resources.getString(R.string.other))

            switch (resources.getString(R.string.add_empty_action_to_the_end_title),
                    resources.getString(R.string.add_empty_action_to_the_end_summary)) {

                initial(settings.notificationAddEmptyAction)
                onChange{ settings.notificationAddEmptyAction = it }
            }
        }


    }
}
