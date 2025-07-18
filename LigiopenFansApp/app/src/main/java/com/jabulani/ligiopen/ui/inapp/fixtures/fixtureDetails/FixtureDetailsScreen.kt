package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth

object FixtureDetailsScreenDestination : AppNavigation {
    override val title: String = "Fixture details screen"
    override val route: String = "fixture-details-screen"
}

@Composable
fun FixtureDetailsScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = navigateToPreviousScreen)
    ElevatedCard(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .safeDrawingPadding()
        ) {
            FixtureDetailsScreen(
                navigateToPreviousScreen = navigateToPreviousScreen
            )
        }
    }
}

@Composable
fun FixtureDetailsScreen(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()

    ) {
        ElevatedCard(
            shape = RoundedCornerShape(0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = navigateToPreviousScreen) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous screen"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Buy ticket",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
//        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.football),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .padding(
                        vertical = screenHeight(x = 16.0),
                        horizontal = screenWidth(x = 16.0)
                    )
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "OveralClub FC vs Obunga FC",
                    fontSize = screenFontSize(x = 16.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "Aug. 20, 2024 11:40am",
                    fontWeight = FontWeight.W300,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "Kisumu stadium",
                    fontWeight = FontWeight.W300,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Add to calendar",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Share event",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "Select ticket type",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 16.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ElevatedCard(
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            text = "General Admission",
                            fontSize = screenFontSize(x = 14.0).sp,
                            modifier = Modifier
                                .padding(
                                    vertical = screenWidth(x = 16.0),
                                    horizontal = screenWidth(x = 18.0)
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    ElevatedCard(
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            text = "VIP",
                            fontSize = screenFontSize(x = 14.0).sp,
                            modifier = Modifier
                                .padding(
                                    vertical = screenWidth(x = 16.0),
                                    horizontal = screenWidth(x = 18.0)
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = R.drawable.ticket),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        color = MaterialTheme.colorScheme.onBackground,
                        text = "1 ticket - 100 KSh",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "Payment method",
                    fontSize = screenFontSize(x = 16.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ElevatedCard(
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            text = "M-PESA",
                            fontSize = screenFontSize(x = 14.0).sp,
                            modifier = Modifier
                                .padding(
                                    vertical = screenWidth(x = 16.0),
                                    horizontal = screenWidth(x = 18.0)
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    ElevatedCard(
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            text = "Card",
                            fontSize = screenFontSize(x = 14.0).sp,
                            modifier = Modifier
                                .padding(
                                    vertical = screenWidth(x = 16.0),
                                    horizontal = screenWidth(x = 18.0)
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Buy ticket",
                        fontSize = screenFontSize(x = 14.0).sp,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FixtureDetailsScreenPreview() {
    LigiopenTheme {
        FixtureDetailsScreen(
            navigateToPreviousScreen = {}
        )
    }

}