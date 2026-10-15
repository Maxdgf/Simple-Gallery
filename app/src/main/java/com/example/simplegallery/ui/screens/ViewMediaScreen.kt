package com.example.simplegallery.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.example.simplegallery.R
import com.example.simplegallery.ui.screens.navigation.NavigationRoutes
import com.example.simplegallery.ui.states.MediaListResult
import com.example.simplegallery.ui.viewmodels.GalleryState

@Composable
private fun ImageItem(
    uri: Uri,
    selectedBorderColor: Color,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val imageRequest = remember(uri) {
        ImageRequest.Builder(context)
            .data(uri)
            .size(50, 50)
            .build()
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = null,
        modifier = Modifier
            .width(30.dp)
            .height(40.dp)
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(2.dp))
            .border(
                width = 2.dp,
                color = selectedBorderColor,
                shape = RoundedCornerShape(1.dp)
            ),
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMediaAppScreen(
    onNavigateTo: (String) -> Unit,
    galleryState: GalleryState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color =
                    if (isSystemInDarkTheme()) Color.Black
                    else Color.White
            )
    ) {
        val currentMediaUri by galleryState.currentMediaUri.collectAsState()

        AsyncImage(
            model = currentMediaUri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    color = (if (isSystemInDarkTheme()) Color.Black else Color.White).copy(alpha = 0.7f)
                ),
            verticalAlignment = Alignment.Bottom
        ) {
            IconButton(onClick = { onNavigateTo(NavigationRoutes.MainScreen.route) }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = null,
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(105.dp)
                .background(
                    color = (if (isSystemInDarkTheme()) Color.Black else Color.White).copy(alpha = 0.7f)
                )
        ) {
            val allMedia by galleryState.allMedia.collectAsState()

            when (val state = allMedia) {
                is MediaListResult.MediaList -> {
                    val currentMediaId by galleryState.currentMediaId.collectAsState()
                    val listState = rememberLazyListState()

                    LaunchedEffect(Unit) {
                        val currentMediaIndex = withContext(Dispatchers.Default) {
                            state.media.values.flatten().indexOfFirst { currentMediaId == it.id }
                        }
                        listState.scrollToItem(currentMediaIndex)
                    }

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(top = 10.dp)
                            .align(Alignment.TopStart),
                        state = listState,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(
                            items = state.media.values.flatten(),
                            key = { media -> media.id }
                        ) { media ->
                            ImageItem(
                                uri = media.uri,
                                selectedBorderColor = if (currentMediaId == media.id) Color.Green else Color.Unspecified,
                                onClick = { galleryState.updateMediaId(media.id) }
                            )
                        }
                    }
                }
                else -> {}
            }
        }
    }
}