package com.example.simplegallery.gallery_resolver.repository

import com.example.simplegallery.gallery_resolver.data_models.MediaItem
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getImages(): List<MediaItem>
    fun observeImages(): Flow<List<MediaItem>>
}