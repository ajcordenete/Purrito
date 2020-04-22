package com.aljon.purrito.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.bumptech.glide.load.resource.gif.GifDrawable
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.file.Files


object MediaHelper {

    fun getFileExtension(file: String): String {
        return file.substringAfterLast(".", "")
    }

    fun isGif(file: String) : Boolean {
        return (getFileExtension(file) == Constants.GIF_TYPE)
    }

    fun isImage(file: String): Boolean {
        return (Constants.IMAGE_FILE_TYPE.contains(getFileExtension(file)))
    }

    fun saveFile(context: Context, bytes: ByteArray, mime_type: String, file_extension: String): Boolean {
        val name = "download_" + System.currentTimeMillis()

        if( android.os.Build.VERSION.SDK_INT >= 29 ) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.MIME_TYPE, mime_type)
                put(MediaStore.Images.Media.RELATIVE_PATH, Constants.DOWNLOADED_FOLDER_PATH)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val uri = context.contentResolver.insert(collection, values)

            uri?.let {
                context.contentResolver?.openOutputStream(uri).use { out ->
                    out?.write(bytes, 0 ,bytes.size)
                    out?.close()
                }

                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                context?.contentResolver?.update(uri, values, null, null)
                return true
            }
        } else {
            val nameWithExtension = "$name.$file_extension"
            //Ignore deprecated as this will only be performed on devices with OS lower than Android SDK 29...
            val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/Purrito")

            var success = true
            if (!storageDir.exists()) {
                success = storageDir.mkdirs();
            }
            if (success) {
                val imageFile = File(storageDir, nameWithExtension)

                val out = FileOutputStream(imageFile)
                out.write(bytes, 0, bytes.size)
                out.close()

                // Refresh the Gallery
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = Uri.fromFile(imageFile)
                context.sendBroadcast(mediaScanIntent)

                return true

            }
        }

        return false
    }

    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray()
    }

    fun getBytesFromGifDrawable(gifDrawable: GifDrawable): ByteArray {
        val byteBuffer = gifDrawable.buffer
        val bytes = ByteArray(byteBuffer.capacity())
        (byteBuffer.duplicate().clear() as ByteBuffer).get(bytes)
        return bytes
    }
}