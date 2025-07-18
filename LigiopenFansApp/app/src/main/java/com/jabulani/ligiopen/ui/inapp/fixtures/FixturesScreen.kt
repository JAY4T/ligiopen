package com.jabulani.ligiopen.ui.inapp.fixtures

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.data.network.model.match.fixture.MatchStatus
import com.jabulani.ligiopen.data.network.model.match.fixture.fixtures
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus
import com.jabulani.ligiopen.ui.inapp.home.HomeScreenTab
import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.ui.theme.backgroundLight
import com.jabulani.ligiopen.ui.theme.errorLight
import com.jabulani.ligiopen.ui.theme.onErrorLight
import com.jabulani.ligiopen.ui.theme.onPrimaryLight
import com.jabulani.ligiopen.ui.theme.onSurfaceLight
import com.jabulani.ligiopen.ui.theme.onSurfaceVariantLight
import com.jabulani.ligiopen.ui.theme.onTertiaryLight
import com.jabulani.ligiopen.ui.theme.primaryLight
import com.jabulani.ligiopen.ui.theme.surfaceContainerHighLight
import com.jabulani.ligiopen.ui.theme.surfaceLight
import com.jabulani.ligiopen.ui.theme.surfaceVariantLight
import com.jabulani.ligiopen.ui.theme.tertiaryLight
import com.jabulani.ligiopen.ui.utils.formatIsoDateTime
import com.jabulani.ligiopen.ui.utils.formatLocalDate
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth
import java.time.LocalDate
import java.time.LocalDateTime

object FixturesScreenDestination: AppNavigation {
    override val title: String = "Fixtures screen"
    override val route: String = "fixtures-screen"
    val clubId: String = "clubId"
    val routeWithClubId: String = "$route/{$clubId}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FixturesScreenComposable(
    showTopBanner: Boolean = true,
    clubId: Int? = null,
    singleClubMode: Boolean = false,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    navigateToLoginScreenWithArgs: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: FixturesViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setSingleClubMode(singleClubMode, clubId)
        if(clubId != null) {
            viewModel.setClubIsNull(false)
            viewModel.updateClubId(
                clubId = clubId
            )
        } else {
            viewModel.setClubIsNull(true)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when(lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                if(clubId != null) {
                    viewModel.setClubIsNull(false)
                    viewModel.updateClubId(
                        clubId = clubId
                    )
                } else {
                    viewModel.setClubIsNull(true)
                }
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
            showTopBanner = showTopBanner,
            selectedDate = uiState.selectedDate,
            onSelectDate = viewModel::selectDate,
            clubs = uiState.clubs,
            selectedClubs = uiState.selectedClubs,
            onSelectClub = viewModel::selectClubs,
            onDeselectClub = viewModel::deselectClub,
            onResetClubs = viewModel::resetClubs,
            matchDateTimes = uiState.matchDateTimes,
            fixtures = uiState.fixtures,
            loadingStatus = uiState.loadingStatus,
            navigateToPostMatchScreen = navigateToPostMatchScreen,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FixturesScreen(
    showTopBanner: Boolean,
    selectedDate: LocalDate?,
    clubs: List<ClubDetails>,
    selectedClubs: List<ClubDetails>,
    onSelectDate: (date: LocalDate?) -> Unit,
    onSelectClub: (clubDetails: ClubDetails) -> Unit,
    onDeselectClub: (clubDetails: ClubDetails) -> Unit,
    onResetClubs: () -> Unit,
    fixtures: List<FixtureData>,
    matchDateTimes: List<String>,
    loadingStatus: LoadingStatus,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Theme colors
    val primaryColor = primaryLight
    val surfaceColor = surfaceContainerHighLight
    val onSurfaceColor = onSurfaceLight
    val accentColor = tertiaryLight
    val errorColor = errorLight

    val allowedDates = rememberSaveable(matchDateTimes) {
        matchDateTimes.mapNotNull {
            runCatching { LocalDateTime.parse(it).toLocalDate() }.getOrNull()
        }
    }

    var showFilter by rememberSaveable { mutableStateOf(false) }
    var showClubsPicker by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }


    // Game-style date picker
    if (showDatePicker) {
        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Surface(
                shape = RoundedCornerShape(screenWidth(16.0)),
                color = surfaceColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(screenWidth(16.0))
                ) {
                    Text(
                        text = "SELECT MATCH DATE",
                        style = MaterialTheme.typography.headlineMedium,
                        color = primaryColor,
                        modifier = Modifier.padding(bottom = screenHeight(16.0))
                    )

                    LazyColumn(modifier = Modifier.weight(1f, false)) {
                        items(allowedDates) { date ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = screenHeight(4.0)),
                                elevation = if (selectedDate == date) CardDefaults.cardElevation(screenHeight(8.0))
                                else CardDefaults.cardElevation(screenHeight(2.0)),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedDate == date) primaryColor.copy(alpha = 0.2f)
                                    else surfaceVariantLight
                                ),
                            ) {
                                TextButton(
                                    onClick = {
                                        onSelectDate(date)
                                        showDatePicker = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = formatLocalDate(date),
                                        color = if (selectedDate == date) primaryColor
                                        else onSurfaceVariantLight,
                                        fontWeight = if (selectedDate == date) FontWeight.Bold
                                        else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = screenHeight(16.0)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                onSelectDate(null)
                                showDatePicker = false
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = errorColor.copy(alpha = 0.1f),
                                contentColor = errorColor
                            ),
                            border = BorderStroke(screenWidth(1.0), errorColor)
                        ) {
                            Text(
                                "RESET",
                                fontSize = screenFontSize(14.0).sp
                            )
                        }

                        Button(
                            onClick = { showDatePicker = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryColor,
                                contentColor = onPrimaryLight
                            )
                        ) {
                            Text(
                                "DONE",
                                fontSize = screenFontSize(14.0).sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Game-style club picker
    if (showClubsPicker) {
        AlertDialog(
            onDismissRequest = { showClubsPicker = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Surface(
                shape = RoundedCornerShape(screenWidth(16.0)),
                color = surfaceColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(screenWidth(16.0))
                ) {
                    Text(
                        text = "SELECT CLUBS",
                        style = MaterialTheme.typography.headlineMedium,
                        color = primaryColor,
                        modifier = Modifier.padding(bottom = screenHeight(16.0))
                    )

                    LazyColumn(modifier = Modifier.weight(1f, false)) {
                        items(clubs) { club ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = screenHeight(4.0)),
                                elevation = if (selectedClubs.contains(club)) CardDefaults.cardElevation(screenHeight(8.0))
                                else CardDefaults.cardElevation(screenHeight(2.0)),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedClubs.contains(club)) primaryColor.copy(alpha = 0.2f)
                                    else surfaceVariantLight
                                ),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(screenWidth(8.0)),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = club.clubLogo.link,
                                        contentDescription = club.name,
                                        modifier = Modifier
                                            .size(screenWidth(32.0))
                                            .clip(CircleShape)
                                    )

                                    Spacer(modifier = Modifier.width(screenWidth(8.0)))

                                    Text(
                                        text = club.name,
                                        color = if (selectedClubs.contains(club)) primaryColor
                                        else onSurfaceVariantLight,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Icon(
                                        painter = painterResource(
                                            if (selectedClubs.contains(club)) R.drawable.checked_box
                                            else R.drawable.unchecked_box
                                        ),
                                        contentDescription = null,
                                        tint = if (selectedClubs.contains(club)) primaryColor
                                        else onSurfaceVariantLight,
                                        modifier = Modifier
                                            .clickable {
                                                if (selectedClubs.contains(club)) {
                                                    onDeselectClub(club)
                                                } else {
                                                    onSelectClub(club)
                                                }
                                            }
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = screenHeight(16.0)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                onResetClubs()
                                showClubsPicker = false
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = errorColor.copy(alpha = 0.1f),
                                contentColor = errorColor
                            ),
                            border = BorderStroke(screenWidth(1.0), errorColor)
                        ) {
                            Text(
                                "RESET",
                                fontSize = screenFontSize(14.0).sp
                            )
                        }

                        Button(
                            onClick = { showClubsPicker = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryColor,
                                contentColor = onPrimaryLight
                            )
                        ) {
                            Text(
                                "DONE",
                                fontSize = screenFontSize(14.0).sp
                            )
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with game-style gradient
        if (showTopBanner) {
            if (showFilter) {
                // Filter controls with game aesthetics
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(screenWidth(8.0)),
                    elevation = CardDefaults.cardElevation(screenHeight(8.0)),
                    colors = CardDefaults.cardColors(
                        containerColor = surfaceColor
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(screenWidth(8.0))
                    ) {
                        // Date filter button
                        OutlinedButton(
                            onClick = { showDatePicker = true },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = surfaceVariantLight,
                                contentColor = primaryColor
                            ),
                            border = BorderStroke(screenWidth(1.0), primaryColor),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.calendar),
                                    contentDescription = "Date",
                                    tint = primaryColor
                                )
                                Spacer(modifier = Modifier.width(screenWidth(8.0)))
                                Text(
                                    text = selectedDate?.let { formatLocalDate(it) } ?: "All Dates",
                                    fontSize = screenFontSize(14.0).sp,
                                    color = primaryColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(screenWidth(8.0)))

                        // Club filter button
                        OutlinedButton(
                            onClick = { showClubsPicker = true },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = surfaceVariantLight,
                                contentColor = primaryColor
                            ),
                            border = BorderStroke(screenWidth(1.0), primaryColor),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.team),
                                    contentDescription = "Clubs",
                                    tint = primaryColor
                                )
                                Spacer(modifier = Modifier.width(screenWidth(8.0)))
                                Text(
                                    text = if (selectedClubs.isEmpty()) "All Clubs"
                                    else "${selectedClubs.size} Selected",
                                    fontSize = screenFontSize(14.0).sp,
                                    color = primaryColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(screenWidth(8.0)))

                        // Close button
                        IconButton(
                            onClick = { showFilter = false },
                            modifier = Modifier
                                .size(screenWidth(40.0))
                                .background(primaryColor, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = onPrimaryLight
                            )
                        }
                    }
                }
            } else {
                // Main header with game logo and title
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
                        text = HomeScreenTab.MATCHES.name
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

                    // Filter button with badge if filters are active
                    Box {
                        IconButton(
                            onClick = { showFilter = true },
                            modifier = Modifier
                                .size(screenWidth(40.0))
                                .background(accentColor, CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.filter),
                                contentDescription = "Filter",
                                tint = onTertiaryLight
                            )
                        }

                        // Show badge if any filters are active
                        if (selectedDate != null || selectedClubs.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(screenWidth(16.0))
                                    .background(errorColor, CircleShape)
                                    .border(2.dp, surfaceColor, CircleShape)
                            ) {
                                Text(
                                    text = "!",
                                    color = onErrorLight,
                                    fontSize = screenFontSize(10.0).sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
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
                    if (fixtures.isEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.cancel_event),
                                    contentDescription = "No matches",
                                    tint = primaryColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(screenWidth(64.0))
                                )
                                Spacer(modifier = Modifier.height(screenHeight(16.0)))
                                Text(
                                    text = "NO MATCHES FOUND",
                                    color = primaryColor.copy(alpha = 0.7f),
                                    fontSize = screenFontSize(16.0).sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (selectedDate != null || selectedClubs.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(screenHeight(8.0)))
                                    TextButton(
                                        onClick = {
                                            onSelectDate(null)
                                            onResetClubs()
                                        }
                                    ) {
                                        Text(
                                            "RESET FILTERS",
                                            fontSize = screenFontSize(14.0).sp,
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(screenWidth(12.0)),
                            contentPadding = PaddingValues(vertical = screenHeight(8.0))
                        ) {
                            items(fixtures) { fixture ->
                                FixtureCard(
                                    fixtureData = fixture,
                                    navigateToPostMatchScreen = navigateToPostMatchScreen,
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
fun FixtureCard(
    fixtureData: FixtureData,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = primaryLight
    val accentColor = tertiaryLight
    val backgroundColor = backgroundLight
    val surfaceColor = surfaceLight
    val onSurfaceColor = onSurfaceLight
    val surfaceVariantColor = surfaceVariantLight
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

    val matchStatusIcon = when (fixtureData.matchStatus) {
        MatchStatus.PENDING -> R.drawable.clock
        MatchStatus.COMPLETED -> R.drawable.check_mark
        MatchStatus.CANCELLED -> R.drawable.close
        MatchStatus.FIRST_HALF -> R.drawable.ball
        MatchStatus.HALF_TIME -> R.drawable.half_time
        MatchStatus.SECOND_HALF -> R.drawable.ball
        MatchStatus.EXTRA_TIME_FIRST_HALF -> R.drawable.ball
        MatchStatus.EXTRA_TIME_HALF_TIME -> R.drawable.half_time
        MatchStatus.EXTRA_TIME_SECOND_HALF -> R.drawable.ball
        MatchStatus.PENALTY_SHOOTOUT -> R.drawable.ball
    }

    Card(
        onClick = {
            navigateToPostMatchScreen(
                fixtureData.postMatchAnalysisId.toString(),
                fixtureData.matchFixtureId.toString(),
                fixtureData.matchLocationId.toString()
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .shadow(screenWidth(4.0), shape = RoundedCornerShape(screenWidth(12.0))),
        shape = RoundedCornerShape(screenWidth(12.0)),
        elevation = CardDefaults.cardElevation(screenHeight(4.0)),
        colors = CardDefaults.cardColors(
            containerColor = surfaceContainerHighLight
        ),
    ) {
        Column(
            modifier = Modifier.padding(screenWidth(16.0))
        ) {
            // Match status bar
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
                    painter = painterResource(id = matchStatusIcon),
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
                    fontSize = screenFontSize(12.0).sp,
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
                        text = fixtureData.homeClub.name,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        fontSize = screenFontSize(14.0).sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
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
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Black,
                                fontSize = screenFontSize(14.0).sp,
                            )
                            Text(
                                text = " : ",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Black,
                                fontSize = screenFontSize(14.0).sp,
                            )
                            Text(
                                text = fixtureData.awayClubScore?.toString() ?: "0",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Black,
                                fontSize = screenFontSize(14.0).sp,
                            )
                        }
                    } else {
                        Text(
                            text = "VS",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = primaryColor,
                            fontSize = screenFontSize(14.0).sp,
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
                    Spacer(modifier = Modifier.height(screenHeight(4.0)))
                    Text(
                        text = fixtureData.awayClub.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = screenFontSize(14.0).sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
fun FixturesScreenPreview() {
    LigiopenTheme {
        FixturesScreen(
            showTopBanner = true,
            selectedClubs = emptyList(),
            onSelectClub = {},
            onResetClubs = {},
            onSelectDate = {},
            selectedDate = null,
            clubs = emptyList(),
            onDeselectClub = {},
            matchDateTimes = emptyList(),
            fixtures = fixtures,
            loadingStatus = LoadingStatus.SUCCESS,
            navigateToPostMatchScreen = {postMatchId, fixtureId, locationId ->  }
        )
    }
}