package com.admin.ligiopen.ui.screens.match.clubs

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.R
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.club
import com.admin.ligiopen.data.network.models.player.PlayerData
import com.admin.ligiopen.data.network.models.player.PlayerPosition
import com.admin.ligiopen.ui.nav.AppNavigation
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.composables.PhotoSelection
import com.admin.ligiopen.utils.composables.PhotosSelection
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth
import java.text.SimpleDateFormat
import java.util.Locale

object ClubDetailsScreenDestination : AppNavigation {
    override val title: String = "Club Details Screen"
    override val route: String = "club-details-screen"
    val clubId: String = "clubId"
    val routeWithClubId: String = "$route/{$clubId}"
}

@Composable
fun ClubDetailsScreenComposable(
    navigateToClubUpdateScreen: (clubId: String) -> Unit,
    navigateToPlayerDetailsScreen: (playerId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: ClubDetailsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    var showLogoPicker by rememberSaveable {
        mutableStateOf(false)
    }

    var showPhotoPicker by rememberSaveable {
        mutableStateOf(false)
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
                viewModel.getInitialData()
            }
        }
    }

    if (uiState.loadingStatus == LoadingStatus.SUCCESS) {
        Toast.makeText(context, "Club updated", Toast.LENGTH_SHORT).show()
        showLogoPicker = false
        showPhotoPicker = false
        viewModel.resetStatus()
    }

    if(showLogoPicker) {
        AlertDialog(
            title = {
                Text(
                    text = "Change club logo",
                    fontSize = screenFontSize(16.0).sp
                )
            },
            text = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    PhotoSelection(
                        onUploadPhoto = viewModel::uploadLogo,
                        onRemovePhoto = viewModel::removeLogo,
                        photo = uiState.newLogo
                    )
                }
            },
            onDismissRequest = {
                if(uiState.loadingStatus != LoadingStatus.LOADING) {
                    showLogoPicker = !showLogoPicker
                }
            },
            dismissButton = {
                TextButton(
                    enabled = uiState.loadingStatus != LoadingStatus.LOADING,
                    onClick = { showLogoPicker = !showLogoPicker }
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = screenFontSize(14.0).sp
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = uiState.loadingStatus != LoadingStatus.LOADING && uiState.newLogo != null,
                    onClick = {
                        viewModel.setClubLogo(context)
                    }
                ) {

                    if(uiState.loadingStatus == LoadingStatus.LOADING) {
                        Text(
                            text = "Loading...",
                            fontSize = screenFontSize(14.0).sp
                        )
                    } else {
                        Text(
                            text = "Upload",
                            fontSize = screenFontSize(14.0).sp
                        )
                    }


                }
            }
        )
    }

    if(showPhotoPicker) {
        AlertDialog(
            title = {
                Text(
                    text = "Change club photo",
                    fontSize = screenFontSize(16.0).sp
                )
            },
            text = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    PhotoSelection(
                        onUploadPhoto = viewModel::uploadClubPhoto,
                        onRemovePhoto = viewModel::removeClubPhoto,
                        photo = uiState.newPhoto
                    )
                }
            },
            onDismissRequest = {
                if(uiState.loadingStatus != LoadingStatus.LOADING) {
                    showPhotoPicker = !showPhotoPicker
                }
            },
            dismissButton = {
                TextButton(
                    enabled = uiState.loadingStatus != LoadingStatus.LOADING,
                    onClick = { showPhotoPicker = !showPhotoPicker }
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = screenFontSize(14.0).sp
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = uiState.loadingStatus != LoadingStatus.LOADING && uiState.newPhoto != null,
                    onClick = {
                        viewModel.setClubPhoto(context)
                    }
                ) {

                    if(uiState.loadingStatus == LoadingStatus.LOADING) {
                        Text(
                            text = "Loading...",
                            fontSize = screenFontSize(14.0).sp
                        )
                    } else {
                        Text(
                            text = "Upload",
                            fontSize = screenFontSize(14.0).sp
                        )
                    }


                }
            }
        )
    }

    ClubDetailsScreen(
        clubData = uiState.club,
        loadingStatus = uiState.loadingStatus,
        onNavigateBack = navigateToPreviousScreen,
        onStatusChange = viewModel::changeClubStatus,
        onChangeLogo = {
            showLogoPicker = true
        },
        onChangeClubPhoto = {
            showPhotoPicker = true
        },
        navigateToClubUpdateScreen = navigateToClubUpdateScreen,
        navigateToPlayerDetailsScreen = navigateToPlayerDetailsScreen,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ClubDetailsScreen(
    clubData: ClubData,
    loadingStatus: LoadingStatus,
    onNavigateBack: () -> Unit = {},
    onStatusChange: (String) -> Unit = {},
    onChangeLogo: () -> Unit,
    onChangeClubPhoto: () -> Unit,
    navigateToClubUpdateScreen: (clubId: String) -> Unit,
    navigateToPlayerDetailsScreen: (playerId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = clubData.name,
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
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = screenWidth(16.0))
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(screenHeight(24.0))
                ) {
                    item { Spacer(modifier = Modifier.height(screenHeight(8.0))) }

                    // Club Header Section
                    item {
                        ClubHeaderSection(
                            clubData = clubData,
                            onChangeLogo = onChangeLogo,
                            onChangeClubPhoto = onChangeClubPhoto
                        )
                    }

                    // Status Management Section
                    item {
                        StatusManagementSection(
                            currentStatus = clubData.clubStatus,
                            loadingStatus = loadingStatus,
                            onStatusChange = { newStatus ->
                                onStatusChange(newStatus)
                            }
                        )
                    }

                    // Club Information Section
                    item {
                        ClubInformationSection(clubData = clubData)
                    }

                    // Players Section
                    item {
                        PlayersSection(
                            players = clubData.players,
                            navigateToPlayerDetailsScreen = navigateToPlayerDetailsScreen
                        )
                    }

                    // Club Photos Section
                    if (clubData.clubMainPhoto != null) {
                        item {
                            ClubPhotosSection(clubData = clubData)
                        }
                    }

                    item { Spacer(modifier = Modifier.height(screenHeight(32.0))) }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = screenWidth(16.0),
                            vertical = screenHeight(8.0)
                        )
                ) {
                    Button(
                        onClick = {
                            navigateToClubUpdateScreen(clubData.clubId.toString())
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Edit club details"
                        )
                    }
                    Spacer(modifier = Modifier.width(screenWidth(4.0)))
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        onClick = {},
//                        modifier = Modifier
//                            .weight(1f)
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onError,
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete club"
                        )
                    }

                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClubHeaderSection(
    clubData: ClubData,
    onChangeLogo: () -> Unit,
    onChangeClubPhoto: () -> Unit,
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
        // Background image with gradient overlay
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
        ) {
            if (clubData.clubMainPhoto != null) {
                AsyncImage(
                    model = clubData.clubMainPhoto.link,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(
                        x = screenWidth((-20.0)),
                        y = screenHeight(20.0)
                    )
            ) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = onChangeClubPhoto
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "Edit club photo"
                    )
                }
            }
        }

        // Content overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenWidth(24.0)),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Club Logo
            Box(
                modifier = Modifier
                    .size(screenWidth(100.0))
                    .shadow(screenWidth(8.0), CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .graphicsLayer { clip = false }
                    .border(
                        width = screenWidth(2.0),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = clubData.clubLogo.link,
                    contentDescription = "Club logo",
                    modifier = Modifier.size(screenWidth(90.0)),
                    contentScale = ContentScale.Crop
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
//                        .alpha(0.7f)
                        .offset(
                            x = screenWidth(-10.0),
                            y = screenHeight(-10.0)
                        )
//                        .background(MaterialTheme.colorScheme.primary)
                        .zIndex(2f)
                ) {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = onChangeLogo
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onPrimary,
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Edit logo"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(screenHeight(16.0)))

            Text(
                text = clubData.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            if (!clubData.clubAbbreviation.isNullOrBlank()) {
                Text(
                    text = clubData.clubAbbreviation,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = screenHeight(4.0))
                )
            }

            Spacer(modifier = Modifier.height(screenHeight(8.0)))

            if (!clubData.description.isNullOrBlank()) {
                Text(
                    text = clubData.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = screenFontSize(20.0).sp,
                    modifier = Modifier.padding(horizontal = screenWidth(16.0))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusManagementSection(
    currentStatus: String,
    loadingStatus: LoadingStatus,
    onStatusChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val statusOptions = listOf("PENDING", "APPROVED", "REJECTED")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = screenHeight(2.0), shape = RoundedCornerShape(screenWidth(16.0))),
        shape = RoundedCornerShape(screenWidth(16.0)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(screenWidth(20.0))
        ) {
            Column {
                Text(
                    text = "Club Status",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(screenHeight(12.0)))

                // Current Status Display
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = screenHeight(16.0))
                ) {
                    Text(
                        text = "Current:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(screenWidth(8.0)))
                    StatusChip(status = currentStatus)
                }

                Divider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    modifier = Modifier.padding(bottom = screenHeight(16.0))
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Change Status",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = screenHeight(12.0))
                    )
                    Spacer(modifier = Modifier.width(screenWidth(4.0)))
                    if (loadingStatus == LoadingStatus.LOADING) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(screenWidth(20.0)),
                            strokeWidth = screenWidth(2.0),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                }

                // Segmented Radio Button Control
                Surface(
                    shape = RoundedCornerShape(screenWidth(8.0)),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = screenWidth(1.0),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(screenWidth(8.0))
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        statusOptions.forEach { status ->
                            val isSelected = currentStatus == status
                            val backgroundColor = if (isSelected) {
                                when (status) {
                                    "APPROVED" -> MaterialTheme.colorScheme.tertiaryContainer
                                    "REJECTED" -> MaterialTheme.colorScheme.errorContainer
                                    else -> MaterialTheme.colorScheme.primaryContainer
                                }
                            } else {
                                Color.Transparent
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(screenHeight(48.0))
                                    .background(
                                        color = backgroundColor,
                                        shape = when (status) {
                                            statusOptions.first() -> RoundedCornerShape(
                                                topStart = screenHeight(8.0),
                                                bottomStart = screenHeight(8.0)
                                            )

                                            statusOptions.last() -> RoundedCornerShape(
                                                topEnd = screenHeight(8.0),
                                                bottomEnd = screenHeight(8.0)
                                            )

                                            else -> RoundedCornerShape(0.dp)
                                        }
                                    )
                                    .clickable { onStatusChange(status) },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .padding(horizontal = screenWidth(12.0))
                                        .horizontalScroll(rememberScrollState())
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = when (status) {
                                                "PENDING" -> R.drawable.clock
                                                "APPROVED" -> R.drawable.check_mark
                                                else -> R.drawable.close
                                            }
                                        ),
                                        contentDescription = null,
                                        tint = if (isSelected) {
                                            when (status) {
                                                "APPROVED" -> MaterialTheme.colorScheme.onTertiaryContainer
                                                "REJECTED" -> MaterialTheme.colorScheme.onErrorContainer
                                                else -> MaterialTheme.colorScheme.onPrimaryContainer
                                            }
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                        modifier = Modifier.size(screenWidth(20.0))
                                    )
                                    Spacer(modifier = Modifier.width(screenWidth(8.0)))
                                    Text(
                                        text = status,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) {
                                            when (status) {
                                                "APPROVED" -> MaterialTheme.colorScheme.onTertiaryContainer
                                                "REJECTED" -> MaterialTheme.colorScheme.onErrorContainer
                                                else -> MaterialTheme.colorScheme.onPrimaryContainer
                                            }
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun StatusButton(
    status: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: Int,
    color: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) color else Color.Transparent
    val borderColor = if (isSelected) Color.Transparent else color.copy(alpha = 0.5f)

    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = textColor
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = if (isSelected) 0.dp else screenWidth(1.0),
//            color = borderColor
        ),
        modifier = modifier.height(screenHeight(40.0))
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(screenWidth(18.0))
        )
        Spacer(modifier = Modifier.width(screenWidth(8.0)))
        Text(
            text = status,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (status.uppercase()) {
        "APPROVED" -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        "REJECTED" -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        "PENDING" -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(screenWidth(16.0)),
        modifier = modifier
    ) {
        Text(
            text = status,
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = screenWidth(12.0), vertical = screenHeight(6.0))
        )
    }
}

@Composable
fun ClubInformationSection(
    clubData: ClubData,
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
                text = "Club Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = screenHeight(12.0))
            )

            InfoRow(
                icon = R.drawable.location,
                label = "Location",
                value = buildString {
                    append(clubData.country)
                    if (!clubData.county.isNullOrBlank()) {
                        append(", ${clubData.county}")
                    }
                    if (!clubData.town.isNullOrBlank()) {
                        append(", ${clubData.town}")
                    }
                }
            )

            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = screenHeight(8.0))
            )

            InfoRow(
                icon = R.drawable.calendar,
                label = "Founded",
                value = formatDate(clubData.startedOn)
            )

            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = screenHeight(8.0))
            )

            InfoRow(
                icon = R.drawable.group,
                label = "Total Players",
                value = "${clubData.players.size}"
            )

            if (clubData.archived) {
                Divider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = screenHeight(8.0))
                )
                InfoRow(
                    icon = R.drawable.delete,
                    label = "Archived",
                    value = formatDate(clubData.archivedAt ?: "")
                )
            }
        }
    }
}

@Composable
fun PlayersSection(
    players: List<PlayerData>,
    navigateToPlayerDetailsScreen: (playerId: String) -> Unit,
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
                Icon(
                    painter = painterResource(id = R.drawable.group),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(screenWidth(24.0))
                )
                Spacer(modifier = Modifier.width(screenWidth(12.0)))
                Text(
                    text = "Players (${players.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (players.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight(120.0)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.circle),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(screenWidth(48.0))
                        )
                        Spacer(modifier = Modifier.height(screenHeight(8.0)))
                        Text(
                            text = "No players registered",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // Player position summary
                val positionCounts = players.groupBy { it.playerPosition }
                    .mapValues { it.value.size }

                Text(
                    text = "By Position",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = screenHeight(12.0))
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = screenHeight(20.0))
                ) {
                    PlayerPosition.values().forEach { position ->
                        val count = positionCounts[position] ?: 0
                        if (count > 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = screenHeight(6.0)),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = position.name.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$count",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Players horizontal list
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(screenWidth(16.0)),
                    contentPadding = PaddingValues(bottom = screenHeight(8.0))
                ) {
                    items(players) { player ->
                        PlayerCard(
                            player = player,
                            navigateToPlayerDetailsScreen = navigateToPlayerDetailsScreen
                        )
                    }

//                    if (players.size > 5) {
//                        item {
//                            PlayerMoreCard(count = players.size - 5)
//                        }
//                    }
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(8.0)))
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add player",
                    fontSize = screenFontSize(14.0).sp
                )
            }
            Spacer(modifier = Modifier.height(screenHeight(8.0)))
        }
    }
}

@Composable
fun PlayerCard(
    player: PlayerData,
    navigateToPlayerDetailsScreen: (playerId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(screenWidth(140.0))
            .height(screenHeight(180.0))
            .clickable {
                navigateToPlayerDetailsScreen(player.playerId.toString())
            },
        shape = RoundedCornerShape(screenWidth(12.0)),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = screenHeight(4.0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenWidth(16.0)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Player avatar
            Box(
                modifier = Modifier
                    .size(screenWidth(80.0))
                    .shadow(screenWidth(4.0), CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = screenWidth(1.0),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (player.mainPhoto != null) {
                    AsyncImage(
                        model = player.mainPhoto.link,
                        contentDescription = "Player photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(screenWidth(36.0))
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeight(16.0)))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = player.username,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(screenHeight(4.0)))

                Text(
                    text = "#${player.number} • ${player.playerPosition.name.lowercase()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PlayerMoreCard(
    count: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(screenWidth(140.0))
            .height(screenHeight(180.0)),
        shape = RoundedCornerShape(screenWidth(12.0)),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = screenHeight(4.0)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(screenWidth(64.0))
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+$count",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(16.0)))
                Text(
                    text = "More Players",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ClubPhotosSection(
    clubData: ClubData,
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
                text = "Club Photos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = screenHeight(16.0))
            )

            clubData.clubMainPhoto?.let { mainPhoto ->
                Surface(
                    shape = RoundedCornerShape(screenWidth(12.0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                ) {
                    AsyncImage(
                        model = mainPhoto.link,
                        contentDescription = "Club photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: Int,
    label: String,
    value: String,
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
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: return dateString)
    } catch (e: Exception) {
        dateString
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubDetailsScreenPreview() {
    LigiopenadminTheme {
        ClubDetailsScreen(
            loadingStatus = LoadingStatus.INITIAL,
            clubData = club,
            onChangeLogo = {},
            onChangeClubPhoto = {},
            navigateToClubUpdateScreen = {},
            navigateToPlayerDetailsScreen = {}
        )
    }
}