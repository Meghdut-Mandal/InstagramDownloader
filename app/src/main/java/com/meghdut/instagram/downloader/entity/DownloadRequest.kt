package com.meghdut.instagram.downloader.entity

data class DownloadRequest(
    val userName: String,
    val description: String,
    var downloadItems: List<DownloadItem> = listOf()
)

data class DownloadItem(val fileName: String, val downloadUrl: String)
