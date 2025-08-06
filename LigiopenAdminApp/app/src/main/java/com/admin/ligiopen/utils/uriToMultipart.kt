package com.admin.ligiopen.utils

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

fun uriToMultipart(uri: Uri, context: Context): MultipartBody.Part {

    var file: MultipartBody.Part? = null

    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
    parcelFileDescriptor?.let { pfd ->
        val inputStream = FileInputStream(pfd.fileDescriptor)
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while(inputStream.read(buffer).also { length = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, length)
        }
        val byteArray = byteArrayOutputStream.toByteArray()

        //Get the MIME type of the file

        val mimeType = context.contentResolver.getType(uri)
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), byteArray)
        val imagePart = MultipartBody.Part.createFormData("file", "upload.$extension", requestFile)
        file = imagePart
    }

    return file!!

}