package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.lineup.PlayerInLineup
import com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.lineup.PlayersLineupScreenComposable
import com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.stats.MatchStatisticsScreenComposable
import com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.summary.MatchSummaryComposable
import com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.timeline.MatchTimelineScreenComposable
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.club.club
import com.jabulani.ligiopen.data.network.model.location.MatchLocationData
import com.jabulani.ligiopen.data.network.model.location.matchLocation
import com.jabulani.ligiopen.data.network.model.match.commentary.MatchCommentaryData
import com.jabulani.ligiopen.data.network.model.match.commentary.matchCommentaries
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.data.network.model.match.fixture.fixture
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus
import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenWidth

object HighlightsScreenDestination: AppNavigation {
    override val route: String = "highlights-screen"
    override val title: String = "Highlights screen"
    val postMatchId: String = "postMatchId"
    val fixtureId: String = "fixtureId"
    val locationId: String = "locationId"
    val routeWithPostMatchIdAndFixtureIdAndLocationId = "$route/{$postMatchId}/{$fixtureId}/{$locationId}"
}

@Composable
fun HighlightsScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: HighlightsScreenViewModel = viewModel(factory = AppViewModelFactory.Factory)
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


    val tabs = listOf(
        HighlightsScreenTabItem(
            name = "Summary",
            tab = HighlightsScreenTabs.SUMMARY,
            icon = R.drawable.full_time2
        ),
        HighlightsScreenTabItem(
            name = "Timeline",
            tab = HighlightsScreenTabs.TIMELINE,
            icon = R.drawable.timeline
        ),
        HighlightsScreenTabItem(
            name = "Lineups",
            tab = HighlightsScreenTabs.LINEUPS,
            icon = R.drawable.lineup_2
        ),
//        HighlightsScreenTabItem(
//            name = "Stats",
//            tab = HighlightsScreenTabs.STATS,
//            icon = R.drawable.stats
//        ),
//        HighlightsScreenTabItem(
//            name = "Edit",
//            tab = HighlightsScreenTabs.EDIT,
//            icon = R.drawable.edit
//        ),
    )
    var currentTab by rememberSaveable {
        mutableStateOf(HighlightsScreenTabs.SUMMARY)
    }
    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        HighlightsScreen(
            commentaries = uiState.commentaries,
            matchFixtureData = uiState.matchFixtureData,
            matchLocation = uiState.matchLocationData,
            fixtureId = uiState.fixtureId,
            homeClubScore = uiState.homeClubScore,
            awayClubScore = uiState.awayClubScore,
            awayClub = uiState.matchFixtureData.awayClub,
            homeClub = uiState.matchFixtureData.homeClub,
            tabs = tabs,
            playersInLineup = uiState.playersInLineup,
            currentTab = currentTab,
            onChangeTab = {
                currentTab = it
            },
            loadingStatus = uiState.loadingStatus,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }

}

@Composable
fun HighlightsScreen(
    commentaries: List<MatchCommentaryData>,
    matchFixtureData: FixtureData,
    matchLocation: MatchLocationData,
    homeClubScore: Int,
    awayClubScore: Int,
    awayClub: ClubDetails,
    homeClub: ClubDetails,
    fixtureId: String?,
    playersInLineup: List<PlayerInLineup>,
    tabs: List<HighlightsScreenTabItem>,
    currentTab: HighlightsScreenTabs,
    onChangeTab: (tab: HighlightsScreenTabs) -> Unit,
    loadingStatus: LoadingStatus,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = currentTab.name,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = matchFixtureData.homeClub.clubLogo.link,
                    contentDescription = matchFixtureData.homeClub.name,
                    modifier = Modifier
                        .size(screenWidth(x = 24.0))
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = homeClubScore.toString(),
                    fontSize = screenFontSize(x = 16.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = ":",
                    fontSize = screenFontSize(x = 16.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = awayClubScore.toString(),
                    fontSize = screenFontSize(x = 16.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                AsyncImage(
                    model = matchFixtureData.awayClub.clubLogo.link,
                    contentDescription = matchFixtureData.awayClub.name,
                    modifier = Modifier
                        .size(screenWidth(x = 24.0))
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
        when(currentTab) {
            HighlightsScreenTabs.SUMMARY -> {
                MatchSummaryComposable(
                    matchStatus = matchFixtureData.matchStatus,
                    matchFixtureData = matchFixtureData,
                    commentaries = commentaries,
                    matchLocation = matchLocation,
                    homeClub = homeClub,
                    awayClub = awayClub,
                    homeClubScore = homeClubScore,
                    awayClubScore = awayClubScore,
                    loadingStatus = loadingStatus,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            HighlightsScreenTabs.TIMELINE -> {
                MatchTimelineScreenComposable(
                    commentaries = commentaries,
                    matchStatus = matchFixtureData.matchStatus,
                    fixtureId = fixtureId,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            HighlightsScreenTabs.LINEUPS -> {
                PlayersLineupScreenComposable(
                    matchStatus = matchFixtureData.matchStatus,
                    playersInLineup = playersInLineup,
                    matchFixtureData = matchFixtureData,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            HighlightsScreenTabs.STATS -> {
                MatchStatisticsScreenComposable(
                    matchStatus = matchFixtureData.matchStatus,
                    matchFixtureData = matchFixtureData,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            HighlightsScreenTabs.EDIT -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Text(text = "Edit")
                }
            }
        }
        HighlightScreenBottomBar(
            tabs = tabs,
            currentTab = currentTab,
            onChangeTab = onChangeTab
        )
    }
}

@Composable
fun HighlightScreenBottomBar(
    tabs: List<HighlightsScreenTabItem>,
    currentTab: HighlightsScreenTabs,
    onChangeTab: (tab: HighlightsScreenTabs) -> Unit,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HighlightsScreenPreview() {
    val tabs = listOf(
        HighlightsScreenTabItem(
            name = "Summary",
            tab = HighlightsScreenTabs.SUMMARY,
            icon = R.drawable.ball_summary
        ),
        HighlightsScreenTabItem(
            name = "Timeline",
            tab = HighlightsScreenTabs.TIMELINE,
            icon = R.drawable.timeline
        ),
        HighlightsScreenTabItem(
            name = "Lineups",
            tab = HighlightsScreenTabs.LINEUPS,
            icon = R.drawable.lineup
        ),
        HighlightsScreenTabItem(
            name = "Stats",
            tab = HighlightsScreenTabs.STATS,
            icon = R.drawable.stats
        ),
    )
    var currentTab by rememberSaveable {
        mutableStateOf(HighlightsScreenTabs.SUMMARY)
    }
    LigiopenTheme {
        HighlightsScreen(
            commentaries = matchCommentaries,
            awayClub = club,
            homeClub = club,
            matchFixtureData = fixture,
            homeClubScore = 0,
            awayClubScore = 0,
            matchLocation = matchLocation,
            fixtureId = null,
            tabs = tabs,
            playersInLineup = emptyList(),
            currentTab = currentTab,
            onChangeTab = {
                currentTab = it
            },
            loadingStatus = LoadingStatus.INITIAL,
            navigateToPreviousScreen = {}
        )
    }
}