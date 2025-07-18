package com.jabulani.ligiopen.ui.inapp.clubs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.jabulani.ligiopen.data.network.model.player.PlayerDetails
import com.jabulani.ligiopen.data.network.model.player.player
import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.ui.theme.backgroundLight
import com.jabulani.ligiopen.ui.theme.onSurfaceLight
import com.jabulani.ligiopen.ui.theme.onSurfaceVariantLight
import com.jabulani.ligiopen.ui.theme.onTertiaryLight
import com.jabulani.ligiopen.ui.theme.primaryLight
import com.jabulani.ligiopen.ui.theme.surfaceLight
import com.jabulani.ligiopen.ui.theme.surfaceVariantLight
import com.jabulani.ligiopen.ui.theme.tertiaryLight
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth

object PlayerDetailsScreenDestination: AppNavigation {
    override val title: String = "Player details screen"
    override val route: String = "player-details-screen"
    val playerId: String = "playerId"
    val routeWithPlayerId: String = "$route/{$playerId}"
}

@Composable
fun PlayerDetailsScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: PlayerDetailsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PlayerDetailsScreen(
            playerDetails = uiState.playerDetails,
            clubDetails = uiState.clubDetails,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }

}

@Composable
fun PlayerDetailsScreen(
    playerDetails: PlayerDetails,
    clubDetails: ClubDetails,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Using theme colors
    val primaryColor = primaryLight
    val accentColor = tertiaryLight
    val backgroundColor = backgroundLight
    val surfaceColor = surfaceLight
    val onSurfaceColor = onSurfaceLight
    val surfaceVariantColor = surfaceVariantLight

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState)
    ) {
        // Header with back button and player image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight(280.0))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryColor, primaryColor.copy(alpha = 0.7f))
                    )
                )
        ) {
            // Player image with parallax effect
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(playerDetails.mainPhoto?.link)
                    .crossfade(true)
                    .build(),
                contentDescription = "Player photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(220.dp)
                    .graphicsLayer {
                        translationY = -scrollState.value * 0.5f
                        alpha = 0.9f
                    }
            )

            // Dark overlay for better text visibility
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(80.0))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
                    .align(Alignment.TopCenter)
            )

            // Back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(screenWidth(16.0)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = navigateToPreviousScreen,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                        .size(screenWidth(40.0))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "PLAYER PROFILE",
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
                    color = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            // Player number badge
            Box(
                modifier = Modifier
                    .offset(x = screenWidth(24.0), y = screenHeight(-24.0))
                    .align(Alignment.BottomEnd)
                    .size(screenWidth(72.0))
                    .background(
                        color = accentColor,
                        shape = CircleShape
                    )
                    .border(
                        width = screenWidth(4.0),
                        color = Color.White,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = playerDetails.number.toString(),
                    color = onTertiaryLight,
                    fontSize = screenFontSize(28.0).sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Player info section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidth(16.0))
        ) {
            Spacer(modifier = Modifier.height(screenHeight(16.0)))

            // Name and club row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = playerDetails.username.uppercase(),
                        color = primaryColor,
                        fontSize = screenFontSize(24.0).sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = screenFontSize(0.5).sp
                    )

                    Text(
                        text = playerDetails.playerPosition.name
                            .lowercase()
                            .replaceFirstChar { it.uppercase() },
                        color = accentColor,
                        fontSize = screenFontSize(16.0).sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Club logo
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(clubDetails.clubLogo.link)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Club logo",
                    modifier = Modifier
                        .size(screenWidth(48.0))
                )
            }

            Spacer(modifier = Modifier.height(screenHeight(8.0)))

            // Club name
            Text(
                text = clubDetails.name,
                color = onSurfaceVariantLight,
                fontSize = screenFontSize(14.0).sp,
                modifier = Modifier.padding(bottom = screenHeight(16.0))
            )

            // Stats cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(screenWidth(8.0))
            ) {
                // Age card
                StatCard(
                    title = "AGE",
                    value = playerDetails.age.toString(),
                    icon = R.drawable.person,
                    containerColor = surfaceVariantColor,
                    contentColor = onSurfaceVariantLight,
                    modifier = Modifier.weight(1f)
                )

                // Height card
                StatCard(
                    title = "HEIGHT",
                    value = "${playerDetails.height} cm",
                    icon = R.drawable.height,
                    containerColor = surfaceVariantColor,
                    contentColor = onSurfaceVariantLight,
                    modifier = Modifier.weight(1f)
                )

                // Weight card
                StatCard(
                    title = "WEIGHT",
                    value = "${playerDetails.weight} kg",
                    icon = R.drawable.weight,
                    containerColor = surfaceVariantColor,
                    contentColor = onSurfaceVariantLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(screenHeight(16.0)))

            // Country card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(screenHeight(4.0)),
                colors = CardDefaults.cardColors(
                    containerColor = surfaceColor,
                    contentColor = onSurfaceColor
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(screenWidth(16.0)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "Country",
                        tint = accentColor,
                        modifier = Modifier.size(screenWidth(24.0))
                    )

                    Spacer(modifier = Modifier.width(screenWidth(16.0)))

                    Column {
                        Text(
                            text = "NATIONALITY",
                            color = onSurfaceVariantLight,
                            fontSize = screenFontSize(12.0).sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.kenya_flag),
                                contentDescription = "Country flag",
                                modifier = Modifier.size(screenWidth(24.0))
                            )

                            Spacer(modifier = Modifier.width(screenWidth(8.0)))

                            Text(
                                text = playerDetails.country,
                                color = onSurfaceColor,
                                fontSize = screenFontSize(16.0).sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(screenHeight(24.0)))

            Spacer(modifier = Modifier.height(screenHeight(32.0)))
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: Int,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(screenHeight(4.0)),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier.padding(screenWidth(12.0)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(screenWidth(20.0))
            )

            Spacer(modifier = Modifier.height(screenHeight(4.0)))

            Text(
                text = value,
                color = contentColor,
                fontSize = screenFontSize(18.0).sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = title,
                color = contentColor.copy(alpha = 0.8f),
                fontSize = screenFontSize(12.0).sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlayerDetailsScreenPreview(
    modifier: Modifier = Modifier
) {
    LigiopenTheme {
        PlayerDetailsScreen(
            playerDetails = player,
            clubDetails = club,
            navigateToPreviousScreen = {}
        )
    }
}