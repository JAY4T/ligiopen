package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.summary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.club.club
import com.jabulani.ligiopen.data.network.model.location.MatchLocationData
import com.jabulani.ligiopen.data.network.model.location.matchLocation
import com.jabulani.ligiopen.data.network.model.match.commentary.MatchCommentaryData
import com.jabulani.ligiopen.data.network.model.match.commentary.matchCommentaries
import com.jabulani.ligiopen.data.network.model.match.events.MatchEventType
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.data.network.model.match.fixture.MatchStatus
import com.jabulani.ligiopen.data.network.model.match.fixture.fixture
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus
import com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.HighlightsScreenViewModel
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.ui.theme.errorLight
import com.jabulani.ligiopen.ui.theme.onSurfaceLight
import com.jabulani.ligiopen.ui.theme.onSurfaceVariantLight
import com.jabulani.ligiopen.ui.theme.primaryLight
import com.jabulani.ligiopen.ui.theme.surfaceContainerHighLight
import com.jabulani.ligiopen.ui.theme.tertiaryLight
import com.jabulani.ligiopen.ui.utils.formatIsoDateTime
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth
import java.time.LocalDateTime

@Composable
fun MatchSummaryComposable(
    matchStatus: MatchStatus,
    matchFixtureData: FixtureData,
    commentaries: List<MatchCommentaryData>,
    matchLocation: MatchLocationData,
    awayClubScore: Int,
    homeClubScore: Int,
    awayClub: ClubDetails,
    homeClub: ClubDetails,
    loadingStatus: LoadingStatus,
    modifier: Modifier = Modifier
) {
    val viewModel: HighlightsScreenViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier) {
        MatchSummary(
            matchStatus = matchStatus,
            matchFixtureData = matchFixtureData,
            commentaries = commentaries,
            matchLocation = matchLocation,
            awayClubScore = awayClubScore,
            homeClubScore = homeClubScore,
            awayClub = awayClub,
            loadingStatus = loadingStatus,
            homeClub = homeClub
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchSummary(
    matchStatus: MatchStatus,
    matchFixtureData: FixtureData,
    commentaries: List<MatchCommentaryData>,
    matchLocation: MatchLocationData,
    awayClubScore: Int,
    homeClubScore: Int,
    awayClub: ClubDetails,
    homeClub: ClubDetails,
    loadingStatus: LoadingStatus,
    modifier: Modifier = Modifier
) {
    val primaryColor = primaryLight
    val surfaceColor = surfaceContainerHighLight
    val onSurfaceColor = onSurfaceLight
    val accentColor = tertiaryLight

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with stadium image and score
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            // Stadium image with parallax effect
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(matchLocation.photos?.get(0)?.link)
                    .crossfade(true)
                    .build(),
                contentDescription = "Stadium",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(280.0))
                    .graphicsLayer {
                        alpha = 0.9f
                    }
            )

            // Dark overlay for better text visibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // Score display
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = screenWidth(16.0)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Home team
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = homeClub.clubLogo.link,
                        contentDescription = homeClub.name,
                        modifier = Modifier
                            .size(screenWidth(64.0))
                            .clip(CircleShape)
                            .border(screenWidth(2.0), primaryColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(screenHeight(8.0)))
                    Text(
                        text = homeClub.clubAbbreviation ?: homeClub.name.take(3).uppercase(),
                        color = Color.White,
                        fontSize = screenFontSize(18.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Score
                Card(
                    modifier = Modifier
                        .padding(horizontal = screenWidth(16.0))
                        .width(screenWidth(120.0)),
                    shape = RoundedCornerShape(screenWidth(12.0)),
                    elevation = CardDefaults.cardElevation(screenHeight(8.0)),
                    colors = CardDefaults.cardColors(
                        containerColor = surfaceColor
                    ),
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = screenHeight(8.0)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = homeClubScore.toString(),
                            color = primaryColor,
                            fontSize = screenFontSize(32.0).sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = ":",
                            color = onSurfaceColor,
                            fontSize = screenFontSize(32.0).sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = awayClubScore.toString(),
                            color = primaryColor,
                            fontSize = screenFontSize(32.0).sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Away team
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = awayClub.clubLogo.link,
                        contentDescription = awayClub.name,
                        modifier = Modifier
                            .size(screenWidth(64.0))
                            .clip(CircleShape)
                            .border(screenWidth(2.0), primaryColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(screenHeight(8.0)))
                    Text(
                        text = awayClub.clubAbbreviation ?: awayClub.name.take(3).uppercase(),
                        color = Color.White,
                        fontSize = screenFontSize(18.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Match status indicator
            if (matchStatus.isLive()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(screenWidth(16.0))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                color = errorLight.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(screenWidth(16.0))
                            )
                            .padding(horizontal = screenWidth(12.0), vertical = screenHeight(6.0))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.live),
                            contentDescription = "Live",
                            tint = Color.White,
                            modifier = Modifier.size(screenWidth(16.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(8.0)))
                        Text(
                            text = matchStatus.displayName(),
                            color = Color.White,
                            fontSize = screenFontSize(14.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Match details
        if (matchStatus == MatchStatus.PENDING && loadingStatus != LoadingStatus.LOADING) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(screenWidth(32.0))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = "Pending",
                        tint = primaryColor.copy(alpha = 0.6f),
                        modifier = Modifier.size(screenWidth(64.0))
                    )
                    Spacer(modifier = Modifier.height(screenHeight(16.0)))
                    Text(
                        text = "Match Summary Not Available",
                        style = MaterialTheme.typography.labelMedium,
                        color = primaryColor,
                        fontSize = screenFontSize(14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(screenHeight(8.0)))
                    Text(
                        text = "Check back when the match starts",
                        color = onSurfaceVariantLight,
                        fontSize = screenFontSize(14.0).sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = screenWidth(16.0))
            ) {
                // Match info card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = screenHeight(16.0)),
                    elevation = CardDefaults.cardElevation(screenHeight(4.0)),
                    shape = RoundedCornerShape(screenWidth(12.0)),
                    colors = CardDefaults.cardColors(
                        containerColor = surfaceColor
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(screenWidth(16.0))
                    ) {
                        // Match date and time
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = screenHeight(8.0))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar),
                                contentDescription = "Date",
                                tint = primaryColor,
                                modifier = Modifier.size(screenWidth(20.0))
                            )
                            Spacer(modifier = Modifier.width(screenWidth(8.0)))
                            Text(
                                text = formatIsoDateTime(LocalDateTime.parse(matchFixtureData.matchDateTime)),
                                color = onSurfaceColor,
                                fontSize = screenFontSize(14.0).sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Stadium info
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = screenHeight(8.0))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.stadium),
                                contentDescription = "Stadium",
                                tint = primaryColor,
                                modifier = Modifier.size(screenWidth(20.0))
                            )
                            Spacer(modifier = Modifier.width(screenWidth(8.0)))
                            Text(
                                text = matchLocation.venueName,
                                color = onSurfaceColor,
                                fontSize = screenFontSize(14.0).sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Attendance (if available)
//                        if (matchFixtureData.attendance != null) {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.attendance),
//                                    contentDescription = "Attendance",
//                                    tint = primaryColor,
//                                    modifier = Modifier.size(20.dp)
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(
//                                    text = "Attendance: ${NumberFormat.getNumberInstance(Locale.US).format(matchFixtureData.attendance)}",
//                                    color = onSurfaceColor,
//                                    fontWeight = FontWeight.SemiBold
//                                )
//                            }
//                        }
                    }
                }

                // Match events header
                Text(
                    text = "MATCH EVENTS",
                    style = MaterialTheme.typography.bodySmall,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = screenWidth(8.0)),
                    fontSize = screenFontSize(14.0).sp,
                )

                // Match events timeline
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = screenHeight(16.0))
                ) {
                    commentaries.forEach { commentary ->
                        MatchEventCell(commentary = commentary)
                        Spacer(modifier = Modifier.height(screenHeight(4.0)))
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchStatus.isLive(): Boolean {
    return this == MatchStatus.FIRST_HALF ||
            this == MatchStatus.SECOND_HALF ||
            this == MatchStatus.HALF_TIME ||
            this == MatchStatus.EXTRA_TIME_FIRST_HALF ||
            this == MatchStatus.EXTRA_TIME_SECOND_HALF ||
            this == MatchStatus.PENALTY_SHOOTOUT
}

@Composable
private fun MatchStatus.displayName(): String {
    return when (this) {
        MatchStatus.FIRST_HALF -> "1st Half"
        MatchStatus.SECOND_HALF -> "2nd Half"
        MatchStatus.HALF_TIME -> "Half Time"
        MatchStatus.EXTRA_TIME_FIRST_HALF -> "ET 1st Half"
        MatchStatus.EXTRA_TIME_SECOND_HALF -> "ET 2nd Half"
        MatchStatus.PENALTY_SHOOTOUT -> "Penalties"
        else -> this.name
    }
}

@Composable
fun MatchEventCell(
    commentary: MatchCommentaryData,
    modifier: Modifier = Modifier
) {
    val primaryColor = primaryLight
    val surfaceColor = surfaceContainerHighLight
    val onSurfaceColor = onSurfaceLight

    val isHomeTeam = commentary.mainPlayer?.clubId == commentary.homeClub.clubId
    val isNeutralEvent = commentary.matchEventType == MatchEventType.KICK_OFF ||
            commentary.matchEventType == MatchEventType.HALF_TIME ||
            commentary.matchEventType == MatchEventType.FULL_TIME

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = screenHeight(4.0)),
        elevation = CardDefaults.cardElevation(screenHeight(2.0)),
        shape = RoundedCornerShape(screenWidth(8.0)),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        )
    ) {
        if (isNeutralEvent) {
            // Neutral events (kickoff, half time, full time)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(screenWidth(12.0)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = commentary.homeClub.clubLogo.link,
                        contentDescription = commentary.homeClub.name,
                        modifier = Modifier
                            .size(screenWidth(24.0))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = commentary.matchEventType.displayName(),
                        color = primaryColor,
                        fontSize = screenFontSize(14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(16.0)))
                    AsyncImage(
                        model = commentary.awayClub.clubLogo.link,
                        contentDescription = commentary.awayClub.name,
                        modifier = Modifier
                            .size(screenWidth(24.0))
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(4.0)))
                Text(
                    text = "${commentary.minute}'",
                    color = onSurfaceVariantLight,
                    fontSize = screenFontSize(14.0).sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            // Team-specific events
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(screenWidth(12.0)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isHomeTeam) Arrangement.Start else Arrangement.End
            ) {
                if (isHomeTeam) {
                    // Home team event layout
                    MinuteBadge(commentary.minute.takeIf { it != null }.toString())
                    Spacer(modifier = Modifier.width(screenWidth(16.0)))
                    EventContent(commentary, isHomeTeam = true)
                } else {
                    // Away team event layout
                    EventContent(commentary, isHomeTeam = false)
                    Spacer(modifier = Modifier.width(screenWidth(16.0)))
                    MinuteBadge(commentary.minute.takeIf { it != null }.toString())
                }
            }
        }
    }
}

@Composable
private fun MinuteBadge(minute: String) {
    Box(
        modifier = Modifier
            .background(
                color = primaryLight.copy(alpha = 0.2f),
                shape = RoundedCornerShape(screenWidth(4.0))
            )
            .padding(horizontal = screenWidth(8.0), vertical = screenHeight(4.0))
    ) {
        Text(
            text = "$minute'",
            fontSize = screenFontSize(14.0).sp,
            color = primaryLight,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EventContent(
    commentary: MatchCommentaryData,
    isHomeTeam: Boolean
) {
    val eventConfig = remember(commentary.matchEventType) {
        when (commentary.matchEventType) {
            MatchEventType.GOAL -> EventConfig(
                icon = R.drawable.goal,
                iconTint = Color(0xFF4CAF50)
            )
            MatchEventType.OWN_GOAL -> EventConfig(
                icon = R.drawable.goal,
                iconTint = Color(0xFFF44336)
            )
            MatchEventType.SUBSTITUTION -> EventConfig(
                icon = R.drawable.substitution,
                iconTint = primaryLight
            )
            MatchEventType.FOUL -> EventConfig(
                icon = when {
                    commentary.foulEvent?.isRedCard == true -> R.drawable.red_card
                    commentary.foulEvent?.isYellowCard == true -> R.drawable.yellow_card
                    else -> R.drawable.foul
                },
                iconTint = when {
                    commentary.foulEvent?.isRedCard == true -> Color(0xFFF44336)
                    commentary.foulEvent?.isYellowCard == true -> Color(0xFFFFC107)
                    else -> primaryLight
                }
            )
            MatchEventType.PENALTY -> EventConfig(
                icon = R.drawable.penalty_kick,
                iconTint = primaryLight
            )
            MatchEventType.CORNER_KICK -> EventConfig(
                icon = R.drawable.corner_kick,
                iconTint = primaryLight
            )
            MatchEventType.FREE_KICK -> EventConfig(
                icon = R.drawable.free_kick,
                iconTint = primaryLight
            )
            MatchEventType.INJURY -> EventConfig(
                icon = R.drawable.injury,
                iconTint = Color(0xFFF44336)
            )
            else -> EventConfig(
                icon = R.drawable.stadium,
                iconTint = primaryLight
            )
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isHomeTeam) Arrangement.Start else Arrangement.End
    ) {
        if (isHomeTeam) {
            Icon(
                painter = painterResource(id = eventConfig.icon),
                contentDescription = commentary.matchEventType.displayName(),
                tint = eventConfig.iconTint,
                modifier = Modifier.size(screenWidth(24.0))
            )
            Spacer(modifier = Modifier.width(screenWidth(8.0)))
        }

        Column {
            when (commentary.matchEventType) {
                MatchEventType.GOAL -> {
                    Text(
                        text = commentary.mainPlayer?.username ?: "",
                        fontWeight = FontWeight.Bold
                    )
                    if (commentary.matchEventType == MatchEventType.OWN_GOAL) {
                        Text(
                            text = "(Own Goal)",
                            color = Color(0xFFF44336),
                            fontSize = screenFontSize(12.0).sp
                        )
                    }
                }
                MatchEventType.SUBSTITUTION -> {
                    Text(
                        text = "Substitution",
                        color = primaryLight,
                        fontSize = screenFontSize(12.0).sp
                    )
                    Text(
                        text = "In: ${commentary.mainPlayer?.username}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = screenFontSize(14.0).sp,
                    )
                    Text(
                        text = "Out: ${commentary.secondaryPlayer?.username ?: ""}",
                        fontSize = screenFontSize(12.0).sp,
                    )
                }
                MatchEventType.FOUL -> {
                    Text(
                        text = "Foul by ${commentary.mainPlayer?.username}",
                        fontSize = screenFontSize(14.0).sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (commentary.secondaryPlayer != null) {
                        Text(
                            text = "on ${commentary.secondaryPlayer.username}",
                            fontSize = screenFontSize(12.0).sp,
                        )
                    }
                }
                else -> {
                    Text(
                        text = commentary.matchEventType.displayName(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = screenFontSize(14.0).sp,
                    )
                    Text(
                        text = commentary.mainPlayer?.username ?: "",
                        fontSize = screenFontSize(12.0).sp,
                    )
                }
            }
        }

        if (!isHomeTeam) {
            Spacer(modifier = Modifier.width(screenWidth(8.0)))
            Icon(
                painter = painterResource(id = eventConfig.icon),
                contentDescription = commentary.matchEventType.displayName(),
                tint = eventConfig.iconTint,
                modifier = Modifier.size(screenWidth(24.0))
            )
        }
    }
}

private data class EventConfig(
    val icon: Int,
    val iconTint: Color
)

private fun MatchEventType.displayName(): String {
    return when (this) {
        MatchEventType.KICK_OFF -> "Kick Off"
        MatchEventType.HALF_TIME -> "Half Time"
        MatchEventType.FULL_TIME -> "Full Time"
        MatchEventType.FREE_KICK -> "Free Kick"
        else -> this.name
            .lowercase()
            .replace("_", " ")
            .replaceFirstChar { it.uppercase() }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MatchSummaryPreview() {
    LigiopenTheme {
        MatchSummary(
            matchStatus = MatchStatus.COMPLETED,
            matchFixtureData = fixture,
            commentaries = matchCommentaries,
            matchLocation = matchLocation,
            awayClub = club,
            homeClub = club,
            awayClubScore = 0,
            homeClubScore = 0,
            loadingStatus = LoadingStatus.INITIAL
        )
    }
}