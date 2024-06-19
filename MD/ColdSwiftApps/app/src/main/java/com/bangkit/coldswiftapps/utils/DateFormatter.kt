package com.bangkit.coldswiftapps.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

object DateFormatter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateDay(currentDateString: String): String {
        val instant = Instant.parse(currentDateString)
        val formatter = DateTimeFormatter.ofPattern("dd")
            .withZone(ZoneId.of(TimeZone.getDefault().id))
        return formatter.format(instant)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatMonthAbbreviation(currentDateString: String): String {
        val instant = Instant.parse(currentDateString)
        val formatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)
            .withZone(ZoneId.of(TimeZone.getDefault().id))
        return formatter.format(instant)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun formatDate(currentDateString: String?, targetTimeZone: String): String {
//        val instant = Instant.parse(currentDateString)
//        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
//            .withZone(ZoneId.of(targetTimeZone))
//        return formatter.format(instant)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(currentDateString: String?, targetTimeZone: String): String {
        val instant = Instant.parse(currentDateString)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
            .withZone(ZoneId.of(targetTimeZone))
        return formatter.format(instant)
    }

    fun formatPrice(priceString: String): String {
        try {
            val price = priceString.toDouble() // Convert string to double
            val formattedPrice = when {
                price >= 1_000_000 -> "${price / 1_000_000}M"
                price >= 1_000 -> "${price / 1_000}K"
                else -> priceString // No change if less than 1000
            }
            return formattedPrice.replace(".0", "") // Remove decimal if it's ".0"
        } catch (e: NumberFormatException) {
            // Handle invalid number format gracefully
            return priceString
        }
    }

    fun formatCurrency(amount: Double): String {
        val locale = Locale("id", "ID")
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        return currencyFormatter.format(amount).replace("Rp", "Rp.")
    }
}