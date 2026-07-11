package com.nahid.dailyhisab.ui.components

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val banglaMonths = arrayOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )

    private val banglaDays = arrayOf(
        "রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার"
    )

    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("bn"))
        return sdf.format(Date(timestamp))
    }

    fun formatDateShort(timestamp: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = banglaMonths[cal.get(Calendar.MONTH)]
        return "$day $month"
    }

    fun formatDayName(timestamp: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        return banglaDays[cal.get(Calendar.DAY_OF_WEEK) - 1]
    }

    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale("bn"))
        return sdf.format(Date(timestamp))
    }

    fun getMonthName(month: Int): String {
        return banglaMonths.getOrElse(month) { "" }
    }

    fun formatAmount(amount: Double): String {
        return String.format("৳ %.2f", amount)
    }
}
