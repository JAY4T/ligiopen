package com.jabulani.ligiopen.ui.inapp.videos

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jabulani.ligiopen.ui.inapp.videos.config.youtubeVideos
import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth

object AllVideosScreenDestination: AppNavigation {
    override val title: String = "All videos"
    override val route: String = "all-videos-screen"
}

@Composable
fun AllVideosScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToSingleVideoScreen: (videoId: String, videoTitle: String, videoDate: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        AllVideosScreen(
            navigateToSingleVideoScreen = navigateToSingleVideoScreen,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}

@Composable
fun AllVideosScreen(
    navigateToPreviousScreen: () -> Unit,
    navigateToSingleVideoScreen: (videoId: String, videoTitle: String, videoDate: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var error = ""
    var showDialog by remember {
        mutableStateOf(false)
    }

    if(showDialog) {
        AlertDialog(
            title = {
                Text(text = "Error")
            },
            text = {
                Text(text = error)
            },
            onDismissRequest = {
                showDialog = false
            },
            dismissButton = {
                showDialog = false
            },
            confirmButton = {
                showDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(16.0),
                horizontal = screenWidth(16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = navigateToPreviousScreen
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(4.0)))
            Text(
                text = "Videos",
                fontSize = screenFontSize(16.0).sp
            )
        }
        LazyColumn {
            items(youtubeVideos.size) { index ->
                SingleVideoCard(
                    videoId = youtubeVideos[index].videoId,
                    title = youtubeVideos[index].videoTitle,
                    date = youtubeVideos[index].videoDate,
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                try {
                                    navigateToSingleVideoScreen(
                                        youtubeVideos[index].videoId,
                                        youtubeVideos[index].videoTitle,
                                        youtubeVideos[index].videoDate
                                    )

                                } catch (e: Exception) {
                                    error = e.toString()
                                    showDialog = true
//                                    Log.e("videoLoadErr", "${e.message}")

                                }

                            }
                        )
                )
                Spacer(modifier = Modifier.height(screenHeight(16.0)))
            }
        }
    }
}

@Composable
fun SingleVideoCard(
    videoId: String,
    title: String,
    date: String,
    modifier: Modifier = Modifier,
//    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
//            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp))
        ) {
            // YouTube thumbnail
            AsyncImage(
                model = "https://img.youtube.com/vi/$videoId/0.jpg",
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Play button overlay
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.9f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.Red,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AllVideosScreenPreview() {
    AllVideosScreen(
        navigateToSingleVideoScreen = {videoId, videoTitle, videoDate ->},
        navigateToPreviousScreen = {}
    )
}