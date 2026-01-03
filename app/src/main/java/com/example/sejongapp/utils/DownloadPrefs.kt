package com.example.sejongapp.utils

import android.content.Context

object DownloadPrefs {
    private const val PREF = "downloads"


    fun saveId(context: Context, fileUrl: String, downloadId: Long) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putLong(fileUrl, downloadId)
            .apply()
    }

    fun getId(context: Context, fileUrl: String): Long? {
        val id = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getLong(fileUrl, -1L)
        return if (id == -1L) null else id
    }
}

