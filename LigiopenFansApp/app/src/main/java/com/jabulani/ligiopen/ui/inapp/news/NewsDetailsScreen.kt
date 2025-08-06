package com.jabulani.ligiopen.ui.inapp.news

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.data.network.model.news.NewsItemDto
import com.jabulani.ligiopen.data.network.model.news.newsItems
import com.jabulani.ligiopen.data.network.model.news.singleNews
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus
import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth

object NewsDetailsScreenDestination : AppNavigation {
    override val title: String = "News details screen"
    override val route: String = "news-details-screen"
    val newsId: String = "newsId"
    val routeWithNewsId: String = "$route/{$newsId}"
}

@Composable
fun NewsDetailsScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity

    val viewModel: NewsDetailsViewModel = viewModel(factory = AppViewModelFactory.Factory)
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


    BackHandler(onBack = navigateToPreviousScreen)
    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        NewsDetailsScreen(
            title = uiState.news.title,
            subtitle = uiState.news.subTitle,
            coverPhoto = uiState.news.coverPhoto.link,
            newsItems = uiState.news.newsItems,
            loadingStatus = uiState.loadingStatus,

            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}

@Composable
fun NewsDetailsScreen(
    title: String?,
    subtitle: String?,
    coverPhoto: String?,
    newsItems: List<NewsItemDto>,
    loadingStatus: LoadingStatus,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Text(
                text = if(loadingStatus == LoadingStatus.LOADING) "Loading..." else title?.let { "$it - $subtitle" } ?: run { "News" },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
        if(loadingStatus == LoadingStatus.LOADING) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                if (coverPhoto != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(coverPhoto)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.loading_img),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f) // Maintains proper proportions
                            .clip(RoundedCornerShape(screenWidth(x = 12.0))) // Applies rounded corners
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                }

                Text(
                    text = title ?: "",
                    fontSize = screenFontSize(x = 18.0).sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = "Mike Kevin - 2025 April 3rd",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.W400,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                newsItems.forEach {
                    NewsItemCard(newsItem = it)
                }
            }
        }

    }
}

@Composable
fun NewsItemCard(
    newsItem: NewsItemDto,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(screenWidth(x = 16.0))
    ) {
        if(newsItem.title != null) {
            Text(
                text = newsItem.title,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
        }
        if(newsItem.subTitle != null) {
            Text(
                text = newsItem.subTitle,
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 6.0)))
        }
        Text(
            text = newsItem.paragraph,
            fontSize = screenFontSize(x = 14.0).sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Justify
        )
        if (newsItem.file?.link != null) {
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(newsItem.file.link)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.Crop,
                contentDescription = "News Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = screenHeight(x = 250.0))
                    .padding(vertical = screenHeight(x = 8.0))
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsDetailsScreenPreview() {
    LigiopenTheme {
        NewsDetailsScreen(
            title = singleNews.title,
            subtitle = singleNews.subTitle,
            coverPhoto = singleNews.coverPhoto.link,
            newsItems = newsItems,
            loadingStatus = LoadingStatus.INITIAL,
            navigateToPreviousScreen = {}
        )
    }
}
