package com.admin.ligiopen.ui.screens.users

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.clubs
import com.admin.ligiopen.ui.nav.AppNavigation
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.TextFieldComposable
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth

object ClubSearchScreenDestination: AppNavigation {
    override val title: String = "Club search screen"
    override val route: String = "club-search-screen"
    val userId: String = "userId"
    val routeWithUserId: String = "$route/{$userId}"
}

@Composable
fun ClubSearchScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: ClubSearchViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

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

    var showSetAdminTypeDialog by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(uiState.setStatus) {
        if(uiState.setStatus == SetStatus.SUCCESS) {
            showSetAdminTypeDialog = false
            Toast.makeText(context, "Team admin set", Toast.LENGTH_LONG).show()
            navigateToPreviousScreen()
            viewModel.resetStatus()
        } else if(uiState.setStatus == SetStatus.FAILURE) {
            Toast.makeText(context, "Failed, please try again later", Toast.LENGTH_SHORT).show()
            viewModel.resetStatus()
        }
    }

    if(showSetAdminTypeDialog) {
        AlertDialog(
            title = {
                Text(
                    text = "Set admin for ${uiState.selectedClubName} club",
                    fontSize = screenFontSize(16.0).sp
                )
            },
            text = {
                Text(
                    text = "Confirm ${uiState.user.username} to be Club admin for ${uiState.selectedClubName}",
                    fontSize = screenFontSize(14.0).sp
                )
            },
            confirmButton = {
                Button(
                    enabled = uiState.setStatus != SetStatus.LOADING,
                    onClick = {
                        viewModel.setAdmin()
                    }
                ) {
                    if(uiState.setStatus == SetStatus.LOADING) {
                        Text(
                            text = "Loading...",
                            fontSize = screenFontSize(14.0).sp
                        )
                    } else {
                        Text(
                            text = "Confirm",
                            fontSize = screenFontSize(14.0).sp
                        )
                    }
                }
            },
            onDismissRequest = {
//                showSetAdminTypeDialog = false
            },
            dismissButton = {
                TextButton(
                    enabled = uiState.setStatus != SetStatus.LOADING,
                    onClick = {
                        showSetAdminTypeDialog = false
                    }
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = screenFontSize(14.0).sp
                    )
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        ClubSearchScreen(
            userId = uiState.user.id,
            clubs = uiState.clubs,
            clubName = uiState.clubName,
            selectedClubId = uiState.selectedClubId,
            onChangeClubName = viewModel::changeClubName,
            onContinue = {
                showSetAdminTypeDialog = true
            },
            onSelectClub = { clubId, clubName ->
                viewModel.selectClub(clubId, clubName)
            },
            navigateToPreviousScreen = navigateToPreviousScreen,
            modifier = modifier
        )
    }
}

@Composable
fun ClubSearchScreen(
    userId: Int,
    clubs: List<ClubData>,
    clubName: String,
    selectedClubId: Int,
    onChangeClubName: (name: String) -> Unit,
    onContinue: () -> Unit,
    onSelectClub: (clubId: Int, clubName: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(16.0),
                horizontal = screenWidth(16.0)
            )
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = navigateToPreviousScreen
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(4.0)))
            Text(
                text = "Select club",
                fontSize = screenFontSize(16.0).sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldComposable(
            label = "Search club",
            value = clubName,
            onValueChange = onChangeClubName,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Available Clubs",
            fontSize = screenFontSize(14.0).sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(clubs) { club ->
                val clubAdminsIds = club.admins.map { it.id }
                if (!clubAdminsIds.contains(userId)) {          
                    ClubItem(
                        club = club,
                        isSelected = club.clubId == selectedClubId,
                        onSelect = { onSelectClub(club.clubId, club.name) },
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onContinue,
            enabled = selectedClubId != -1,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Continue",
                fontSize = screenFontSize(14.0).sp
            )
        }
    }
}

@Composable
private fun ClubItem(
    club: ClubData,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onSelect)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.surface
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                Text(
                    text = club.name,
                    fontSize = screenFontSize(14.0).sp,
                    fontWeight = FontWeight.Medium
                )
                club.clubAbbreviation?.let { abbreviation ->
                    Text(
                        text = "Abbreviation: $abbreviation",
                        fontSize = screenFontSize(12.0).sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = "Location: ${club.town ?: club.county ?: club.country}",
                    fontSize = screenFontSize(12.0).sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Checkbox(
                checked = isSelected,
                onCheckedChange = { if (it) onSelect() }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubSearchScreenPreview() {
    LigiopenadminTheme {
        ClubSearchScreen(
            userId = 1,
            clubs = clubs,
            clubName = "Test Club",
            selectedClubId = 1,
            onChangeClubName = {},
            onContinue = {},
            onSelectClub = { _, _ -> },
            navigateToPreviousScreen = {}
        )
    }
}