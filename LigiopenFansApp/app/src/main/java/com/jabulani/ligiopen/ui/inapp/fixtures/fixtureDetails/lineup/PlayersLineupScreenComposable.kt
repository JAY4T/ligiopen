package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.lineup

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.data.network.model.match.fixture.MatchStatus
import com.jabulani.ligiopen.data.network.model.match.fixture.fixture
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth

@Composable
fun PlayersLineupScreenComposable(
    matchStatus: MatchStatus,
    playersInLineup: List<PlayerInLineup>,
    matchFixtureData: FixtureData,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        PlayersLineupScreen(
            matchStatus = matchStatus,
            playersInLineup = playersInLineup,
            matchFixtureData = matchFixtureData
        )
    }
}

@Composable
fun PlayersLineupScreen(
    matchStatus: MatchStatus,
    playersInLineup: List<PlayerInLineup>,
    matchFixtureData: FixtureData,
    modifier: Modifier = Modifier
) {
    // Separate players by position
    val groupedPlayers = playersInLineup.groupBy { it.position }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = screenWidth(x = 16.0),
                top = 0.dp,
                end = screenWidth(x = 16.0),
                bottom = screenHeight(x = 16.0),
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = matchFixtureData.homeClub.clubAbbreviation ?: "${matchFixtureData.homeClub.name.take(3)} FC",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 18.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(matchFixtureData.homeClub.clubLogo.link)
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
                    .data(matchFixtureData.awayClub.clubLogo.link)
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
                text = matchFixtureData.awayClub.clubAbbreviation ?: "${matchFixtureData.awayClub.name.take(3)} FC",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 18.0).sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
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
                        text = "Match lineups not available",
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
                    .verticalScroll(rememberScrollState())
            ) {
                // Iterate through positions and display players side by side
                groupedPlayers.forEach { (position, players) ->
                    val homeTeamPlayers = players.filter { it.home }
                    val awayTeamPlayers = players.filter { !it.home }

                    Text(
                        text = position.name.lowercase().replaceFirstChar { it.uppercase() },
                        fontWeight = FontWeight.Bold,
                        fontSize = screenFontSize(x = 14.0).sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = screenHeight(x = 8.0))
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenHeight(x = 4.0)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Display home team players
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = screenWidth(x = 8.0))
                        ) {
                            homeTeamPlayers.forEach { player ->
                                PlayerLineupCell(playerInLineup = player)
                            }
                        }

                        // Display away team players
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = screenWidth(x = 8.0))
                        ) {
                            awayTeamPlayers.forEach { player ->
                                PlayerLineupCell(playerInLineup = player)
                            }
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun PlayerLineupCell(
    playerInLineup: PlayerInLineup,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenHeight(x = 4.0))
            .background(if (playerInLineup.bench) Color.Gray else Color.Transparent)
    ) {
        Text(
            text = "${playerInLineup.number}. ${playerInLineup.name}",
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = screenWidth(x = 8.0))
        )

        if (playerInLineup.yellowCard) {
            Text(
                text = "🟨",
                modifier = Modifier.padding(horizontal = screenWidth(x = 4.0))
            )
        }

        if (playerInLineup.redCard) {
            Text(
                text = "🟥",
                modifier = Modifier.padding(horizontal = screenWidth(x = 4.0))
            )
        }

        if (playerInLineup.scored) {
            Text(
                text = "⚽",
                modifier = Modifier.padding(horizontal = screenWidth(x = 4.0))
            )
        }

        if(playerInLineup.substituted) {
            Text(
                text = "🔄",
                modifier = Modifier.padding(horizontal = screenWidth(x = 4.0))
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlayersLineupScreenPreview() {
    LigiopenTheme {
        PlayersLineupScreen(
            matchStatus = MatchStatus.COMPLETED,
            matchFixtureData = fixture,
            playersInLineup = playersInlineup
        )
    }
}