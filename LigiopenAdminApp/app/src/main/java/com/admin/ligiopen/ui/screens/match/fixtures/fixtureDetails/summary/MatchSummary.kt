package com.admin.ligiopen.ui.screens.match.fixtures.fixtureDetails.summary

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.R
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.club
import com.admin.ligiopen.data.network.models.match.commentary.MatchCommentaryData
import com.admin.ligiopen.data.network.models.match.commentary.matchCommentaries
import com.admin.ligiopen.data.network.models.match.events.MatchEventType
import com.admin.ligiopen.data.network.models.match.fixture.FixtureData
import com.admin.ligiopen.data.network.models.match.fixture.MatchStatus
import com.admin.ligiopen.data.network.models.match.fixture.fixture
import com.admin.ligiopen.data.network.models.match.location.MatchLocationData
import com.admin.ligiopen.data.network.models.match.location.matchLocation
import com.admin.ligiopen.ui.screens.match.fixtures.fixtureDetails.HighlightsScreenViewModel
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth

@Composable
fun MatchSummaryComposable(
    matchFixtureData: FixtureData,
    commentaries: List<MatchCommentaryData>,
    matchLocation: MatchLocationData,
    awayClubScore: Int,
    homeClubScore: Int,
    awayClub: ClubData,
    homeClub: ClubData,
    modifier: Modifier = Modifier
) {
    val viewModel: HighlightsScreenViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier) {
        MatchSummary(
            matchFixtureData = matchFixtureData,
            commentaries = commentaries,
            matchLocation = matchLocation,
            awayClubScore = awayClubScore,
            homeClubScore = homeClubScore,
            awayClub = awayClub,
            homeClub = homeClub
        )
    }
}

@Composable
fun MatchSummary(
    matchFixtureData: FixtureData,
    commentaries: List<MatchCommentaryData>,
    matchLocation: MatchLocationData,
    awayClubScore: Int,
    homeClubScore: Int,
    awayClub: ClubData,
    homeClub: ClubData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box {
            if(matchLocation.photos?.isNotEmpty() == true) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(matchLocation.photos?.get(0)?.link)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Match location",
                    modifier = Modifier
                        .background(Color.Black)
                        .alpha(0.3f)
                        .fillMaxWidth()
                        .height(screenHeight(x = 250.0))
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = homeClub.clubAbbreviation ?: "${homeClub.name.take(3)} FC",
                        color = Color.White,
                        fontSize = screenFontSize(x = 18.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(homeClub.clubLogo.link)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Home club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 50.0))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Card(
                        shape = RoundedCornerShape(
                            topStart = screenWidth(x = 5.0),
                            bottomStart = screenWidth(x = 5.0)
                        )
                    ) {
                        Text(
                            text = homeClubScore.toString(),
                            fontSize = screenFontSize(x = 18.0).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(screenWidth(x = 12.0))
                        )
                    }
                    ElevatedCard(
                        shape = RoundedCornerShape(
                            topEnd = screenWidth(x = 5.0),
                            bottomEnd = screenWidth(x = 5.0)
                        )
                    ) {
                        Text(
                            text = awayClubScore.toString(),
                            fontSize = screenFontSize(x = 18.0).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(screenWidth(x = 12.0))
                        )
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(awayClub.clubLogo.link)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Away club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 50.0))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = awayClub.clubAbbreviation ?: "${awayClub.name.take(3)} FC",
                        color = Color.White,
                        fontSize = screenFontSize(x = 18.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if(matchFixtureData.matchStatus == MatchStatus.FIRST_HALF || matchFixtureData.matchStatus == MatchStatus.SECOND_HALF || matchFixtureData.matchStatus == MatchStatus.HALF_TIME || matchFixtureData.matchStatus == MatchStatus.SECOND_HALF || matchFixtureData.matchStatus == MatchStatus.EXTRA_TIME_FIRST_HALF || matchFixtureData.matchStatus == MatchStatus.EXTRA_TIME_SECOND_HALF || matchFixtureData.matchStatus == MatchStatus.PENALTY_SHOOTOUT) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                vertical = screenHeight(x = 8.0),
                                horizontal = screenWidth(x = 16.0)
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.live),
                            contentDescription = "Live match",
                            modifier = Modifier
                                .size(screenWidth(x = 48.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = matchFixtureData.matchStatus.name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() },
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(
                    horizontal = screenWidth(x = 16.0),
                    vertical = screenHeight(x = 16.0)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = homeClub.clubAbbreviation ?: "${homeClub.name.take(3)} FC",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 18.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(homeClub.clubLogo.link)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Home club logo",
                    modifier = Modifier
                        .size(screenWidth(x = 24.0))
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.weight(1f))
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(awayClub.clubLogo.link)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Away club logo",
                    modifier = Modifier
                        .size(screenWidth(x = 24.0))
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = awayClub.clubAbbreviation ?: "${awayClub.name.take(3)} FC",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 18.0).sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            commentaries.forEach { commentary ->
                MatchEventCell(commentary = commentary)
                HorizontalDivider()
            }
        }
    }
}



@Composable
fun MatchEventCell(
    commentary: MatchCommentaryData,
    modifier: Modifier = Modifier
) {
    if(commentary.mainPlayer?.clubId == commentary.homeClub.clubId) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(screenWidth(x = 8.0))
        ) {
            Text(
                text = "${commentary.minute}'",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(screenWidth(x = 16.0)))
            when(commentary.matchEventType) {
                MatchEventType.GOAL -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.goal),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                }
                MatchEventType.OWN_GOAL -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.goal),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Text(
                            text = "${commentary.mainPlayer.username} (OWN)",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                }
                MatchEventType.SUBSTITUTION -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.substitution),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Column {
                            Text(
                                text = "In: ${commentary.mainPlayer.username}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp,
                            )
                            Text(
                                text = "Out: ${commentary.secondaryPlayer?.username}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp,
                            )
                        }
                    }
                }
                MatchEventType.FOUL -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(commentary.foulEvent!!.isYellowCard) {
                            Image(
                                painter = painterResource(id = R.drawable.yellow_card),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                            )
                        } else if (commentary.foulEvent.isRedCard) {
                            Image(
                                painter = painterResource(id = R.drawable.red_card),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.foul),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                            )
                        }
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Column {
                            Text(
                                text = "Offender: ${commentary.mainPlayer.username}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp,
                            )
                            Text(
                                text = "Victim: ${commentary.secondaryPlayer?.username}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp,
                            )
                        }
                    }
                }
                MatchEventType.YELLOW_CARD -> {}
                MatchEventType.RED_CARD -> {}
                MatchEventType.OFFSIDE -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.offside),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                }
                MatchEventType.CORNER_KICK -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.corner_kick),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                }
                MatchEventType.FREE_KICK -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.free_kick),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        if(commentary.penaltyEvent!!.isScored) {
                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                text = "(Scored)",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                text = "(Missed)",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                MatchEventType.PENALTY -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.free_kick),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        if(commentary.penaltyEvent!!.isScored) {
                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                text = "(Scored)",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                text = "(Missed)",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                MatchEventType.PENALTY_MISSED -> {}
                MatchEventType.INJURY -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.injury),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                }
                MatchEventType.THROW_IN -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.throw_in),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                }
                MatchEventType.GOAL_KICK -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.goal_kick),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                }
                MatchEventType.KICK_OFF -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kick-Off",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                MatchEventType.HALF_TIME -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Half-Time",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                MatchEventType.FULL_TIME -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Full-Time",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    } else if(commentary.mainPlayer?.clubId == commentary.awayClub.clubId) {
        Row(
            modifier = Modifier
                .padding(screenWidth(x = 8.0))
        ) {
            Spacer(modifier = Modifier.weight(1f))
            when(commentary.matchEventType) {
                MatchEventType.GOAL -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Image(
                            painter = painterResource(id = R.drawable.goal),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                }
                MatchEventType.OWN_GOAL -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${commentary.mainPlayer.username} (OWN)",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Image(
                            painter = painterResource(id = R.drawable.goal),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                }
                MatchEventType.SUBSTITUTION -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "In: ${commentary.mainPlayer.username}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp,
                            )
                            Text(
                                text = "Out: ${commentary.secondaryPlayer?.username}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp,
                            )
                        }
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Image(
                            painter = painterResource(id = R.drawable.substitution),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                }
                MatchEventType.FOUL -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Offender: ${commentary.mainPlayer.username}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp,
                            )
                            Text(
                                text = "Victim: ${commentary.secondaryPlayer?.username}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp,
                            )
                        }
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        if(commentary.foulEvent!!.isYellowCard) {
                            Image(
                                painter = painterResource(id = R.drawable.yellow_card),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                            )
                        } else if (commentary.foulEvent.isRedCard) {
                            Image(
                                painter = painterResource(id = R.drawable.red_card),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.foul),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                            )
                        }
                    }
                }
                MatchEventType.YELLOW_CARD -> {}
                MatchEventType.RED_CARD -> {}
                MatchEventType.OFFSIDE -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Image(
                            painter = painterResource(id = R.drawable.offside),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                }
                MatchEventType.CORNER_KICK -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Image(
                            painter = painterResource(id = R.drawable.corner_kick),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                }
                MatchEventType.FREE_KICK -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        if(commentary.penaltyEvent?.isScored != null) {
                            if(commentary.penaltyEvent.isScored) {
                                Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                                Text(
                                    text = "(Scored)",
                                    fontSize = screenFontSize(x = 14.0).sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                                Text(
                                    text = "(Missed)",
                                    fontSize = screenFontSize(x = 14.0).sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                text = "(Missed)",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Image(
                            painter = painterResource(id = R.drawable.free_kick),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                }
                MatchEventType.PENALTY -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        if(commentary.penaltyEvent!!.isScored) {
                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                text = "(Scored)",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                text = "(Missed)",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Image(
                            painter = painterResource(id = R.drawable.free_kick),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                }
                MatchEventType.PENALTY_MISSED -> {}
                MatchEventType.INJURY -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Image(
                            painter = painterResource(id = R.drawable.injury),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                }
                MatchEventType.THROW_IN -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Image(
                            painter = painterResource(id = R.drawable.throw_in),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                }
                MatchEventType.GOAL_KICK -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = commentary.mainPlayer.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Image(
                            painter = painterResource(id = R.drawable.goal_kick),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                }
                MatchEventType.KICK_OFF -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kick-Off",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                MatchEventType.HALF_TIME -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Half-Time",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                MatchEventType.FULL_TIME -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Full-Time",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 16.0)))
            Text(
                text = "${commentary.minute}'",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        if(commentary.matchEventType == MatchEventType.KICK_OFF) {
            HorizontalDivider()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = screenHeight(x = 8.0)
                    )
            ) {
                Text(
                    text = "${commentary.minute}'",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(commentary.homeClub!!.clubLogo?.link)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Text(
                        text = "Kickoff".uppercase(),
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(commentary.awayClub!!.clubLogo?.link)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .clip(CircleShape)
                    )
                }
            }
        } else if(commentary.matchEventType == MatchEventType.HALF_TIME) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = screenHeight(x = 8.0)
                    )
            ) {
                Text(
                    text = "${commentary.minute}'",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(commentary.homeClub!!.clubLogo?.link)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Text(
                        text = commentary.halfTimeEvent?.homeClubScore.toString(),
                        fontSize = screenFontSize(x = 16.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Text(
                        text = "Half-time".uppercase(),
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Text(
                        text = commentary.halfTimeEvent?.awayClubScore.toString(),
                        fontSize = screenFontSize(x = 16.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(commentary.awayClub!!.clubLogo?.link)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .clip(CircleShape)
                    )
                }
            }
        } else if(commentary.matchEventType == MatchEventType.FULL_TIME) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = screenHeight(x = 8.0)
                    )
            ) {
                Text(
                    text = "${commentary.minute}'",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(commentary.homeClub!!.clubLogo?.link)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Text(
                        text = commentary.fullTimeEvent?.homeClubScore.toString(),
                        fontSize = screenFontSize(x = 16.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Text(
                        text = "Full-time".uppercase(),
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Text(
                        text = commentary.fullTimeEvent?.awayClubScore.toString(),
                        fontSize = screenFontSize(x = 16.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(commentary.awayClub!!.clubLogo?.link)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .clip(CircleShape)
                    )
                }

            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MatchSummaryPreview() {
    LigiopenadminTheme {
        MatchSummary(
            matchFixtureData = fixture,
            commentaries = matchCommentaries,
            matchLocation = matchLocation,
            awayClub = club,
            homeClub = club,
            awayClubScore = 0,
            homeClubScore = 0
        )
    }
}