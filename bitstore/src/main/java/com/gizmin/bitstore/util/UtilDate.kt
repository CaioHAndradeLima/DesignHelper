package com.gizmin.bitstore.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object UtilsDate {

    const val FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val FORMAT_DD_MM_HH_MM = "dd/MM hh:mm"

    fun getToday() : Date {
        return Date()
    }

    fun getDateAfterDaysPassedByParam( days : Int , dateInit : Date = getToday() ) : Date {
        return getDateAfterDaysPassedByParam(days.toFloat() ,dateInit)
    }

    fun getDateAfterDaysPassedByParam( days : Float , dateInit : Date = getToday() ) : Date {
        return Date(dateInit.time + ((ONE_DAY_IN_TIMEMILLS) * days ).toInt())
    }

    fun getDateBeforeDaysPassedByParam( days : Int , dateInit : Date = getToday() ) : Date {
        return try {
            val cal = Calendar.getInstance()
            cal.time = dateInit
            cal.add(Calendar.DATE, -days)
            cal.time
        } catch (e: Exception) {
            Date(dateInit.time - ((ONE_DAY_IN_TIMEMILLS) * days ))
        }
    }

    fun formatDateToFormat(format : String, date : Date): String {
        val dateFormat = SimpleDateFormat(format)
        return dateFormat.format(date)
    }

    fun formatToUser(date: Date): String {
        if(DateUtils.isToday(date.time))
            return "hoje"

        if(isYesterday(date))
            return "ontem"

        return UtilsDate.formatDateToFormat("dd/MM/yy", date)

    }

    private fun isYesterday(date: Date): Boolean {
        val c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

        val c2 = Calendar.getInstance();
        c2.setTime(date); // your date

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    private const val ONE_DAY_IN_TIMEMILLS = 1000 * 60 * 60 * 24

}
