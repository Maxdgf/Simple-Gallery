package com.example.simplegallery.ui.states

import com.example.simplegallery.gallery_resolver.data_models.MediaItem

sealed class MediaListResult {
    data class MediaList(val media: Map<String, List<MediaItem>>) : MediaListResult()
    object Empty : MediaListResult()
    object Loading : MediaListResult()
}