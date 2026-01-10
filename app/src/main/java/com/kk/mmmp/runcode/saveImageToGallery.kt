package com.kk.mmmp.runcode

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

data class SaveResult(val success: Boolean, val message: String? = null)

suspend fun saveImageToGallery(context: Context, imagePath: String): SaveResult {
    return withContext(Dispatchers.IO) {
        try {
            val imageFile = File(imagePath)
            val filename = imageFile.name
            val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val contentResolver = context.contentResolver
            val uri = contentResolver.insert(imageCollection, contentValues)
                ?: return@withContext SaveResult(false, "Failed to create MediaStore entry")

            contentResolver.openOutputStream(uri)?.use { outputStream ->
                FileInputStream(imageFile).use { inputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            } ?: return@withContext SaveResult(false, "Failed to open output stream")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }

            SaveResult(true, "Image saved successfully")
        } catch (e: Exception) {
            SaveResult(false, "Error occurred: ${e.message}")
        }
    }
}