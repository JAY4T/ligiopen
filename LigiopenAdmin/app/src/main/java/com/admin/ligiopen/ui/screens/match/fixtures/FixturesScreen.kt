package com.admin.ligiopen.ui.screens.match.fixtures

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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.R
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.match.fixture.FixtureData
import com.admin.ligiopen.data.network.models.match.fixture.MatchStatus
import com.admin.ligiopen.data.network.models.match.fixture.fixtures
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth

@Composable
fun FixturesScreenComposable(
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    navigateToLoginScreenWithArgs: (email: String, password: String) -> Unit,
    navigateToFixtureCreationScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: FixturesViewModel = viewModel(factory = AppViewModelFactory.Factory)
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
        if(uiState.unauthorized) {
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
        FixturesScreen(
            fixtures = uiState.fixtures,
            loadingStatus = uiState.loadingStatus,
            navigateToPostMatchScreen = navigateToPostMatchScreen,
            navigateToFixtureCreationScreen = navigateToFixtureCreationScreen
        )
    }
}

@Composable
fun FixturesScreen(
    fixtures: List<FixtureData>,
    loadingStatus: LoadingStatus,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    navigateToFixtureCreationScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToFixtureCreationScreen
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create fixture"
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = screenHeight(x = 16.0),
                        horizontal = screenWidth(x = 16.0)
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
                        items(fixtures) { fixture ->
                            FixtureCard(
                                fixtureData = fixture,
                                navigateToPostMatchScreen = navigateToPostMatchScreen
                            )
                            Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun FixtureCard(
    fixtureData: FixtureData,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier
            .clickable {
                navigateToPostMatchScreen(fixtureData.postMatchAnalysisId.toString(), fixtureData.matchFixtureId.toString(), fixtureData.matchLocationId.toString())
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row {
                    AsyncImage(
                        model = fixtureData.homeClub.clubLogo.link,
                        contentDescription = fixtureData.homeClub.name,
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .clip(RoundedCornerShape(screenWidth(x = 8.0))),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Column {
                        Text(
                            text = fixtureData.homeClub.name,
                            fontSize = screenFontSize(x = 16.0).sp
                        )
                        Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                        Text(
                            text = "HOME",
                            fontSize = screenFontSize(x = 10.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "VS",
                    fontSize = screenFontSize(x = 16.0).sp,
                    fontWeight = FontWeight.Bold
                )
                if(fixtureData.matchStatus != MatchStatus.CANCELLED && fixtureData.matchStatus != MatchStatus.PENDING) {
                    Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fixtureData.homeClubScore!!.toString(),
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = ":",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = fixtureData.awayClubScore!!.toString(),
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = fixtureData.awayClub.name,
                            fontSize = screenFontSize(x = 16.0).sp
                        )
                        Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                        Text(
                            text = "AWAY",
                            fontSize = screenFontSize(x = 10.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = fixtureData.awayClub.clubLogo.link,
                        contentDescription = fixtureData.awayClub.name,
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .clip(RoundedCornerShape(screenWidth(x = 8.0))),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenWidth(x = 4.0)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val matchStatusColor = when (fixtureData.matchStatus) {
                MatchStatus.PENDING -> Color(0xFF9E9E9E) // Gray - Neutral for pending matches
                MatchStatus.COMPLETED -> Color(0xFF4CAF50) // Green - Indicates match completion
                MatchStatus.CANCELLED -> Color(0xFFF44336) // Red - Signifies cancellation
                MatchStatus.FIRST_HALF -> Color(0xFFFFC107) // Amber - Active but early stage
                MatchStatus.HALF_TIME -> Color(0xFFFF9800) // Orange - Signals break time
                MatchStatus.SECOND_HALF -> Color(0xFFFFC107) // Amber - Ongoing second half
                MatchStatus.EXTRA_TIME_FIRST_HALF -> Color(0xFFFF5722) // Deep Orange - More intensity
                MatchStatus.EXTRA_TIME_HALF_TIME -> Color(0xFFFF9800) // Orange - Half-time in extra time
                MatchStatus.EXTRA_TIME_SECOND_HALF -> Color(0xFFFF5722) // Deep Orange - More intensity
                MatchStatus.PENALTY_SHOOTOUT -> Color(0xFF673AB7) // Purple - Indicates a high-stakes moment
            }


            val matchStatusIcon = when (fixtureData.matchStatus) {
                MatchStatus.PENDING -> R.drawable.clock // Clock icon
                MatchStatus.COMPLETED -> R.drawable.check_mark // Checkmark icon
                MatchStatus.CANCELLED -> R.drawable.close // Cross icon
                MatchStatus.FIRST_HALF -> R.drawable.ball
                MatchStatus.HALF_TIME -> R.drawable.half_time
                MatchStatus.SECOND_HALF -> R.drawable.ball
                MatchStatus.EXTRA_TIME_FIRST_HALF -> R.drawable.ball
                MatchStatus.EXTRA_TIME_HALF_TIME -> R.drawable.half_time
                MatchStatus.EXTRA_TIME_SECOND_HALF -> R.drawable.ball
                MatchStatus.PENALTY_SHOOTOUT -> R.drawable.ball
            }

            Icon(
                painter = painterResource(id = matchStatusIcon),
                contentDescription = fixtureData.matchStatus.name,
                tint = matchStatusColor,
                modifier = Modifier.size(screenWidth(x = 16.0))
            )

            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            Text(
                text = fixtureData.matchStatus.name,
                color = matchStatusColor,
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))


            TextButton(
                onClick = {
                    navigateToPostMatchScreen(fixtureData.postMatchAnalysisId.toString(), fixtureData.matchFixtureId.toString(), fixtureData.matchLocationId.toString())
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Match details",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Match details"
                    )
                }
            }
        }
        // Show "Buy Tickets" button if match is PENDING
        if (fixtureData.matchStatus == MatchStatus.PENDING) {
            Button(
                onClick = { /* Handle ticket purchase */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2962FF), // Blue button
                    contentColor = Color.White
                )
            ) {
                Text(text = "Buy Tickets", fontSize = screenFontSize(x = 14.0).sp)
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Icon(
                    painter = painterResource(id = R.drawable.ticket),
                    contentDescription = "Buy Tickets"
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FixturesScreenPreview() {
    LigiopenadminTheme {
        FixturesScreen(
            fixtures = fixtures,
            loadingStatus = LoadingStatus.LOADING,
            navigateToPostMatchScreen = {postMatchId, fixtureId, locationId ->  },
            navigateToFixtureCreationScreen = {}
        )
    }
}