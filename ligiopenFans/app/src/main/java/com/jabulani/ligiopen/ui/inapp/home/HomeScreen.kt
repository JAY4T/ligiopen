package com.jabulani.ligiopen.ui.inapp.home

import YouTubePlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.data.network.model.match.fixture.MatchStatus
import com.jabulani.ligiopen.data.network.model.match.fixture.fixtures
import com.jabulani.ligiopen.data.network.model.news.NewsDto
import com.jabulani.ligiopen.data.network.model.news.news
import com.jabulani.ligiopen.ui.inapp.news.NewsTile
import com.jabulani.ligiopen.ui.inapp.videos.SingleVideoCard
import com.jabulani.ligiopen.ui.inapp.videos.config.YoutubeVideo
import com.jabulani.ligiopen.ui.inapp.videos.config.youtubeVideo
import com.jabulani.ligiopen.ui.inapp.videos.config.youtubeVideos

import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.ui.theme.errorLight
import com.jabulani.ligiopen.ui.theme.onErrorLight
import com.jabulani.ligiopen.ui.theme.onPrimaryLight
import com.jabulani.ligiopen.ui.theme.onSurfaceLight
import com.jabulani.ligiopen.ui.theme.onSurfaceVariantLight
import com.jabulani.ligiopen.ui.theme.onTertiaryLight
import com.jabulani.ligiopen.ui.theme.primaryLight
import com.jabulani.ligiopen.ui.theme.surfaceContainerHighLight
import com.jabulani.ligiopen.ui.theme.surfaceVariantLight
import com.jabulani.ligiopen.ui.theme.tertiaryLight
import com.jabulani.ligiopen.ui.utils.formatIsoDateTime
import com.jabulani.ligiopen.ui.utils.video.FullscreenVideoScreen
import com.jabulani.ligiopen.utils.reusables.composables.AutoVideoPlayer
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth
import java.time.LocalDateTime

object HomeScreenDestination : AppNavigation {
    override val title: String = "Home screen"
    override val route: String = "home-screen"

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenComposable(
    fixtures: List<FixtureData>,
    news: List<NewsDto>,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    navigateToAllVideosScreen: () -> Unit,
    navigateToSingleVideoScreen: (videoId: String, videoTitle: String, videoDate: String) -> Unit,
    navigateToNewsDetailsScreen: (newsId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()


    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        HomeScreen(
            fixtures = fixtures,
            news = news,
            navigateToPostMatchScreen = navigateToPostMatchScreen,
            navigateToSingleVideoScreen = navigateToSingleVideoScreen,
            navigateToAllVideosScreen = navigateToAllVideosScreen,
            navigateToNewsDetailsScreen = navigateToNewsDetailsScreen
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    fixtures: List<FixtureData>,
    news: List<NewsDto>,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    navigateToAllVideosScreen: () -> Unit,
    navigateToSingleVideoScreen: (videoId: String, videoTitle: String, videoDate: String) -> Unit,
    navigateToNewsDetailsScreen: (newsId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Theme colors
    val primaryColor = primaryLight
    val surfaceColor = surfaceContainerHighLight
    val onSurfaceColor = onSurfaceLight
    val accentColor = tertiaryLight

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with game-style gradient
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.8f),
                            primaryColor.copy(alpha = 0.4f)
                        )
                    )
                )
                .padding(horizontal = screenWidth(16.0), vertical = screenHeight(12.0))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ligiopen_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(screenWidth(48.0))
                    .clip(RoundedCornerShape(screenWidth(8.0)))
                    .border(screenWidth(2.0), accentColor, RoundedCornerShape(screenWidth(8.0)))
            )

            Spacer(modifier = Modifier.width(screenWidth(12.0)))

            Text(
                text = HomeScreenTab.HOME.name
                    .lowercase()
                    .replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineMedium,
                color = onPrimaryLight,
                fontWeight = FontWeight.Black,
                letterSpacing = screenFontSize(1.0).sp,
                modifier = Modifier
                    .shadow(screenWidth(4.0), shape = RoundedCornerShape(screenWidth(4.0)))
            )

            Spacer(modifier = Modifier.weight(1f))

            // Notification bell with badge
            Box {
                IconButton(
                    onClick = { /* Handle notifications */ },
                    modifier = Modifier
                        .size(screenWidth(40.0))
                        .background(accentColor, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.notification),
                        contentDescription = "Notifications",
                        tint = onTertiaryLight
                    )
                }

                // Notification badge
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .size(16.dp)
//                        .background(errorLight, CircleShape)
//                        .border(2.dp, surfaceColor, CircleShape)
//                ) {
//                    Text(
//                        text = "3",
//                        color = onErrorLight,
//                        fontSize = 10.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
            }
        }

        // Main content area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = screenWidth(8.0))
        ) {
            // Upcoming Matches section
            SectionHeader(
                title = "Upcoming Matches",
                actionText = "View All",
                onActionClick = { /* TODO */ },
                modifier = Modifier.padding(top = screenHeight(16.0))
            )

            Spacer(modifier = Modifier.height(screenHeight(8.0)))

            if (fixtures.isEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(screenWidth(12.0)),
                    contentPadding = PaddingValues(horizontal = screenWidth(8.0))
                ) {
                    items(3) { index ->
                        // Shimmer placeholder card
                        Card(
                            modifier = Modifier
                                .width(screenWidth(280.0))
                                .height(screenHeight(180.0)),
                            shape = RoundedCornerShape(screenWidth(12.0)),
                            elevation = CardDefaults.cardElevation(screenHeight(4.0)),
                            colors = CardDefaults.cardColors(
                                containerColor = surfaceVariantLight
                            ),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
//                                    .shimmerEffect()
                                    .padding(screenWidth(16.0))
                            ) {
                                // Placeholder status
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(screenHeight(24.0))
                                        .background(
                                            color = surfaceContainerHighLight,
                                            shape = RoundedCornerShape(screenWidth(4.0))
                                        )
                                )

                                Spacer(modifier = Modifier.height(screenHeight(12.0)))

                                // Placeholder teams and score
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Home team placeholder
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(screenWidth(48.0))
                                                .clip(CircleShape)
                                                .background(surfaceContainerHighLight)
                                        )
                                        Spacer(modifier = Modifier.height(screenHeight(8.0)))
                                        Box(
                                            modifier = Modifier
                                                .width(screenWidth(60.0))
                                                .height(screenHeight(16.0))
                                                .background(surfaceContainerHighLight)
                                        )
                                        Spacer(modifier = Modifier.height(screenHeight(4.0)))
                                        Box(
                                            modifier = Modifier
                                                .width(screenWidth(40.0))
                                                .height(screenHeight(12.0))
                                                .background(surfaceContainerHighLight)
                                        )
                                    }

                                    // Score placeholder
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(0.8f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(screenWidth(80.0))
                                                .height(screenHeight(24.0))
                                                .background(surfaceContainerHighLight)
                                        )
                                        Spacer(modifier = Modifier.height(screenHeight(4.0)))
                                        Box(
                                            modifier = Modifier
                                                .width(screenWidth(120.0))
                                                .height(screenHeight(14.0))
                                                .background(surfaceContainerHighLight)
                                        )
                                    }

                                    // Away team placeholder
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(screenWidth(48.0))
                                                .clip(CircleShape)
                                                .background(surfaceContainerHighLight)
                                        )
                                        Spacer(modifier = Modifier.height(screenHeight(8.0)))
                                        Box(
                                            modifier = Modifier
                                                .width(screenWidth(60.0))
                                                .height(screenHeight(16.0))
                                                .background(surfaceContainerHighLight)
                                        )
                                        Spacer(modifier = Modifier.height(screenHeight(4.0)))
                                        Box(
                                            modifier = Modifier
                                                .width(screenWidth(40.0))
                                                .height(screenHeight(12.0))
                                                .background(surfaceContainerHighLight)
                                        )
                                    }
                                }

                                // Glowing accent placeholder
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(screenHeight(2.0))
                                        .background(surfaceContainerHighLight)
                                )
                            }
                        }
                    }
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(screenWidth(12.0)),
                    contentPadding = PaddingValues(horizontal = screenWidth(8.0))
                ) {
                    items(fixtures) { fixture ->
                        FixtureCard(
                            fixtureData = fixture,
                            onClick = {
                                navigateToPostMatchScreen(
                                    fixture.postMatchAnalysisId.toString(),
                                    fixture.matchFixtureId.toString(),
                                    fixture.matchLocationId.toString()
                                )
                            },
                            modifier = Modifier.width(screenWidth(280.0))
                        )
                    }
                }
            }



            Spacer(modifier = Modifier.height(screenHeight(24.0)))

            // Featured Video section
            SectionHeader(
                title = "Featured Video",
                actionText = "Watch",
                onActionClick = {
                    navigateToSingleVideoScreen(
                        youtubeVideo.videoId,
                        youtubeVideo.videoTitle,
                        youtubeVideo.videoDate
                    )
                }
            )

            Spacer(modifier = Modifier.height(screenHeight(8.0)))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidth(8.0)),
                shape = RoundedCornerShape(screenWidth(16.0)),
                elevation = CardDefaults.cardElevation(screenHeight(8.0))
            ) {
                Box {
                    YouTubePlayer(
                        youtubeVideoId = "mUki-_cLdsQ",
                        lifecycleOwner = LocalLifecycleOwner.current,
                        shouldMute = true,
                        shouldAutoPlay = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f/9f)
                    )

//                    // Play button overlay
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(Color.Black.copy(alpha = 0.3f))
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.play),
//                            contentDescription = "Play",
//                            tint = Color.White,
//                            modifier = Modifier
//                                .size(64.dp)
//                                .align(Alignment.Center)
//                        )
//                    }
                }
            }

            Spacer(modifier = Modifier.height(screenHeight(8.0)))

            Text(
                text = youtubeVideo.videoTitle,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = screenWidth(16.0))
            )

            Spacer(modifier = Modifier.height(screenHeight(4.0)))

            Text(
                text = youtubeVideo.videoDate,
                color = onSurfaceVariantLight,
                fontSize = screenFontSize(12.0).sp,
                modifier = Modifier.padding(horizontal = screenWidth(16.0))
            )

            Spacer(modifier = Modifier.height(screenHeight(24.0)))

            // Video Highlights section
            SectionHeader(
                title = "Video Highlights",
                actionText = "View All",
                onActionClick = navigateToAllVideosScreen
            )

            Spacer(modifier = Modifier.height(screenHeight(8.0)))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(screenWidth(12.0)),
                contentPadding = PaddingValues(horizontal = screenWidth(8.0))
            ) {
                items(youtubeVideos.take(5)) { video ->
                    VideoHighlightCard(
                        video = video,
                        onClick = {
                            navigateToSingleVideoScreen(
                                video.videoId,
                                video.videoTitle,
                                video.videoDate
                            )
                        },
                        modifier = Modifier.width(screenWidth(240.0))
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeight(24.0)))

            // Latest News section
            SectionHeader(
                title = "Latest News",
                actionText = "View All",
                onActionClick = { /* TODO */ }
            )

            Spacer(modifier = Modifier.height(screenHeight(8.0)))

            if (news.isNotEmpty()) {
                // Featured news item
                NewsTile(
                    news = news[0],
                    isFeatured = true,
                    onClick = { navigateToNewsDetailsScreen(news[0].id.toString()) },
                    modifier = Modifier.padding(horizontal = screenWidth(8.0))
                )

                Spacer(modifier = Modifier.height(screenHeight(16.0)))

                // Other news items
                news.drop(1).take(3).forEach { newsItem ->
                    NewsTile(
                        news = newsItem,
                        isFeatured = false,
                        onClick = { navigateToNewsDetailsScreen(newsItem.id.toString()) }
                    )
                    Spacer(modifier = Modifier.height(screenHeight(16.0)))
                }
//                LazyColumn(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.spacedBy(16.dp),
//                    contentPadding = PaddingValues(horizontal = 8.dp)
//                ) {
//                    items(news.drop(1).take(3)) { newsItem ->
//                        NewsTile(
//                            news = newsItem,
//                            isFeatured = false,
//                            onClick = { navigateToNewsDetailsScreen(newsItem.id.toString()) }
//                        )
//                    }
//                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight(120.0))
                ) {
                    Text(
                        text = "No news available",
                        color = onSurfaceVariantLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeight(24.0)))
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidth(8.0))
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = primaryLight,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onActionClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = primaryLight
            )
        ) {
            Text(text = actionText)
            Spacer(modifier = Modifier.width(screenWidth(4.0)))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(screenWidth(16.0))
            )
        }
    }
}

@Composable
private fun VideoHighlightCard(
    video: YoutubeVideo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = primaryLight
    val surfaceColor = surfaceContainerHighLight
    val onSurfaceColor = onSurfaceLight
    val accentColor = tertiaryLight

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(screenWidth(12.0)),
        elevation = CardDefaults.cardElevation(screenWidth(4.0))
    ) {
        Column {
            // Thumbnail container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f/9f)
            ) {
                // Video thumbnail
                AsyncImage(
                    model = "https://img.youtube.com/vi/${video.videoId}/0.jpg",
                    contentDescription = video.videoTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data("https://img.youtube.com/vi/${video.videoId}/maxresdefault.jpg") // YouTube thumbnail URL
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = video.videoTitle,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize()
//                )

                // Dark overlay for better play button visibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f))
                )

                // Play button overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.play2),
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier
                            .size(screenWidth(48.0))
                            .align(Alignment.Center)
                            .shadow(screenWidth(4.0))
                    )
                }

                // Duration badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(screenWidth(8.0))
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(screenWidth(4.0)))
                        .padding(horizontal = screenWidth(6.0), vertical = screenHeight(4.0))
                ) {
                    Text(
                        text = "2:45", // You might want to get actual duration from your data
                        color = Color.White,
                        fontSize = screenFontSize(12.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Video info
            Column(
                modifier = Modifier.padding(screenWidth(12.0))
            ) {
                Text(
                    text = video.videoTitle,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(screenHeight(4.0)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = "Duration",
                        tint = onSurfaceVariantLight,
                        modifier = Modifier.size(screenWidth(14.0))
                    )
                    Spacer(modifier = Modifier.width(screenWidth(4.0)))
                    Text(
                        text = video.videoDate,
                        color = onSurfaceVariantLight,
                        fontSize = screenFontSize(12.0).sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.play_views),
                        contentDescription = "Views",
                        tint = onSurfaceVariantLight,
                        modifier = Modifier.size(screenWidth(14.0))
                    )
                    Spacer(modifier = Modifier.width(screenWidth(4.0)))
                    Text(
                        text = "1.2K", // Example view count
                        color = onSurfaceVariantLight,
                        fontSize = screenFontSize(12.0).sp
                    )
                }
            }

            // Glowing accent at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(2.0))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.3f),
                                primaryColor,
                                primaryColor.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun FixtureCard(
    fixtureData: FixtureData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = primaryLight
    val surfaceColor = surfaceContainerHighLight
    val onSurfaceColor = onSurfaceLight
    val accentColor = tertiaryLight
    val matchStatusColor = when (fixtureData.matchStatus) {
        MatchStatus.PENDING -> Color(0xFF9E9E9E)
        MatchStatus.COMPLETED -> Color(0xFF4CAF50)
        MatchStatus.CANCELLED -> errorLight
        MatchStatus.FIRST_HALF -> Color(0xFFFFC107)
        MatchStatus.HALF_TIME -> Color(0xFFFF9800)
        MatchStatus.SECOND_HALF -> Color(0xFFFFC107)
        MatchStatus.EXTRA_TIME_FIRST_HALF -> Color(0xFFFF5722)
        MatchStatus.EXTRA_TIME_HALF_TIME -> Color(0xFFFF9800)
        MatchStatus.EXTRA_TIME_SECOND_HALF -> Color(0xFFFF5722)
        MatchStatus.PENALTY_SHOOTOUT -> Color(0xFF673AB7)
    }

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(screenWidth(12.0)),
        elevation = CardDefaults.cardElevation(screenHeight(4.0)),
        colors = CardDefaults.cardColors(
            containerColor = surfaceContainerHighLight
        ),
    ) {
        Column(
            modifier = Modifier.padding(screenWidth(16.0))
        ) {
            // Match status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = matchStatusColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(screenWidth(4.0))
                    )
                    .padding(vertical = screenHeight(4.0), horizontal = screenWidth(8.0)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(
                        when (fixtureData.matchStatus) {
                            MatchStatus.PENDING -> R.drawable.clock
                            MatchStatus.COMPLETED -> R.drawable.check_mark
                            MatchStatus.CANCELLED -> R.drawable.close
                            else -> R.drawable.ball
                        }
                    ),
                    contentDescription = fixtureData.matchStatus.name,
                    tint = matchStatusColor,
                    modifier = Modifier.size(screenWidth(16.0))
                )
                Spacer(modifier = Modifier.width(screenWidth(8.0)))
                Text(
                    text = fixtureData.matchStatus.name
                        .lowercase()
                        .replace("_", " ")
                        .replaceFirstChar { it.uppercase() },
                    color = matchStatusColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(screenHeight(12.0)))

            // Teams and score
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Home team
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = fixtureData.homeClub.clubLogo.link,
                        contentDescription = fixtureData.homeClub.name,
                        modifier = Modifier
                            .size(screenWidth(48.0))
                            .clip(CircleShape)
                            .border(screenWidth(2.0), primaryColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(screenHeight(4.0)))
                    Text(
                        text = fixtureData.homeClub.clubAbbreviation ?:
                        fixtureData.homeClub.name.take(3).uppercase(),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(screenHeight(4.0)))
                    Text(
                        text = "HOME",
                        color = primaryColor.copy(alpha = 0.7f),
                        fontSize = screenFontSize(10.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Score or VS
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.8f)
                ) {
                    if (fixtureData.matchStatus == MatchStatus.COMPLETED ||
                        fixtureData.matchStatus == MatchStatus.FIRST_HALF ||
                        fixtureData.matchStatus == MatchStatus.SECOND_HALF ||
                        fixtureData.matchStatus == MatchStatus.EXTRA_TIME_FIRST_HALF ||
                        fixtureData.matchStatus == MatchStatus.EXTRA_TIME_SECOND_HALF ||
                        fixtureData.matchStatus == MatchStatus.PENALTY_SHOOTOUT) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = fixtureData.homeClubScore?.toString() ?: "0",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = " : ",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = fixtureData.awayClubScore?.toString() ?: "0",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Black
                            )
                        }
                    } else {
                        Text(
                            text = "VS",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            color = primaryColor
                        )
                    }

                    Spacer(modifier = Modifier.height(screenHeight(4.0)))

                    // Match time
                    Text(
                        text = formatIsoDateTime(LocalDateTime.parse(fixtureData.matchDateTime)),
                        color = onSurfaceVariantLight,
                        fontSize = screenFontSize(12.0).sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Away team
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = fixtureData.awayClub.clubLogo.link,
                        contentDescription = fixtureData.awayClub.name,
                        modifier = Modifier
                            .size(screenWidth(48.0))
                            .clip(CircleShape)
                            .border(screenWidth(2.0), primaryColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(screenWidth(4.0)))
                    Text(
                        text = fixtureData.awayClub.clubAbbreviation ?:
                        fixtureData.awayClub.name.take(3).uppercase(),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(screenHeight(4.0)))
                    Text(
                        text = "AWAY",
                        color = primaryColor.copy(alpha = 0.7f),
                        fontSize = screenFontSize(10.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Glowing accent at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(2.0))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.3f),
                                primaryColor,
                                primaryColor.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    LigiopenTheme {
        HomeScreen(
            fixtures = fixtures,
            news = news,
            navigateToSingleVideoScreen = {_,_,_ ->},
            navigateToAllVideosScreen = {},
            navigateToPostMatchScreen = { _, _, _ -> },
            navigateToNewsDetailsScreen = {}
        )
    }
}