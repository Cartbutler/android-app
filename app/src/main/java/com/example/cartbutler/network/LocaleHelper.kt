package com.example.cartbutler.network

import android.content.Context

object LocaleHelper {
    fun currentLanguage(context: Context): String {
        return when (context.resources.configuration.locales[0]?.language ?: "en") {
            "pt" -> "pt-BR"
            else -> "en-US"
        }
    }
}