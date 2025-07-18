package com.admin.ligiopen.ui.screens.match.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.match.location.MatchLocationData
import com.admin.ligiopen.data.network.models.match.location.matchLocations
import com.admin.ligiopen.ui.screens.match.clubs.ClubCard
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun LocationsScreenComposable(
    navigateToLoginScreenWithArgs: (email: String, password: String) -> Unit,
    navigateToLocationAdditionScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: LocationsViewModel = viewModel(factory = AppViewModelFactory.Factory)
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

    if(uiState.loadingStatus == LoadingStatus.FAIL) {
        if(uiState.unaAuthorized) {
            val email = uiState.userAccount.email
            val password = uiState.userAccount.password

            navigateToLoginScreenWithArgs(email, password)
        }
        viewModel.resetStatus()
    }

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        LocationsScreen(
            matchLocations = uiState.matchLocations,
            navigateToLocationAdditionScreen = navigateToLocationAdditionScreen,
            loadingStatus = uiState.loadingStatus
        )
    }
}

@Composable
fun LocationsScreen(
    matchLocations: List<MatchLocationData>,
    navigateToLocationAdditionScreen: () -> Unit,
    loadingStatus: LoadingStatus,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToLocationAdditionScreen) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new location"
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = screenWidth(x = 16.0),
                        vertical = screenHeight(x = 16.0)
                    )
            ) {
                if(loadingStatus == LoadingStatus.LOADING) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn {
                        items(matchLocations) { location ->
                            LocationCard(matchLocation = location)
                            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LocationCard(
    matchLocation: MatchLocationData,
    modifier: Modifier = Modifier
) {
    val photos = matchLocation.photos ?: emptyList()

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(screenWidth(x = 8.0))
    ) {
        Column {
            if (photos.isNotEmpty()) {
                val pagerState = rememberPagerState()
                val coroutineScope = rememberCoroutineScope()

                Box(modifier = Modifier.height(screenHeight(x = 200.0))) {
                    HorizontalPager(
                        count = photos.size,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = photos[page].link,
                            contentDescription = "Image of ${matchLocation.venueName}",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(screenWidth(x = 8.0))),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Dots Indicator
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(photos.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(screenWidth(x = 8.0))
                                    .padding(2.dp)
                                    .background(
                                        if (pagerState.currentPage == index) Color.White else Color.Gray,
                                        shape = RoundedCornerShape(50)
                                    )
                            )
                        }
                    }
                }

//                // Auto-slide effect (optional)
//                LaunchedEffect(pagerState.currentPage) {
//                    coroutineScope.launch {
//                        pagerState.animateScrollToPage((pagerState.currentPage + 1) % photos.size)
//                    }
//                }
            } else {
                // No Photos Case
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight(x = 200.0))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Photos",
                        fontSize = screenFontSize(x = 16.0).sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray
                    )
                }
            }
            
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = screenWidth(x = 16.0),
                        vertical = screenHeight(x = 8.0)
                    )
            ) {
                Text(
                    text = matchLocation.venueName,
                    modifier = Modifier.padding(screenWidth(x = 8.0)),
                    fontSize = screenFontSize(x = 18.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${matchLocation.town}, ${matchLocation.county}, ${matchLocation.country}",
                    modifier = Modifier.padding(horizontal = screenWidth(x = 4.0), vertical = screenHeight(
                        x = 4.0
                    )),
                    fontSize = screenFontSize(x = 14.0).sp
                )  
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationsScreenPreview() {
    LigiopenadminTheme {
        LocationsScreen(
            matchLocations = matchLocations,
            loadingStatus = LoadingStatus.INITIAL,
            navigateToLocationAdditionScreen = {}
        )
    }
}