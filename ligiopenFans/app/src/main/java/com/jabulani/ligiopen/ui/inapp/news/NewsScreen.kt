package com.jabulani.ligiopen.ui.inapp.news

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.data.network.model.news.NewsDto
import com.jabulani.ligiopen.data.network.model.news.news
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus
import com.jabulani.ligiopen.ui.inapp.home.HomeScreenTab
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.ui.theme.onPrimaryLight
import com.jabulani.ligiopen.ui.theme.onSurfaceLight
import com.jabulani.ligiopen.ui.theme.onSurfaceVariantLight
import com.jabulani.ligiopen.ui.theme.onTertiaryLight
import com.jabulani.ligiopen.ui.theme.primaryLight
import com.jabulani.ligiopen.ui.theme.surfaceContainerHighLight
import com.jabulani.ligiopen.ui.theme.tertiaryLight
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsScreenComposable(
    addTopPadding: Boolean = true,
    clubId: Int? = null,
    navigateToNewsDetailsScreen: (newsId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: NewsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when(lifecycleState) {
            Lifecycle.State.DESTROYED -> {
                Log.d("lifecycle", "Destroyed: CLUB ID: $clubId")
            }
            Lifecycle.State.INITIALIZED -> {
                Log.d("lifecycle", "Initialized: CLUB ID: $clubId")
                viewModel.setClubId(clubId = clubId)
            }
            Lifecycle.State.CREATED -> {
                Log.d("lifecycle", "Created: CLUB ID: $clubId")

            }
            Lifecycle.State.STARTED -> {
                Log.d("lifecycle", "Started: CLUB ID: $clubId")

            }
            Lifecycle.State.RESUMED -> {
                Log.d("lifecycle", "Resumed: CLUB ID: $clubId")
                viewModel.setClubId(clubId = clubId)
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        NewsScreen(
            news = uiState.news,
            addTopPadding = addTopPadding,
            loadingStatus = uiState.loadingStatus,
            navigateToNewsDetailsScreen = navigateToNewsDetailsScreen,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsScreen(
    news: List<NewsDto>,
    addTopPadding: Boolean,
    loadingStatus: LoadingStatus,
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
        if (addTopPadding) {
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
                    text = HomeScreenTab.NEWS.name
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

                // Refresh button
                IconButton(
                    onClick = { /* Handle refresh */ },
                    modifier = Modifier
                        .size(screenWidth(40.0))
                        .background(accentColor, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.refresh),
                        contentDescription = "Refresh",
                        tint = onTertiaryLight
                    )
                }
            }
        }

        // Main content area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidth(8.0))
        ) {
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(screenWidth(64.0)),
                            strokeWidth = screenWidth(4.0),
                            color = accentColor
                        )
                    }
                }
                else -> {
                    if (news.isEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.newspaper2),
                                    contentDescription = "No news",
                                    tint = primaryColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(screenWidth(64.0))
                                )
                                Spacer(modifier = Modifier.height(screenHeight(16.0)))
                                Text(
                                    text = "NO NEWS AVAILABLE",
                                    color = primaryColor.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(screenHeight(8.0)))
                                Text(
                                    text = "Check back later for updates",
                                    color = onSurfaceVariantLight
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(screenWidth(16.0)),
                            contentPadding = PaddingValues(vertical = screenHeight(8.0))
                        ) {
                            // Featured news item (first item)
                            item {
                                NewsTile(
                                    news = news[0],
                                    isFeatured = true,
                                    onClick = { navigateToNewsDetailsScreen(news[0].id.toString()) },
                                    modifier = Modifier.padding(horizontal = screenWidth(4.0))
                                )
                            }

                            // Regular news items
                            items(news.drop(1)) { newsItem ->
                                NewsTile(
                                    news = newsItem,
                                    isFeatured = false,
                                    onClick = { navigateToNewsDetailsScreen(newsItem.id.toString()) },
                                    modifier = Modifier.padding(horizontal = screenWidth(4.0))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsTile(
    news: NewsDto,
    isFeatured: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = primaryLight
    val surfaceColor = surfaceContainerHighLight
    val onSurfaceColor = onSurfaceLight
    val accentColor = tertiaryLight
//    val dateTime = remember(news.createdAt) {
//        try {
//            LocalDateTime.parse(news.createdAt)
//        } catch (e: Exception) {
//            LocalDateTime.now()
//        }
//    }

    val dateTime = LocalDateTime.now()

    val formattedDate = remember(dateTime) {
        DateTimeFormatter
            .ofPattern("MMM dd, yyyy • hh:mm a")
            .format(dateTime)
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .shadow(screenWidth(4.0), shape = RoundedCornerShape(screenWidth(12.0))),
        shape = RoundedCornerShape(screenWidth(12.0)),
        elevation = CardDefaults.cardElevation(screenHeight(4.0)),
        colors = CardDefaults.cardColors(
            containerColor = surfaceContainerHighLight
        ),
    ) {
        Column {
            // News image with category tag
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isFeatured) screenHeight(240.0) else screenHeight(160.0))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(news.coverPhoto.link)
                        .crossfade(true)
                        .build(),
                    contentDescription = news.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Category tag
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(screenWidth(12.0))
                        .background(
                            color = primaryColor.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(screenWidth(4.0))
                        )
                        .padding(horizontal = screenWidth(8.0), vertical = screenHeight(4.0))
                ) {
//                    Text(
//                        text = news.category?.uppercase() ?: "NEWS",
//                        color = onPrimaryLight,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold
//                    )
                    Text(
                        text = "NEWS",
                        color = onPrimaryLight,
                        fontSize = screenFontSize(12.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Gradient overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 0.7f
                            )
                        )
                )
            }

            // News content
            Column(
                modifier = Modifier.padding(screenWidth(16.0))
            ) {
                Text(
                    text = news.title,
                    style = if (isFeatured) MaterialTheme.typography.labelMedium
                    else MaterialTheme.typography.labelSmall,
                    color = onSurfaceColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = screenHeight(8.0))
                )

                Text(
                    text = news.subTitle ?: "",
                    color = onSurfaceVariantLight,
                    maxLines = if (isFeatured) 3 else 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = screenHeight(8.0))
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.clock),
                        contentDescription = "Time",
                        tint = onSurfaceVariantLight,
                        modifier = Modifier.size(screenWidth(16.0))
                    )
                    Spacer(modifier = Modifier.width(screenWidth(8.0)))
                    Text(
                        text = formattedDate,
                        color = onSurfaceVariantLight,
                        fontSize = screenFontSize(12.0).sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Read more button
                    TextButton(
                        onClick = onClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = primaryColor
                        )
                    ) {
                        Text(
                            "READ MORE",
                            fontSize = screenFontSize(14.0).sp,
                        )
                        Spacer(modifier = Modifier.width(screenWidth(4.0)))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Read more",
                            modifier = Modifier.size(screenWidth(16.0))
                        )
                    }
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
fun NewsScreenPreview() {
    LigiopenTheme {
        NewsScreen(
            news = news,
            addTopPadding = false,
            loadingStatus = LoadingStatus.INITIAL,
            navigateToNewsDetailsScreen = {},
        )
    }
}