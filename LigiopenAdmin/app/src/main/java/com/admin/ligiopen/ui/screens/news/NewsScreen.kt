package com.admin.ligiopen.ui.screens.news

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.news.NewsDto
import com.admin.ligiopen.data.network.models.news.news
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsScreenComposable(
    addTopPadding: Boolean = true,
    navigateToNewsDetailsScreen: (newsId: String) -> Unit,
    navigateToNewsAdditionScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: NewsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when(lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                viewModel.getInitialData()
            }
        }
    }

    NewsScreen(
        news = uiState.news,
        addTopPadding = addTopPadding,
        loadingStatus = uiState.loadingStatus,
        navigateToNewsDetailsScreen = navigateToNewsDetailsScreen,
        navigateToNewsAdditionScreen = navigateToNewsAdditionScreen,
        changeTab = viewModel::changeTab,
        modifier = modifier
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    news: List<NewsDto>,
    addTopPadding: Boolean,
    loadingStatus: LoadingStatus,
    navigateToNewsDetailsScreen: (newsId: String) -> Unit,
    navigateToNewsAdditionScreen: () -> Unit,
    changeTab: (NewsStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Approved", "Pending", "Rejected")
    val newsStatuses = listOf(NewsStatus.APPROVED, NewsStatus.PENDING, NewsStatus.REJECTED)

    LaunchedEffect(selectedTabIndex) {
        changeTab(newsStatuses[selectedTabIndex])
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToNewsAdditionScreen) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add news"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Status tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = screenFontSize(14.0).sp
                            )
                        }
                    )
                }
            }

            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(screenWidth(16.0))
            ) {
                when {
                    loadingStatus == LoadingStatus.LOADING -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    news.isEmpty() -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "No ${tabTitles[selectedTabIndex].lowercase()} news",
                                fontSize = screenFontSize(16.0).sp
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(screenHeight(16.0))
                        ) {
                            // Featured news item (first item)
                            item {
                                NewsFeaturedCard(
                                    news = news.first(),
                                    onClick = { navigateToNewsDetailsScreen(news.first().id.toString()) }
                                )
                            }

                            // Regular news items
                            items(news.drop(1)) { item ->
                                NewsCard(
                                    news = item,
                                    onClick = { navigateToNewsDetailsScreen(item.id.toString()) }
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
fun NewsFeaturedCard(
    news: NewsDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(screenWidth(12.0))
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(news.coverPhoto.link)
                    .crossfade(true)
                    .build(),
                contentDescription = "News image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(200.0))
            )

            Column(
                modifier = Modifier.padding(screenWidth(16.0))
            ) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(screenHeight(8.0)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(screenWidth(8.0)))
                            .background(
                                when (NewsStatus.valueOf(news.newsStatus)) {
                                    NewsStatus.APPROVED -> MaterialTheme.colorScheme.tertiaryContainer
                                    NewsStatus.PENDING -> MaterialTheme.colorScheme.secondaryContainer
                                    NewsStatus.REJECTED -> MaterialTheme.colorScheme.errorContainer
                                }
                            )
                            .padding(horizontal = screenWidth(8.0), vertical = screenHeight(4.0))
                    ) {
                        Text(
                            text = news.newsStatus.lowercase().replaceFirstChar { it.uppercase() },
                            color = when (NewsStatus.valueOf(news.newsStatus)) {
                                NewsStatus.APPROVED -> MaterialTheme.colorScheme.onTertiaryContainer
                                NewsStatus.PENDING -> MaterialTheme.colorScheme.onSecondaryContainer
                                NewsStatus.REJECTED -> MaterialTheme.colorScheme.onErrorContainer
                            },
                            fontSize = screenFontSize(12.0).sp
                        )
                    }

                    Spacer(modifier = Modifier.width(screenWidth(8.0)))

                    Text(
                        text = formatDate(news.publishedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsCard(
    news: NewsDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(screenWidth(12.0))
    ) {
        Row(
            modifier = Modifier.padding(screenWidth(12.0))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(news.coverPhoto.link)
                    .crossfade(true)
                    .build(),
                contentDescription = "News image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(screenWidth(80.0))
                    .clip(RoundedCornerShape(screenWidth(8.0)))
            )

            Column(
                modifier = Modifier
                    .padding(start = screenWidth(12.0))
                    .weight(1f)
            ) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(screenHeight(8.0)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(screenWidth(8.0)))
                            .background(
                                when (NewsStatus.valueOf(news.newsStatus)) {
                                    NewsStatus.APPROVED -> MaterialTheme.colorScheme.tertiaryContainer
                                    NewsStatus.PENDING -> MaterialTheme.colorScheme.secondaryContainer
                                    NewsStatus.REJECTED -> MaterialTheme.colorScheme.errorContainer
                                }
                            )
                            .padding(horizontal = screenWidth(8.0), vertical = screenHeight(4.0))
                    ) {
                        Text(
                            text = news.newsStatus.lowercase().replaceFirstChar { it.uppercase() },
                            color = when (NewsStatus.valueOf(news.newsStatus)) {
                                NewsStatus.APPROVED -> MaterialTheme.colorScheme.onTertiaryContainer
                                NewsStatus.PENDING -> MaterialTheme.colorScheme.onSecondaryContainer
                                NewsStatus.REJECTED -> MaterialTheme.colorScheme.onErrorContainer
                            },
                            fontSize = screenFontSize(12.0).sp
                        )
                    }

                    Spacer(modifier = Modifier.width(screenWidth(8.0)))

                    Text(
                        text = formatDate(news.publishedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "View details",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateString: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateString, formatter)
        val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        dateTime.format(outputFormatter)
    } catch (e: Exception) {
        dateString
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsScreenPreview() {
    LigiopenadminTheme {
        NewsScreen(
            news = news,
            addTopPadding = true,
            loadingStatus = LoadingStatus.INITIAL,
            navigateToNewsDetailsScreen = {},
            navigateToNewsAdditionScreen = {},
            changeTab = {}
        )
    }
}