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
package com.github.quarck.calnotifyng.prefs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.github.quarck.calnotifyng.R
import com.github.quarck.calnotifyng.Settings
import com.github.quarck.calnotifyng.logs.DevLog
//import com.github.quarck.calnotify.logs.Logger
import com.github.quarck.calnotifyng.utils.find
import com.github.quarck.calnotifyng.utils.findOrThrow

class AppStylePrefFragment : Fragment() {

    private lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DevLog.debug(LOG_TAG, "onCreate")

        val ctx = context

        if (ctx != null)
            settings = Settings(ctx)
        else
            throw Exception("Context is null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.activity_style_pref, container, false);

        val useCompactView = settings.useCompactView

        view.findOrThrow<RadioButton>(R.id.radio_button_compact_view).isChecked = useCompactView
        view.findOrThrow<RadioButton>(R.id.radio_button_card_view).isChecked = !useCompactView

        return view
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
        private const val LOG_TAG = "AppStylePrefFragment"
    }
}
