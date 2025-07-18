package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.stats

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun MatchStatisticsScreenComposable(
    matchStatus: MatchStatus,
    matchFixtureData: FixtureData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        MatchStatisticsScreen(
            matchStats = sampleStats,
            matchFixtureData = matchFixtureData
        )
    }
}

@Composable
fun MatchStatisticsScreen(
    matchStats: MatchStatistics,
    matchFixtureData: FixtureData,
    modifier: Modifier = Modifier
) {
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

        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))

        // Statistics rows
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            matchStats.statistics.forEach { stat ->
                MatchStatisticRow(
                    label = stat.label,
                    homeValue = stat.homeValue,
                    awayValue = stat.awayValue
                )
            }
        }
    }
}

@Composable
fun MatchStatisticRow(
    label: String,
    homeValue: String,
    awayValue: String,
    modifier: Modifier = Modifier
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
//            .background(Color.Yellow)
            .padding(
                vertical = screenHeight(x = 8.0),
                horizontal = screenWidth(x = 16.0)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home team value
        Text(
            text = homeValue,
            textAlign = TextAlign.Center,
//            modifier = Modifier.weight(1f),
            fontSize = screenFontSize(x = 16.0).sp,
            fontWeight = FontWeight.Bold
        )

        // Statistic label
        Text(
            text = label,
            textAlign = TextAlign.Center,
//            modifier = Modifier.weight(1f),
            fontSize = screenFontSize(x = 14.0).sp
        )

        // Away team value
        Text(
            text = awayValue,
            textAlign = TextAlign.Center,
//            modifier = Modifier.weight(1f),
            fontSize = screenFontSize(x = 16.0).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MatchStatisticsScreenPreview() {
    val sampleStats = MatchStatistics(
        homeTeamName = "ORL",
        awayTeamName = "ALT",
        statistics = listOf(
            MatchStatistic("Shots", "10", "12"),
            MatchStatistic("Shots on Target", "6", "7"),
            MatchStatistic("Possession", "55%", "45%"),
            MatchStatistic("Passing Accuracy", "82%", "79%"),
            MatchStatistic("Corners", "5", "4"),
            MatchStatistic("Fouls", "12", "15"),
            MatchStatistic("Offsides", "3", "2"),
            MatchStatistic("Yellow Cards", "2", "3"),
            MatchStatistic("Red Cards", "0", "1")
        )
    )

    LigiopenTheme {
        MatchStatisticsScreen(
            matchStats = sampleStats,
            matchFixtureData = fixture
        )
    }
}
