package com.jabulani.ligiopen.ui.inapp.clubs

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.jabulani.ligiopen.data.network.model.club.ClubDivisionDt
import com.jabulani.ligiopen.data.network.model.club.clubs
import com.jabulani.ligiopen.data.network.model.club.divisions
import com.jabulani.ligiopen.data.network.model.file.FileData
import com.jabulani.ligiopen.ui.inapp.home.HomeScreenTab
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.ui.theme.backgroundLight
import com.jabulani.ligiopen.ui.theme.onPrimaryLight
import com.jabulani.ligiopen.ui.theme.onSurfaceLight
import com.jabulani.ligiopen.ui.theme.onTertiaryLight
import com.jabulani.ligiopen.ui.theme.primaryLight
import com.jabulani.ligiopen.ui.theme.surfaceContainerHighLight
import com.jabulani.ligiopen.ui.theme.surfaceLight
import com.jabulani.ligiopen.ui.theme.surfaceVariantLight
import com.jabulani.ligiopen.ui.theme.tertiaryLight
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth

@Composable
fun ClubsScreenComposable(
    switchToHomeTab: () -> Unit,
    navigateToClubDetailsScreen: (clubId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ClubsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        ClubsScreen(
            searchQuery = uiState.clubSearchText,
            onChangeSearchQuery = viewModel::changeClubName,
            selectedDivision = uiState.selectedDivision,
            onChangeSelectedDivision = viewModel::changeClubDivision,
            onApplyFilters = viewModel::onApplyFilters,
            onClearFilters = {},
            clubs = uiState.clubs,
            loadingStatus = uiState.loadingStatus,
            navigateToClubDetailsScreen = navigateToClubDetailsScreen,
            divisions = uiState.divisions,
            onToggleFavorite = {
                viewModel.bookmarkClub(it.clubId)
            },
            onToggleShowFavorites = viewModel::changeFavorite,
            showOnlyFavorites = uiState.favorite
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubsScreen(
    searchQuery: String,
    onChangeSearchQuery: (String) -> Unit,
    selectedDivision: ClubDivisionDt?,
    onChangeSelectedDivision: (ClubDivisionDt?) -> Unit,
    clubs: List<ClubDetails>,
    navigateToClubDetailsScreen: (clubId: String) -> Unit,
    loadingStatus: LoadingStatus,
    divisions: List<ClubDivisionDt> = emptyList(),
    onApplyFilters: () -> Unit,
    onClearFilters: () -> Unit,
    onToggleFavorite: (ClubDetails) -> Unit,
    showOnlyFavorites: Boolean,
    onToggleShowFavorites: () -> Unit,
    modifier: Modifier = Modifier
) {

    var showFilters by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    // Theme colors
    val primaryColor = primaryLight
    val accentColor = tertiaryLight
    val onSurfaceColor = onSurfaceLight
    val errorColor = MaterialTheme.colorScheme.error


    // Bottom sheet content
    if (showFilters) {
        ModalBottomSheet(
            onDismissRequest = { showFilters = false },
            sheetState = bottomSheetState,
            containerColor = surfaceContainerHighLight,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidth(16.0), vertical = screenHeight(16.0))
            ) {
                Text(
                    text = "Filter Clubs",
                    style = MaterialTheme.typography.headlineSmall,
                    color = onSurfaceColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = screenHeight(16.0))
                )

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onChangeSearchQuery,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search clubs...") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search"
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onChangeSearchQuery("") }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.close),
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(screenWidth(12.0)),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = onSurfaceColor.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(screenHeight(16.0)))

                // Favorite filter toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleShowFavorites() }
                        .padding(vertical = screenHeight(12.0)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (showOnlyFavorites) R.drawable.star_filled
                            else R.drawable.star_unfilled2
                        ),
                        contentDescription = "Favorite",
                        tint = if (showOnlyFavorites) accentColor else onSurfaceColor.copy(alpha = 0.7f),
                        modifier = Modifier.size(screenWidth(24.0))
                    )
                    Spacer(modifier = Modifier.width(screenWidth(12.0)))
                    Text(
                        text = "My Favorite Clubs",
                        color = if (showOnlyFavorites) onSurfaceColor
                        else onSurfaceColor.copy(alpha = 0.7f),
                        fontWeight = if (showOnlyFavorites) FontWeight.Bold else FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(screenHeight(8.0)))

                // Division Filter Dropdown
                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(screenWidth(12.0)),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (expanded) accentColor else onSurfaceColor.copy(alpha = 0.5f)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = surfaceLight
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.league),
                                contentDescription = "League",
                                modifier = Modifier.size(screenWidth(20.0))
                            )
                            Spacer(modifier = Modifier.width(screenWidth(8.0)))
                            Text(
                                text = selectedDivision?.name ?: "Filter by league",
                                color = if (selectedDivision == null) onSurfaceColor.copy(alpha = 0.7f) else onSurfaceColor,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (expanded) "Collapse" else "Expand"
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(surfaceLight)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("All leagues", fontWeight = if (selectedDivision == null) FontWeight.Bold else FontWeight.Normal)
                            },
                            onClick = {
                                onChangeSelectedDivision(null)
                                expanded = false
                            }
                        )
                        HorizontalDivider()
                        divisions.forEach { division ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        division.name,
                                        fontWeight = if (selectedDivision?.id == division.id) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    onChangeSelectedDivision(division)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(screenHeight(24.0)))

                // Apply button
                Button(
                    onClick = {
                        showFilters = false
                        onApplyFilters()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight(48.0)),
                    shape = RoundedCornerShape(screenWidth(12.0)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = onTertiaryLight
                    )
                ) {
                    Text("Apply Filters", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        // Header
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
                text = HomeScreenTab.CLUBS.name
                    .lowercase()
                    .replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineMedium,
                color = onPrimaryLight,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .shadow(screenWidth(4.0), shape = RoundedCornerShape(screenWidth(4.0)))
            )

            Spacer(modifier = Modifier.weight(1f))

            // Filter button
            IconButton(
                onClick = { showFilters = true },
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
        }

        // Active filters chips
        if (selectedDivision != null || searchQuery.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceVariantLight)
                    .padding(horizontal = screenWidth(16.0), vertical = screenHeight(8.0))
            ) {
                Text(
                    text = "Filters:",
                    color = onSurfaceColor.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.width(screenWidth(8.0)))

                if (searchQuery.isNotEmpty()) {
                    FilterChip(
                        selected = true,
                        onClick = {onChangeSearchQuery("")},
                        label = { Text("Name: $searchQuery") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                modifier = Modifier.size(screenWidth(14.0))
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = accentColor.copy(alpha = 0.2f),
                            selectedLabelColor = onSurfaceColor
                        ),
                        modifier = Modifier.padding(end = screenWidth(4.0))
                    )
                }

                if (selectedDivision != null) {
                    FilterChip(
                        selected = true,
                        onClick = {onChangeSelectedDivision(null)},
                        label = { Text("League: ${selectedDivision?.name}") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                modifier = Modifier.size(screenWidth(14.0))
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = accentColor.copy(alpha = 0.2f),
                            selectedLabelColor = onSurfaceColor
                        )
                    )
                }
            }
        }


        // Main content
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
            LoadingStatus.FAIL -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(screenWidth(24.0))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.error),
                            contentDescription = "Error",
                            tint = errorColor,
                            modifier = Modifier.size(screenWidth(64.0))
                        )
                        Spacer(modifier = Modifier.height(screenHeight(16.0)))
                        Text(
                            text = "Failed to load clubs",
                            style = MaterialTheme.typography.titleLarge,
                            color = errorColor
                        )
                    }
                }
            }
            LoadingStatus.SUCCESS -> {
                if (clubs.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(screenWidth(24.0))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.no_results),
                                contentDescription = "No results",
                                tint = onSurfaceColor.copy(alpha = 0.5f),
                                modifier = Modifier.size(screenWidth(64.0))
                            )
                            Spacer(modifier = Modifier.height(screenHeight(16.0)))
                            Text(
                                text = "No clubs found",
                                style = MaterialTheme.typography.titleLarge,
                                color = onSurfaceColor.copy(alpha = 0.7f)
                            )
                            if (searchQuery.isNotEmpty() || selectedDivision != null) {
                                Text(
                                    text = "Try adjusting your filters",
                                    color = onSurfaceColor.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(screenWidth(16.0)),
                        verticalArrangement = Arrangement.spacedBy(screenHeight(16.0)),
                        horizontalArrangement = Arrangement.spacedBy(screenWidth(16.0))
                    ) {
                        items(clubs) { club ->
                            ClubCard(
                                club = club,
                                onClick = { navigateToClubDetailsScreen(club.clubId.toString()) },
                                onToggleFavorite = {
                                    onToggleFavorite(club)
                                }
                            )
                        }
                    }
                }
            }
            else -> {
                if (clubs.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(screenWidth(24.0))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.no_results),
                                contentDescription = "No results",
                                tint = onSurfaceColor.copy(alpha = 0.5f),
                                modifier = Modifier.size(screenWidth(64.0))
                            )
                            Spacer(modifier = Modifier.height(screenHeight(16.0)))
                            Text(
                                text = "No clubs found",
                                style = MaterialTheme.typography.titleLarge,
                                color = onSurfaceColor.copy(alpha = 0.7f)
                            )
                            if (searchQuery.isNotEmpty() || selectedDivision != null) {
                                Text(
                                    text = "Try adjusting your filters",
                                    color = onSurfaceColor.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(screenWidth(16.0)),
                        verticalArrangement = Arrangement.spacedBy(screenHeight(16.0)),
                        horizontalArrangement = Arrangement.spacedBy(screenWidth(16.0))
                    ) {
                        items(clubs) { club ->
                            ClubCard(
                                club = club,
                                onClick = { navigateToClubDetailsScreen(club.clubId.toString()) },
                                onToggleFavorite = {
                                    onToggleFavorite(club)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClubCard(
    club: ClubDetails,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(screenWidth(16.0)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = screenHeight(8.0),
            pressedElevation = screenHeight(4.0)
        ),
        colors = CardDefaults.cardColors(
            containerColor = surfaceContainerHighLight
        )
    ) {
        Column {
            // Club banner image with overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
            ) {

                // Favorite button at top-left
                IconButton(
                    onClick = {
                        onToggleFavorite()
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(screenWidth(8.0))
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (club.favorite) R.drawable.star_filled
                            else R.drawable.star_unfilled2
                        ),
                        contentDescription = if (club.favorite) "Remove from favorites"
                        else "Add to favorites",
                        tint = if (club.favorite) tertiaryLight
                        else surfaceContainerHighLight,
                        modifier = Modifier.size(screenWidth(24.0))
                    )
                }
                // Club photo
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(club.clubMainPhoto?.link)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Club banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    primaryLight.copy(alpha = 0.7f)
                                ),
                                startY = 0.6f
                            )
                        )
                )

                // Club logo badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(screenWidth(12.0))
                        .size(screenWidth(48.0))
                        .background(
                            color = surfaceContainerHighLight,
                            shape = CircleShape
                        )
                        .border(
                            width = screenWidth(2.0),
                            color = tertiaryLight,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(club.clubLogo.link)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Club logo",
                        modifier = Modifier.size(screenWidth(36.0))
                    )
                }

                // League badge at top-right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(screenWidth(8.0))
                ) {
                    Surface(
                        color = tertiaryLight.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(screenWidth(8.0)),
//                        shadowElevation = screenHeight(2.0))
                    ) {
                        Text(
                            text = club.division.name,
                            color = onTertiaryLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(
                                horizontal = screenWidth(8.0),
                                vertical = screenHeight(4.0))
                        )
                    }

                }
            }

            // Club info
            Column(
                modifier = Modifier.padding(screenWidth(12.0))
            ) {
                Text(
                    text = club.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = onSurfaceLight,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(screenHeight(4.0)))

                // Location info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "Location",
                        tint = onSurfaceLight.copy(alpha = 0.7f),
                        modifier = Modifier.size(screenWidth(14.0))
                    )
                    Spacer(modifier = Modifier.width(screenWidth(4.0)))
                    Text(
                        text = club.town ?: club.county ?: club.country,
                        color = onSurfaceLight.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Glowing accent at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(4.0))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(tertiaryLight, primaryLight)
                        )
                    )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubsScreenPreview() {
    LigiopenTheme {
        ClubsScreen(
            searchQuery = "",
            onChangeSearchQuery = {},
            selectedDivision = null,
            onChangeSelectedDivision = {},
            clubs = clubs,
            loadingStatus = LoadingStatus.SUCCESS,
            navigateToClubDetailsScreen = {},
            onApplyFilters = {},
            onClearFilters = {},
            onToggleFavorite = {},
            divisions = divisions,
            onToggleShowFavorites = {},
            showOnlyFavorites = false
        )
    }
}