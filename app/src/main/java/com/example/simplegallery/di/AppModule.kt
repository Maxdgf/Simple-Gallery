package com.example.simplegallery.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.example.simplegallery.gallery_resolver.repository.GalleryRepository
import com.example.simplegallery.gallery_resolver.repository.GalleryRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideGalleryRepository(@ApplicationContext context: Context): GalleryRepository =
        GalleryRepositoryImpl(context)
}