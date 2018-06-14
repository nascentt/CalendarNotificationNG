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

package com.github.quarck.calnotifyng.prefs

import android.content.Context
import android.content.res.TypedArray
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import com.github.quarck.calnotifyng.R
import com.github.quarck.calnotifyng.Settings
//import com.github.quarck.calnotify.logs.Logger
import com.github.quarck.calnotifyng.utils.find
import com.github.quarck.calnotifyng.utils.findOrThrow
import java.text.DateFormat
import java.util.*

class TimeOfDayPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

    internal var timeValue = Pair(0, 0) // hr, min

    // UI representation
    internal lateinit var picker: TimePicker

    internal var isTwentyFourHour: Boolean = true

    internal var widgetView: TextView? = null

    init {
        val settings = Settings(getContext())

        dialogLayoutResource = R.layout.dialog_time_of_day

        widgetLayoutResource = R.layout.dialog_time_of_day_widget

        setPositiveButtonText(android.R.string.ok)
        setNegativeButtonText(android.R.string.cancel)

        dialogIcon = null
        isTwentyFourHour = android.text.format.DateFormat.is24HourFormat(context)//  context.is24HoursClock()
    }

    override fun onBindView(view: View) {
        super.onBindView(view)

        widgetView = view.find<TextView?>(R.id.dialog_time_of_day_widget)

        updateWidgetView()
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        picker = view.findOrThrow<TimePicker>(R.id.time_picker_pref_time_of_day)

        picker.setIs24HourView(isTwentyFourHour)
        picker.hour = timeValue.component1()
        picker.minute = timeValue.component2()

        updateWidgetView()
    }

    override fun onClick() {
        super.onClick()
        picker.clearFocus()
    }

    override fun onDialogClosed(positiveResult: Boolean) {

        // When the user selects "OK", persist the new value
        if (positiveResult) {
            picker.clearFocus()

            timeValue = Pair(picker.hour, picker.minute)
            persistInt(PreferenceUtils.packTime(timeValue))
            updateWidgetView()
        }
    }

    private fun formatTime(hours: Int, minutes: Int): String {
        val timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT)
        val asUnixMillis = (hours * 3600 + minutes * 60) * 1000L
        val date = Date(asUnixMillis)
        return timeFormatter.format(date)
    }

    private fun updateWidgetView() {
        widgetView?.text = formatTime(timeValue.component1(), timeValue.component2())
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {

        var timeValueInt: Int = 0

        if (restorePersistedValue) {
            // Restore existing state
            timeValueInt = this.getPersistedInt(0)

        }
        else if (defaultValue != null && defaultValue is Int) {
            // Set default state from the XML attribute
            timeValueInt = defaultValue
            persistInt(defaultValue)
        }

        timeValue = PreferenceUtils.unpackTime(timeValueInt)
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getInteger(index, 0)
    }

    companion object {
        private const val LOG_TAG = "TimeOfDayPreference"
    }
}
