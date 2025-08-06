package com.admin.ligiopen.ui.screens.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.R
import com.admin.ligiopen.data.network.models.player.PlayerData
import com.admin.ligiopen.data.network.models.player.PlayerPosition
import com.admin.ligiopen.data.network.models.player.PlayerState
import com.admin.ligiopen.ui.nav.AppNavigation
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth

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

    PlayerDetailsScreen(
        playerData = uiState.player,
        onNavigateBack = navigateToPreviousScreen,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDetailsScreen(
    playerData: PlayerData,
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = playerData.username,
                        fontSize = screenFontSize(20.0).sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.padding(start = screenWidth(8.0))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = screenWidth(16.0)),
                verticalArrangement = Arrangement.spacedBy(screenHeight(24.0))
            ) {
//                item { Spacer(modifier = Modifier.height(screenHeight(8.0))) }

                // Player Header Section
                item {
                    PlayerHeaderSection(playerData = playerData)
                }

                // Player Information Section
                item {
                    PlayerInformationSection(playerData = playerData)
                }

                // Player Stats Section
                item {
                    PlayerStatsSection(playerData = playerData)
                }

                // Player Photos Section
                if (playerData.files.isNotEmpty()) {
                    item {
                        PlayerPhotosSection(playerData = playerData)
                    }
                }

                item { Spacer(modifier = Modifier.height(screenHeight(32.0))) }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerHeaderSection(
    playerData: PlayerData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight(280.0))
            .shadow(
                elevation = screenHeight(8.0),
                shape = RoundedCornerShape(screenWidth(16.0)),
                clip = true
            )
    ) {
        // Background with gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                        ),
                        startY = 300f
                    )
                )
        )

        // Content overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenWidth(24.0)),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Player Avatar
            Box(
                modifier = Modifier
                    .size(screenWidth(120.0))
                    .shadow(screenWidth(8.0), CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = screenWidth(2.0),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (playerData.mainPhoto != null) {
                    AsyncImage(
                        model = playerData.mainPhoto.link,
                        contentDescription = "Player photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(screenWidth(48.0))
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeight(16.0)))

            Text(
                text = playerData.username,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeight(8.0)))

            Text(
                text = "#${playerData.number} • ${playerData.playerPosition.name.replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = screenHeight(4.0))
            )

            Spacer(modifier = Modifier.height(screenHeight(16.0)))

            // Status chip
            StatusChip(
                status = playerData.playerState.name,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun PlayerInformationSection(
    playerData: PlayerData,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = screenHeight(2.0),
                shape = RoundedCornerShape(screenWidth(16.0))
            ),
        shape = RoundedCornerShape(screenWidth(16.0)),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(screenWidth(20.0))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = screenHeight(16.0))
            ) {
                Text(
                    text = "Player Information",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Age
            InfoRowWithEdit(
                icon = R.drawable.cake,
                label = "Age",
                value = "${playerData.age} years",
                onEditClick = { /* Handle edit age */ }
            )

            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = screenHeight(8.0))
            )

            // Height
            InfoRowWithEdit(
                icon = R.drawable.height,
                label = "Height",
                value = "${playerData.height} m",
                onEditClick = { /* Handle edit height */ }
            )

            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = screenHeight(8.0))
            )

            // Weight
            InfoRowWithEdit(
                icon = R.drawable.weight,
                label = "Weight",
                value = playerData.weight?.let { "$it kg" } ?: "Not specified",
                onEditClick = { /* Handle edit weight */ }
            )

            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = screenHeight(8.0))
            )

            // Location
            InfoRowWithEdit(
                icon = R.drawable.location,
                label = "Location",
                value = buildString {
                    append(playerData.country)
                    if (!playerData.county.isNullOrBlank()) {
                        append(", ${playerData.county}")
                    }
                    if (!playerData.town.isNullOrBlank()) {
                        append(", ${playerData.town}")
                    }
                },
                onEditClick = { /* Handle edit location */ }
            )
        }
    }
}

@Composable
fun PlayerStatsSection(
    playerData: PlayerData,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = screenHeight(2.0),
                shape = RoundedCornerShape(screenWidth(16.0))
            ),
        shape = RoundedCornerShape(screenWidth(16.0)),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(screenWidth(20.0))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = screenHeight(16.0))
            ) {
                Text(
                    text = "Player Stats",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Position
            InfoRowWithEdit(
                icon = R.drawable.team,
                label = "Position",
                value = playerData.playerPosition.name.replaceFirstChar { it.uppercase() },
                onEditClick = { /* Handle edit position */ }
            )

            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = screenHeight(8.0))
            )

            // Jersey Number
            InfoRowWithEdit(
                icon = R.drawable.number,
                label = "Jersey Number",
                value = "#${playerData.number}",
                onEditClick = { /* Handle edit number */ }
            )

            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = screenHeight(8.0))
            )

            // Status
            InfoRowWithEdit(
                icon = R.drawable.radio,
                label = "Status",
                value = playerData.playerState.name.replaceFirstChar { it.uppercase() },
                onEditClick = { /* Handle edit status */ }
            )
        }
    }
}

@Composable
fun PlayerPhotosSection(
    playerData: PlayerData,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = screenHeight(2.0),
                shape = RoundedCornerShape(screenWidth(16.0))
            ),
        shape = RoundedCornerShape(screenWidth(16.0)),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(screenWidth(20.0))
        ) {
            Text(
                text = "Player Photos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = screenHeight(16.0))
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(screenWidth(16.0)),
                contentPadding = PaddingValues(bottom = screenHeight(8.0))
            ) {
                items(playerData.files) { file ->
                    Card(
                        modifier = Modifier
                            .width(screenWidth(120.0))
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(screenWidth(12.0))
                    ) {
                        AsyncImage(
                            model = file.link,
                            contentDescription = "Player photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRowWithEdit(
    icon: Int,
    label: String,
    value: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = screenHeight(8.0)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(screenWidth(24.0))
        )
        Spacer(modifier = Modifier.width(screenWidth(16.0)))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = screenHeight(2.0))
            )
        }

        IconButton(
            onClick = onEditClick,
            modifier = Modifier.size(screenWidth(24.0))
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit $label",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (status.uppercase()) {
        "ACTIVE" -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        "INACTIVE" -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        "PENDING" -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(screenWidth(16.0)),
        modifier = modifier
    ) {
        Text(
            text = status.replaceFirstChar { it.uppercase() },
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = screenWidth(12.0), vertical = screenHeight(6.0))
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlayerDetailsScreenPreview() {
    val playerData = PlayerData(
        playerId = 1,
        mainPhoto = null,
        username = "John Doe",
        number = 10,
        playerPosition = PlayerPosition.MIDFIELDER,
        age = 25,
        height = 1.85,
        weight = 75.0,
        country = "United States",
        county = "California",
        town = "Los Angeles",
        clubId = 1,
        files = emptyList(),
        playerState = PlayerState.ACTIVE
    )

    LigiopenadminTheme {
        PlayerDetailsScreen(
            playerData = playerData,
        )
    }
}