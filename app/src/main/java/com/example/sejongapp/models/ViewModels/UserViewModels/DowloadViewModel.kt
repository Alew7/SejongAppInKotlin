package com.example.sejongapp.models.ViewModels.UserViewModels

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.utils.DownloadPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class DownloadViewModel : ViewModel() {


    var progressMap = mutableStateMapOf<String, Int>()
    var isDownloadingMap = mutableStateMapOf<String, Boolean>()
    var isDownloadedMap = mutableStateMapOf<String, Boolean>()


    fun startDownload(context: Context, url: String, fileName: String) {

        if (isDownloadingMap[url] == true || isDownloadedMap[url] == true)
            return

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        try {
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(fileName)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)


            val id = dm.enqueue(request)

            // Сохраняем ID загрузки в префы, привязывая к URL
            DownloadPrefs.saveId(context, url, id)

            isDownloadingMap[url] = true
            isDownloadedMap[url] = false
            observeProgress(dm, id, url)

        } catch (e: Exception) {
            e.printStackTrace()
            isDownloadingMap[url] = false
        }
    }

    /**
     * Отслеживание прогресса в реальном времени
     */
    private fun observeProgress(dm: DownloadManager, id: Long, url: String) {
        viewModelScope.launch {
            var downloading = true
            while (downloading) {
                val query = DownloadManager.Query().setFilterById(id)
                val cursor = dm.query(query)

                if (cursor != null && cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))

                    // Считаем проценты
                    val downloaded = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val total = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                    if (total > 0) {
                        progressMap[url] = ((downloaded * 100) / total).toInt()
                    }

                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            isDownloadingMap[url] = false
                            isDownloadedMap[url] = true
                            downloading = false
                        }
                        DownloadManager.STATUS_FAILED -> {
                            isDownloadingMap[url] = false
                            isDownloadedMap[url] = false
                            downloading = false
                        }
                    }
                } else {

                    downloading = false
                    isDownloadingMap[url] = false
                }
                cursor?.close()
                delay(500)
            }
        }
    }


    //  Восстановление статуса при открытии экрана или перезапуске приложения

    fun restoreDownload(context: Context, url: String, fileName: String) {
        // 1. ПЕРВАЯ ПРОВЕРКА: Есть ли файл физически в папке Downloads?
        if (!isFileExists(context, fileName)) {
            isDownloadedMap[url] = false
            isDownloadingMap[url] = false
            progressMap[url] = 0
            return // Файла нет — даем пользователю возможность скачать снова
        }

        // 2. ВТОРАЯ ПРОВЕРКА: Если файл есть, проверяем, что о нем знает система
        val savedId = DownloadPrefs.getId(context, url)
        if (savedId == null) {

            isDownloadedMap[url] = true
            return
        }

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(savedId)
        val cursor = dm.query(query)

        if (cursor != null && cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))

            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    isDownloadedMap[url] = true
                    isDownloadingMap[url] = false
                }
                DownloadManager.STATUS_RUNNING, DownloadManager.STATUS_PENDING -> {
                    isDownloadingMap[url] = true
                    isDownloadedMap[url] = false
                    observeProgress(dm, savedId, url)
                }
                else -> {
                    isDownloadedMap[url] = false
                    isDownloadingMap[url] = false
                }
            }
        } else {

            isDownloadedMap[url] = true
        }
        cursor?.close()
    }


//      Проверка: существует ли файл в папке загрузок

    private fun isFileExists(context: Context, fileName: String): Boolean {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )
        return file.exists()
    }



// ... внутри класса DownloadViewModel ...

    fun openFile(context: Context, fileName: String) {
        // Находим файл в папке Downloads
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )

        if (file.exists()) {
            // Создаем безопасный URI (ссылку) для других приложений
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Даем право на чтение
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Нет приложения для открытия PDF", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Файл не найден на устройстве", Toast.LENGTH_SHORT).show()
        }
    }
}