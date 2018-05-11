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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.RadioButton
import com.github.quarck.calnotifyng.R
import com.github.quarck.calnotifyng.Settings
import com.github.quarck.calnotifyng.logs.DevLog
//import com.github.quarck.calnotify.logs.Logger
import com.github.quarck.calnotifyng.utils.find
import com.github.quarck.calnotifyng.utils.findOrThrow

class AppStylePrefActivity : AppCompatActivity() {

    private lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DevLog.debug(LOG_TAG, "onCreate")

        setContentView(R.layout.activity_style_pref)

        setSupportActionBar(find<Toolbar?>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        settings = Settings(this)

        val useCompactView = settings.useCompactView

        findOrThrow<RadioButton>(R.id.radio_button_compact_view).isChecked = useCompactView
        findOrThrow<RadioButton>(R.id.radio_button_card_view).isChecked = !useCompactView
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton && view.isChecked) {
            when (view.id) {
                R.id.radio_button_card_view ->
                    settings.useCompactView = false

                R.id.radio_button_compact_view ->
                    settings.useCompactView = true
            }
        }
    }


    companion object {
        private const val LOG_TAG = "AppStylePrefActivity"
    }
}