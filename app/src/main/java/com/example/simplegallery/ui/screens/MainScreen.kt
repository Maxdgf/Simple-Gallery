package com.example.simplegallery.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest

import com.example.simplegallery.R
import com.example.simplegallery.ui.components.LoadingUiBlock
import com.example.simplegallery.ui.components.NoDataUiDescriptionBlock
import com.example.simplegallery.ui.screens.navigation.NavigationRoutes
import com.example.simplegallery.ui.states.MediaListResult
import com.example.simplegallery.ui.viewmodels.GalleryState

@Composable
private fun ImageItem(
    uri: Uri,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val imageRequest = remember(uri) {
        ImageRequest.Builder(context)
            .data(uri)
            .size(250, 250)
            .build()
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(1f)
            .border(
                width = 0.15.dp,
                color =
                    if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.5f)
                    else Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(2.dp)
            )
            .clip(RoundedCornerShape(2.dp))
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    onNavigateTo: (String) -> Unit,
    galleryState: GalleryState
) {
    val media by galleryState.allMedia.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val mediaState = media) {
                is MediaListResult.MediaList -> {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color =
                                    if (isSystemInDarkTheme()) Color.Black
                                    else Color.White
                            ),
                        contentPadding = PaddingValues(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        columns = GridCells.Fixed(5)
                    ) {
                        mediaState.media.forEach { (date, media) ->
                            item(span = { GridItemSpan(5) }) {
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            stickyHeader {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(color = if (isSystemInDarkTheme()) Color.Black else Color.White)
                                    ) {
                                        Text(
                                            text = date,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = "media: ${media.size}",
                                            fontSize = 12.sp,
                                            color =
                                                if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.5f)
                                                else Color.Black.copy(alpha = 0.5f)
                                        )
                                    }

                                    HorizontalDivider()
                                }
                            }

                            items(
                                items = media,
                                key = { item -> item.id }
                            ) { image ->
                                ImageItem(
                                    uri = image.uri,
                                    onClick = {
                                        onNavigateTo(NavigationRoutes.ViewMediaScreen.route)
                                        galleryState.updateMediaId(image.id)
                                    }
                                )
                            }
                        }
                    }
                }

                is MediaListResult.Empty -> {
                    NoDataUiDescriptionBlock(
                        description = "No media on your device :(",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is MediaListResult.Loading -> {
                    LoadingUiBlock(
                        description = "Loading media...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}