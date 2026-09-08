package com.example.ui.renderer

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.data.model.QuoteCardSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object ExportManager {

    suspend fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        title: String = "Quote_${System.currentTimeMillis()}"
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$title.png")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/QuoteStudio")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: return@withContext Result.failure(Exception("Failed to create MediaStore entry"))

            resolver.openOutputStream(imageUri)?.use { outputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                    return@withContext Result.failure(Exception("Failed to compress bitmap"))
                }
            } ?: return@withContext Result.failure(Exception("Failed to open output stream"))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(imageUri, contentValues, null, null)
            }

            Result.success(imageUri)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun shareBitmap(
        context: Context,
        bitmap: Bitmap,
        title: String = "Quote Studio"
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val cacheFolder = File(context.cacheDir, "shared_quotes")
            if (!cacheFolder.exists()) {
                cacheFolder.mkdirs()
            }
            val file = File(cacheFolder, "quote_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_TEXT, "Created with Quote Studio ✨")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Share Quote via")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun exportBatchToGallery(
        context: Context,
        specs: List<QuoteCardSpec>,
        targetSize: Int = 1080,
        onProgress: (Int, Int) -> Unit = { _, _ -> }
    ): List<Uri> = withContext(Dispatchers.IO) {
        val savedUris = mutableListOf<Uri>()
        specs.forEachIndexed { index, spec ->
            val bitmap = QuoteBitmapRenderer.renderToBitmap(context, spec, targetSize)
            val result = saveBitmapToGallery(context, bitmap, "QuoteStudio_Batch_${index + 1}_${System.currentTimeMillis()}")
            result.getOrNull()?.let { savedUris.add(it) }
            onProgress(index + 1, specs.size)
        }
        savedUris
    }
}
