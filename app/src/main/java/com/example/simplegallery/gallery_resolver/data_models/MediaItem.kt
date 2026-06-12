package com.example.simplegallery.gallery_resolver.data_models

import android.net.Uri

data class MediaItem(
    val id: Long,
    val name: String,
    val dateTaken: Long,
    val uri: Uri,
    val width: Int,
    val height: Int
)
