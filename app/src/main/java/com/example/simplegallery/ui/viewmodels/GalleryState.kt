package com.example.simplegallery.ui.viewmodels

import android.content.ContentUris
import android.provider.MediaStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

import com.example.simplegallery.ui.states.MediaListResult
import com.example.simplegallery.gallery_resolver.repository.GalleryRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull

@HiltViewModel
class GalleryState @Inject constructor(
    galleryRepository: GalleryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private companion object {
        const val CURRENT_MEDIA_KEY = "current_media"
    }

    val currentMediaId: StateFlow<Long?> = savedStateHandle.getStateFlow(CURRENT_MEDIA_KEY, null)

    val currentMediaUri = currentMediaId
        .filterNotNull()
        .map { id ->
            ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val allMedia = galleryRepository.observeImages()
        .map { list ->
            val groupedMediaList = withContext(Dispatchers.Default) {
                list.groupBy { media ->
                    val instant = Instant.ofEpochMilli(media.dateTaken).atZone(ZoneId.systemDefault())
                    "${instant.dayOfMonth} ${instant.month.toString().lowercase()} ${instant.year}"
                }
            }

            if (groupedMediaList.isNotEmpty())
                MediaListResult.MediaList(groupedMediaList)
            else
                MediaListResult.Empty
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MediaListResult.Loading
        )

    fun updateMediaId(id: Long) {
        savedStateHandle[CURRENT_MEDIA_KEY] = id
    }
}