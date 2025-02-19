package com.example.cartbutler.components

import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.icu.util.Currency
import android.icu.util.ULocale

fun formatCurrency(amount: Float): String {
    return try {
        val systemLocale = ULocale.getDefault()

        NumberFormatter.withLocale(systemLocale)
            .unit(Currency.getInstance(systemLocale))
            .precision(Precision.fixedFraction(2))
            .format(amount.toDouble())
            .toString()
    } catch (e: Exception) {
        val currencySymbol = Currency.getInstance(ULocale.getDefault()).symbol
        "$currencySymbol${"%.2f".format(amount)}"
    }
}