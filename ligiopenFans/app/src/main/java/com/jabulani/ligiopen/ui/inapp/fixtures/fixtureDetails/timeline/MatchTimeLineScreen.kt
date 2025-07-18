package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.match.commentary.MatchCommentaryData
import com.jabulani.ligiopen.data.network.model.match.commentary.matchCommentaries
import com.jabulani.ligiopen.data.network.model.match.events.MatchEventType
import com.jabulani.ligiopen.data.network.model.match.fixture.MatchStatus
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth

@Composable
fun MatchTimelineScreenComposable(
    matchStatus: MatchStatus,
    commentaries: List<MatchCommentaryData>,
    fixtureId: String?,
    modifier: Modifier = Modifier
) {
//    val viewModel: HighlightsScreenViewModel = viewModel(factory = AppViewModelFactory.Factory)
//    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
    ) {
        MatchTimelineScreen(
            matchStatus = matchStatus,
            fixtureId = fixtureId,
            matchCommentaries = commentaries,
        )
    }
}

@Composable
fun MatchTimelineScreen(
    matchStatus: MatchStatus,
    fixtureId: String?,
    matchCommentaries: List<MatchCommentaryData>,
    modifier: Modifier = Modifier
) {
    Scaffold(

    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            if(matchStatus == MatchStatus.PENDING) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Match timeline not available",
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Check back when the match starts",
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(
                            start = screenWidth(x = 16.0),
                            top = 0.dp,
                            end = screenWidth(x = 16.0),
                            bottom = screenHeight(x = 16.0),
                        )
                ) {
                    LazyColumn {
                        items(matchCommentaries) { matchCommentary ->
                            SingleMatchCommentaryComposable(
                                matchCommentaryData = matchCommentary
                            )
                            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun SingleMatchCommentaryComposable(
    matchCommentaryData: MatchCommentaryData,
    modifier: Modifier = Modifier
) {
    val icon = when(matchCommentaryData.matchEventType) {
        MatchEventType.GOAL -> R.drawable.goal
        MatchEventType.OWN_GOAL -> R.drawable.goal
        MatchEventType.SUBSTITUTION -> R.drawable.substitution
        MatchEventType.FOUL -> R.drawable.foul
        MatchEventType.YELLOW_CARD -> R.drawable.yellow_card
        MatchEventType.RED_CARD -> R.drawable.red_card
        MatchEventType.OFFSIDE -> R.drawable.offside
        MatchEventType.CORNER_KICK -> R.drawable.corner_kick
        MatchEventType.FREE_KICK -> R.drawable.free_kick
        MatchEventType.PENALTY -> R.drawable.penalty_kick
        MatchEventType.PENALTY_MISSED -> R.drawable.penalty_kick
        MatchEventType.INJURY -> R.drawable.injury
        MatchEventType.THROW_IN -> R.drawable.throw_in
        MatchEventType.GOAL_KICK -> R.drawable.goal_kick
        MatchEventType.KICK_OFF -> R.drawable.kicking_ball
        MatchEventType.HALF_TIME -> R.drawable.half_2
        MatchEventType.FULL_TIME -> R.drawable.full_time2
    }

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = matchCommentaryData.matchEventType.name,
                modifier = Modifier
                    .size(screenWidth(x = 24.0))
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Text(text = "${matchCommentaryData.minute}'")
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
            )
        }
        Spacer(modifier = Modifier.width(screenWidth(x = 16.0)))
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(screenWidth(x = 16.0))
            ) {
                Row {
                    if(matchCommentaryData.matchEventType != MatchEventType.KICK_OFF && matchCommentaryData.matchEventType != MatchEventType.HALF_TIME && matchCommentaryData.matchEventType != MatchEventType.FULL_TIME) {
                        Text(
                            text = matchCommentaryData.matchEventType.name.lowercase().replace("_", "").replaceFirstChar { it.uppercase() },
                            fontWeight = FontWeight.Bold,
                            fontSize = screenFontSize(x = 16.0).sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    when(matchCommentaryData.matchEventType) {
                        MatchEventType.GOAL -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.OWN_GOAL -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.SUBSTITUTION -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.FOUL -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.YELLOW_CARD -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.RED_CARD -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.OFFSIDE -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.CORNER_KICK -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.FREE_KICK -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.PENALTY -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.PENALTY_MISSED -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.INJURY -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.THROW_IN -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.GOAL_KICK -> {
                            val club: ClubDetails = if(matchCommentaryData.mainPlayer!!.clubId == matchCommentaryData.homeClub.clubId) matchCommentaryData.homeClub else matchCommentaryData.awayClub
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(club.clubLogo.link)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Club logo",
                                modifier = Modifier
                                    .size(screenWidth(x = 24.0))
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                            )
                        }
                        MatchEventType.KICK_OFF -> {

                        }
                        MatchEventType.HALF_TIME -> {

                        }
                        MatchEventType.FULL_TIME -> {

                        }
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                when(matchCommentaryData.matchEventType) {
                    MatchEventType.GOAL -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))
//                                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.goalEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Scorer:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                        if(matchCommentaryData.secondaryPlayer != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Assist:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                                Text(
                                    text = matchCommentaryData.secondaryPlayer!!.username,
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                    MatchEventType.OWN_GOAL -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))

                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.ownGoalEvent?.summary ?: "Own goal by Gor Mahia team",
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Own scorer:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                    MatchEventType.SUBSTITUTION -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))

                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.substitutionEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "In:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                        if(matchCommentaryData.secondaryPlayer != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Out:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                                Text(
                                    text = matchCommentaryData.secondaryPlayer!!.username,
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                    MatchEventType.FOUL -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))

                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.foulEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Victim:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                        if(matchCommentaryData.secondaryPlayer != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Offender:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                                Text(
                                    text = matchCommentaryData.secondaryPlayer!!.username,
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                    MatchEventType.YELLOW_CARD -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))
                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.goalEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                    }
                    MatchEventType.RED_CARD -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))
                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.goalEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                    }
                    MatchEventType.OFFSIDE -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))
                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.offsideEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Offside player:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                    MatchEventType.CORNER_KICK -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))

                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.cornerEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Corner kick taker:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                    MatchEventType.FREE_KICK -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))

                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.freeKickEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Free kick taker:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                    MatchEventType.PENALTY -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))

                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.penaltyEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Penalty taker:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }

                    }
                    MatchEventType.PENALTY_MISSED -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))

                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.penaltyEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Penalty taker:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }

                    }
                    MatchEventType.INJURY -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))

                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.injuryEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Injured player:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                    MatchEventType.THROW_IN -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))

                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.throwInEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Thrower:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }

                    }
                    MatchEventType.GOAL_KICK -> {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(screenWidth(x = 56.0)) // Slightly larger to allow the white box to be visible
                            ) {
                                // Red Circle (Main Player Icon)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(matchCommentaryData.mainPlayer!!.mainPhoto?.link)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(id = R.drawable.loading_img),
                                        error = painterResource(id = R.drawable.ic_broken_image),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Club logo",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))
                                    )
                                }

                                // White Circle (Number Indicator)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = matchCommentaryData.mainPlayer!!.number.toString(),
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            Text(
                                fontSize = screenFontSize(x = 14.0).sp,
                                text = matchCommentaryData.goalKickEvent!!.summary,
                                fontWeight = FontWeight.W400
                            )

                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Goalkeeper:",
                                fontWeight = FontWeight.Bold,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = matchCommentaryData.mainPlayer!!.username,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                    MatchEventType.KICK_OFF -> {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(matchCommentaryData.homeClub!!.clubLogo?.link)
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
                                    .data(matchCommentaryData.awayClub!!.clubLogo?.link)
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
                    MatchEventType.HALF_TIME -> {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(matchCommentaryData.homeClub!!.clubLogo?.link)
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
                                text = matchCommentaryData.halfTimeEvent?.homeClubScore.toString(),
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
                                text = matchCommentaryData.halfTimeEvent?.awayClubScore.toString(),
                                fontSize = screenFontSize(x = 16.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(matchCommentaryData.awayClub!!.clubLogo?.link)
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
                    MatchEventType.FULL_TIME -> {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(matchCommentaryData.homeClub!!.clubLogo?.link)
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
                                text = matchCommentaryData.fullTimeEvent?.homeClubScore.toString(),
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
                                text = matchCommentaryData.fullTimeEvent?.awayClubScore.toString(),
                                fontSize = screenFontSize(x = 16.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(matchCommentaryData.awayClub!!.clubLogo?.link)
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

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MatchTimelineScreenPreview() {
    LigiopenTheme {
        MatchTimelineScreen(
            matchStatus = MatchStatus.FIRST_HALF,
            fixtureId = null,
            matchCommentaries = matchCommentaries,
        )
    }
}