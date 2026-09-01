package com.example.simplegallery.gallery_resolver.repository

import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.provider.MediaStore
import com.example.simplegallery.gallery_resolver.data_models.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(context: Context) : GalleryRepository {
    private val contentResolver = context.contentResolver

    override fun getImages(): List<MediaItem> {
        // projection
        val projection = arrayOf(
            MediaStore.Images.Media._ID,          // image id
            MediaStore.Images.Media.DISPLAY_NAME, // image name
            MediaStore.Images.Media.DATE_TAKEN,   // image date taken
            MediaStore.Images.Media.WIDTH,        // image width
            MediaStore.Images.Media.HEIGHT        // image height
        )

        // cursor
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_TAKEN + " DESC"
        )

        val images = mutableListOf<MediaItem>()

        cursor?.use { cur ->
            if (cur.moveToFirst()) {
                val idIndex = cur.getColumnIndex(MediaStore.Images.Media._ID)
                val nameIndex = cur.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val dateTakenIndex = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
                val widthIndex = cur.getColumnIndex(MediaStore.Images.Media.WIDTH)
                val heightIndex = cur.getColumnIndex(MediaStore.Images.Media.HEIGHT)

                do {
                    val id = cur.getLong(idIndex)
                    val name = cur.getString(nameIndex)
                    val dateTaken = cur.getLong(dateTakenIndex)
                    val width = cur.getInt(widthIndex)
                    val height = cur.getInt(heightIndex)

                    val imageUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    val media = MediaItem(
                        id = id,
                        name = name,
                        dateTaken = dateTaken,
                        uri = imageUri,
                        width = width,
                        height = height
                    )

                    // add image item
                    images.add(media)
                } while (cur.moveToNext())
            }
        }

        return images
    }

    override fun observeImages(): Flow<List<MediaItem>> =
        callbackFlow {
            val mediaObserver = object : ContentObserver(null) {
                override fun onChange(selfChange: Boolean) {
                    super.onChange(selfChange)
                    trySend(getImages())
                }
            }

            contentResolver.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                mediaObserver
            )

            trySend(getImages())

            awaitClose {
                contentResolver.unregisterContentObserver(mediaObserver)
            }
        }.flowOn(Dispatchers.IO)
}