package com.example.cartbutler.network

import android.content.Context

object LocaleHelper {
    fun currentLanguage(context: Context): String {
        val locale = context.resources.configuration.locales[0]
        val language = locale?.language?.takeIf { it.isNotEmpty() } ?: "en"
        val country = locale?.country?.takeIf { it.isNotEmpty() } ?: if (language == "pt") "BR" else "US"
        return "$language-$country"
    }
}