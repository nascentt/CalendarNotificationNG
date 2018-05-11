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

package com.github.quarck.calnotifyng.dismissedeventsstorage

import com.github.quarck.calnotifyng.calendar.EventAlertRecord

interface DismissedEventsStorageInterface {

    fun addEvent(type: EventDismissType, changeTime: Long, event: EventAlertRecord)

    fun addEvent(type: EventDismissType, event: EventAlertRecord)

    fun addEvents(type: EventDismissType, events: Collection<EventAlertRecord>)

    fun deleteEvent(entry: DismissedEventAlertRecord)

    fun deleteEvent(event: EventAlertRecord)

    fun clearHistory()

    fun purgeOld(currentTime: Long, maxLiveTime: Long);

    val events: List<DismissedEventAlertRecord> get

}