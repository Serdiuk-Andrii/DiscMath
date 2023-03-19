package com.example.discmath.util

import com.google.firebase.storage.FirebaseStorage

class FileDownloadManager (
    val callback: (downloadedFile: ByteArray) -> Unit): Downloader {

    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val MAXIMUM_DOWNLOAD_SIZE: Long = 30 * 1024 * 1024

    override fun downloadFile(urlString: String) {
        storage.getReferenceFromUrl(urlString).getBytes(MAXIMUM_DOWNLOAD_SIZE).
        addOnSuccessListener {
            callback(it)
        }
    }
}