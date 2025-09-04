package com.example.sejongapp.utils

import android.content.Context
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, lang: String): Context {
        val locale = when (lang) {
            "KOR" -> Locale("ko")
            "ENG" -> Locale("en")
            "RUS" -> Locale("ru")
            "TAJ" -> Locale("tj")
            else -> Locale.getDefault()
        }

        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}
