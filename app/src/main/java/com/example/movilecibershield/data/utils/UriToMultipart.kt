package com.example.movilecibershield.data.utils


import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


fun uriToMultipart(
    context: Context,
    uri: Uri
): MultipartBody.Part {

    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)!!
    val bytes = inputStream.readBytes()

    val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(
        name = "imageUser",
        filename = "profile.jpg",
        body = requestBody
    )
}

