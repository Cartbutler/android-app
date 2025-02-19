package com.example.cartbutler.components

import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.icu.util.Currency
import android.icu.util.ULocale

fun formatCurrency(amount: Float): String {
    return try {
        NumberFormatter.withLocale(ULocale.CANADA)
            .unit(Currency.getInstance("CAD"))
            .precision(Precision.fixedFraction(2))
            .format(amount.toDouble())
            .toString()
    } catch (e: Exception) {
        "$${"%.2f".format(amount)}"
    }
}