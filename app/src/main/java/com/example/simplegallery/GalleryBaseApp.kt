package com.example.simplegallery


import android.app.Application

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.crossfade

import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class GalleryBaseApp : Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context)
            .crossfade(1000)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.10)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.30)
                    .build()
            }
            .build()
}