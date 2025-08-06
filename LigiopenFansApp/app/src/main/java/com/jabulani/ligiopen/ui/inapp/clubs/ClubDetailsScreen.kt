package com.jabulani.ligiopen.ui.inapp.clubs

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.club.club
import com.jabulani.ligiopen.data.network.model.news.NewsDto
import com.jabulani.ligiopen.data.network.model.news.news
import com.jabulani.ligiopen.data.network.model.player.PlayerDetails
import com.jabulani.ligiopen.data.network.model.player.PlayerPosition
import com.jabulani.ligiopen.ui.inapp.fixtures.FixturesScreenComposable
import com.jabulani.ligiopen.ui.inapp.news.NewsScreenComposable
import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.ui.theme.backgroundLight
import com.jabulani.ligiopen.ui.theme.onSurfaceLight
import com.jabulani.ligiopen.ui.theme.primaryLight
import com.jabulani.ligiopen.ui.theme.surfaceLight
import com.jabulani.ligiopen.ui.theme.surfaceVariantLight
import com.jabulani.ligiopen.ui.theme.tertiaryLight
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth

object ClubDetailsScreenDestination : AppNavigation {
    override val title: String = "Club details screen"
    override val route: String = "club-details-screen"
    val clubId: String = "clubId"
    val routeWithArgs: String = "$route/{$clubId}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClubDetailsScreenComposable(
    navigateToFixtureDetailsScreen: () -> Unit,
    navigateToNewsDetailsScreen: (newsId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    navigateToPlayerDetailsScreen: (playerId: String) -> Unit,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    navigateToLoginScreenWithArgs: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity
    BackHandler(onBack = navigateToPreviousScreen)

    val viewModel: ClubDetailsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val tabs = listOf(
        ClubScreenTabItem(
            name = "Overview",
            icon = R.drawable.info,
            tab = ClubScreenTab.INFO
        ),
        ClubScreenTabItem(
            name = "News",
            icon = R.drawable.newspaper,
            tab = ClubScreenTab.NEWS
        ),
        ClubScreenTabItem(
            name = "Matches",
            icon = R.drawable.score2,
            tab = ClubScreenTab.MATCHES
        ),
//        ClubScreenTabItem(
//            name = "Shop",
//            icon = R.drawable.shop,
//            tab = ClubScreenTab.SHOP
//        ),
    )

    var selectedTab by rememberSaveable {
        mutableStateOf(ClubScreenTab.INFO)
    }

    BackHandler(onBack = {
        if(selectedTab != ClubScreenTab.INFO) {
            selectedTab = ClubScreenTab.INFO
        } else {
            navigateToPreviousScreen()
        }
    })

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        ClubDetailsScreen(
            clubDetails = uiState.clubDetails,
            clubNews = uiState.clubNews,
            tabs = tabs,
            onChangeTab = {
                selectedTab = it
            },
            selectedTab = selectedTab,
            navigateToNewsDetailsScreen = navigateToNewsDetailsScreen,
            navigateToPreviousScreen = navigateToPreviousScreen,
            navigateToFixtureDetailsScreen = navigateToFixtureDetailsScreen,
            navigateToPlayerDetailsScreen = navigateToPlayerDetailsScreen,
            navigateToPostMatchScreen = navigateToPostMatchScreen,
            navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClubDetailsScreen(
    clubDetails: ClubDetails,
    clubNews: List<NewsDto>,
    tabs: List<ClubScreenTabItem>,
    onChangeTab: (tab: ClubScreenTab) -> Unit,
    selectedTab: ClubScreenTab,
    navigateToFixtureDetailsScreen: () -> Unit,
    navigateToNewsDetailsScreen: (newsId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    navigateToPlayerDetailsScreen: (playerId: String) -> Unit,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    navigateToLoginScreenWithArgs: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier
) {

    Log.d("clubDetailsScreen", "Club ID: ${clubDetails.clubId}")
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(
//                vertical = screenHeight(x = 16.0),
//                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenWidth(x = 8.0))

        ) {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ligiopen_icon),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(screenWidth(x = 3.0)))
            Text(
                text = selectedTab.name,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(clubDetails.clubLogo.link)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.Crop,
                contentDescription = "Club logo",
                modifier = Modifier
                    .size(screenWidth(x = 24.0))
                    .clip(CircleShape)

            )
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Text(
                text = clubDetails.name.takeIf { it.length < 15 } ?: (clubDetails.name.take(15) + "..."),
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
        when(selectedTab) {
            ClubScreenTab.INFO -> {
                ClubOverviewScreen(
                    clubDetails = clubDetails,
                    navigateToPlayerDetailsScreen = navigateToPlayerDetailsScreen,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            ClubScreenTab.NEWS -> {
                NewsScreenComposable(
                    navigateToNewsDetailsScreen = navigateToNewsDetailsScreen,
                    clubId = clubDetails.clubId,
                    addTopPadding = false,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            ClubScreenTab.MATCHES -> {
                FixturesScreenComposable(
                    showTopBanner = false,
                    navigateToPostMatchScreen = navigateToPostMatchScreen,
                    navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
                    clubId = clubDetails.clubId,
                    singleClubMode = true,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            ClubScreenTab.SHOP -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Text(text = "${clubDetails.name} shop")
                }
            }
        }
        ClubDetailsScreenBottomBar(
            tabs = tabs,
            currentTab = selectedTab,
            onChangeTab = onChangeTab
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClubOverviewScreen(
    clubDetails: ClubDetails,
    navigateToPlayerDetailsScreen: (playerId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val primaryColor = primaryLight
    val accentColor = tertiaryLight
    val backgroundColor = backgroundLight
    val surfaceColor = surfaceLight
    val onSurfaceColor = onSurfaceLight
    val surfaceVariantColor = surfaceVariantLight

    // Group players by position with null checks
    val groupedPlayers = clubDetails.players.groupBy { it.playerPosition }
    val goalkeepers = groupedPlayers[PlayerPosition.GOALKEEPER] ?: emptyList()
    val defenders = groupedPlayers[PlayerPosition.DEFENDER] ?: emptyList()
    val midfielders = groupedPlayers[PlayerPosition.MIDFIELDER] ?: emptyList()
    val forwards = groupedPlayers[PlayerPosition.FORWARD] ?: emptyList()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Club Header Section
        item {
            Box(
                modifier =
                    Modifier
                        .height(screenHeight(280.0))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(primaryColor, primaryColor.copy(alpha = 0.7f))
                            )
                        )
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(clubDetails.clubMainPhoto?.link)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Club main photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            translationY = -scrollState.value * 0.5f
                            alpha = 0.9f
                        }

                )
                Column(
                    modifier = Modifier
                        .padding(screenWidth(16.0))
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                ) {
                    Text(
                        color = Color.White,
                        text = if(clubDetails.name.isNotEmpty()) "${clubDetails.name} (${clubDetails.clubAbbreviation})" else "",
                        style = TextStyle(
                            fontSize = screenFontSize(22.0).sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = screenFontSize(1.0).sp,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(1f, 1f),
                                blurRadius = 4f
                            )
                        ),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(screenHeight(8.0)))
                    Text(
                        color = Color.White,
                        text = clubDetails.description,
                        style = TextStyle(
                            fontSize = screenFontSize(16.0).sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = screenFontSize(1.0).sp,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(1f, 1f),
                                blurRadius = 4f
                            )
                        ),
                    )
                }
            }
        }

        // Players Section
        stickyHeader {
            Text(
                text = "Players",
                fontSize = screenFontSize(18.0).sp,
                color = primaryColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = screenWidth(16.0))
            )
            Spacer(modifier = Modifier.height(screenHeight(8.0)))
        }

        // Goalkeepers Section
        if (goalkeepers.isNotEmpty()) {
            item {
                SectionHeader(
                    text = "Goalkeepers",
                )
            }
            item {
                PlayersHorizontalGrid(
                    players = goalkeepers,
                    navigateToPlayerDetailsScreen = navigateToPlayerDetailsScreen
                )
            }
        }

        // Defenders Section
        if (defenders.isNotEmpty()) {
            item {
                SectionHeader(text = "Defenders")
            }
            item {
                PlayersHorizontalGrid(
                    players = defenders,
                    navigateToPlayerDetailsScreen = navigateToPlayerDetailsScreen
                )
            }
        }

        // Midfielders Section
        if (midfielders.isNotEmpty()) {
            item {
                SectionHeader(text = "Midfielders")
            }
            item {
                PlayersHorizontalGrid(
                    players = midfielders,
                    navigateToPlayerDetailsScreen = navigateToPlayerDetailsScreen
                )
            }
        }

        // Forwards Section
        if (forwards.isNotEmpty()) {
            item {
                SectionHeader(text = "Forwards")
            }
            item {
                PlayersHorizontalGrid(
                    players = forwards,
                    navigateToPlayerDetailsScreen = navigateToPlayerDetailsScreen
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        color = tertiaryLight,
        fontWeight = FontWeight.Bold,
        fontSize = screenFontSize(16.0).sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidth(16.0), vertical = screenHeight(8.0))
    )
}

@Composable
private fun PlayersHorizontalGrid(
    players: List<PlayerDetails>,
    navigateToPlayerDetailsScreen: (playerId: String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = screenWidth(16.0)),
        horizontalArrangement = Arrangement.spacedBy(screenWidth(12.0))
    ) {
        items(players) { player ->
            PlayerCard(
                player = player,
                onClick = { navigateToPlayerDetailsScreen(player.playerId.toString()) }
            )
        }
    }
}

@Composable
private fun PlayerCard(
    player: PlayerDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(screenWidth(120.0))
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = screenHeight(4.0))
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(player.mainPhoto?.link)
                    .crossfade(true)
                    .build(),
                contentDescription = "Player photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            Column(modifier = Modifier.padding(screenWidth(8.0))) {
                Text(
                    text = player.username,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "#${player.number}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}






@Composable
fun PlayerCell(
    playerDetails: PlayerDetails,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(
        model = playerDetails.mainPhoto?.link ?: R.drawable.no_photo
        , // Fallback to local drawable if URL is null
        placeholder = painterResource(id = R.drawable.loading),
        error = painterResource(id = R.drawable.broken_image)
    )

    Card(modifier = modifier) {
        Column(
            modifier = Modifier
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(playerDetails.mainPhoto?.link)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.Crop,
                contentDescription = "Player photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // Force square aspect ratio
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Column(
                modifier = Modifier
                    .padding(screenWidth(x = 8.0))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onBackground,
                        text = "Name: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Text(
                        color = MaterialTheme.colorScheme.onBackground,
                        text = playerDetails.username,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onBackground,
                        text = "Age: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Text(
                        color = MaterialTheme.colorScheme.onBackground,
                        text = "${playerDetails.age} years",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = playerDetails.playerPosition.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun ClubDetailsScreenBottomBar(
    tabs: List<ClubScreenTabItem>,
    currentTab: ClubScreenTab,
    onChangeTab: (tab: ClubScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar {
        for(tab in tabs) {
            NavigationBarItem(
                label = {
                    Text(
                        text = tab.name,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                },
                selected = currentTab == tab.tab,
                onClick = {
                    onChangeTab(tab.tab)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = tab.icon),
                        contentDescription = tab.name,
                        modifier = Modifier
                            .size(screenWidth(x = 20.0))
                    )
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubDetailsScreenPreview() {

    val tabs = listOf(
        ClubScreenTabItem(
            name = "Overview",
            icon = R.drawable.info,
            tab = ClubScreenTab.INFO
        ),
        ClubScreenTabItem(
            name = "News",
            icon = R.drawable.news,
            tab = ClubScreenTab.NEWS
        ),
        ClubScreenTabItem(
            name = "Fixtures",
            icon = R.drawable.standings,
            tab = ClubScreenTab.MATCHES
        ),
        ClubScreenTabItem(
            name = "Scores",
            icon = R.drawable.matches,
            tab = ClubScreenTab.MATCHES
        ),
    )

    var selectedTab by rememberSaveable {
        mutableStateOf(ClubScreenTab.INFO)
    }

    LigiopenTheme {
        ClubDetailsScreen(
            clubDetails = club,
            clubNews = news,
            tabs = tabs,
            onChangeTab = {
                selectedTab = it
            },
            selectedTab = selectedTab,
            navigateToFixtureDetailsScreen = { /*TODO*/ },
            navigateToNewsDetailsScreen = { /*TODO*/ },
            navigateToPreviousScreen = { /*TODO*/ },
            navigateToPlayerDetailsScreen = {},
            navigateToPostMatchScreen = { _ , _, _, ->},
            navigateToLoginScreenWithArgs = { _ , _, ->}
        )
    }
}