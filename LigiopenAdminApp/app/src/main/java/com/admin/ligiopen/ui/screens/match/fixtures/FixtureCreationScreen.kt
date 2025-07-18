package com.admin.ligiopen.ui.screens.match.fixtures

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.club
import com.admin.ligiopen.data.network.models.club.clubs
import com.admin.ligiopen.data.network.models.match.location.MatchLocationData
import com.admin.ligiopen.data.network.models.match.location.matchLocation
import com.admin.ligiopen.data.network.models.match.location.matchLocations
import com.admin.ligiopen.ui.nav.AppNavigation
import com.admin.ligiopen.ui.screens.news.newsManagement.SelectableClubCard
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.composables.OutlinedTextFieldComposable
import com.admin.ligiopen.utils.composables.ResultDialog
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object FixtureCreationScreenDestination: AppNavigation {
    override val title: String = "Fixture creation screen"
    override val route: String = "fixture-creation-screen"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FixtureCreationScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: FixtureCreationViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        ResultDialog(
            title = "Match fixture",
            body = "Match fixture has been created successfully",
            onDismiss = {
                viewModel.resetStatus()
                navigateToPreviousScreen()
            },
            onConfirm = {
                viewModel.resetStatus()
                navigateToPreviousScreen()
            },
            showDismissButton = false
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        FixtureCreationScreen(
            clubSearchText = uiState.clubSearchText,
            onChangeClubSearchText = viewModel::changeClubSearchText,
            home = uiState.home,
            away = uiState.away,
            onSelectHomeClub = viewModel::changeHomeClub,
            onRemoveHomeClub = viewModel::removeHomeClub,
            onSelectAwayClub = viewModel::changeAwayClub,
            onRemoveAwayClub = viewModel::removeAwayClub,
            clubs = uiState.clubs,
            locationSearchText = uiState.locationSearchText,
            onChangeLocationSearchText = viewModel::changeSearchLocationText,
            selectedLocation = uiState.selectedLocation,
            locations = uiState.locations,
            onSelectMatchLocation = viewModel::selectMatchLocation,
            onRemoveMatchLocation = viewModel::removeSelectedLocation,
            navigateToPreviousScreen = navigateToPreviousScreen,
            matchDate = uiState.matchDate,
            matchTime = uiState.matchTime,
            onDateSelected = viewModel::selectMatchDate,
            onTimeSelected = viewModel::selectMatchTime,
            loadingStatus = uiState.loadingStatus,
            buttonEnabled = uiState.buttonEnabled,
            onCreateFixture = viewModel::createMatchFixture
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixtureCreationScreen(
    clubSearchText: String,
    onChangeClubSearchText: (String) -> Unit,
    home: ClubData?,
    onSelectHomeClub: (club: ClubData) -> Unit,
    onRemoveHomeClub: () -> Unit,
    away: ClubData?,
    onSelectAwayClub: (club: ClubData) -> Unit,
    onRemoveAwayClub: () -> Unit,
    clubs: List<ClubData>,
    locationSearchText: String,
    onChangeLocationSearchText: (String) -> Unit,
    selectedLocation: MatchLocationData?,
    locations: List<MatchLocationData>,
    onSelectMatchLocation: (location: MatchLocationData) -> Unit,
    matchDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    matchTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    onRemoveMatchLocation: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    loadingStatus: LoadingStatus,
    buttonEnabled: Boolean,
    onCreateFixture: () -> Unit,
    modifier: Modifier = Modifier
) {

    var searchHomeClubExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var searchAwayClubExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var searchLocationExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var showDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    var showTimePicker by rememberSaveable {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(16.0),
                horizontal = screenWidth(16.0)
            )

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
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
                text = "Create fixture",
                fontSize = screenFontSize(16.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(8.0)))
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(screenWidth(16.0))
                    ) {
                        Text(
                            text = "Home club",
                            fontSize = screenFontSize(18.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(16.0)))
                if(home != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
//                    .background(color = Color.Transparent, shape = RoundedCornerShape(screenWidth(8.0)))
                                .border(
                                    width = screenWidth(1.0),
                                    shape = RoundedCornerShape(screenWidth(8.0)),
                                    color = Color.LightGray
                                )
                                .padding(screenWidth(16.0))
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = home.clubLogo.link,
                                    contentDescription = "Image of ${home.name}",
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(RoundedCornerShape(screenWidth(x = 8.0))),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(screenHeight(4.0)))
                                Text(
                                    text = home.name,
                                    fontSize = screenFontSize(12.0).sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(screenHeight(8.0)))
                        TextButton(
                            onClick = {
                                searchHomeClubExpanded = true
                            }
                        ) {
                            Text(
                                text = "Change club",
                                fontSize = screenFontSize(14.0).sp
                            )
                        }
                    }
                } else {
                    TextButton(
                        onClick = {
                            searchHomeClubExpanded = true
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Select club",
                            fontSize = screenFontSize(14.0).sp
                        )
                    }
                }
                if(searchHomeClubExpanded) {
                    OutlinedTextFieldComposable(
                        label = "Search home club",
                        value = clubSearchText,
                        onValueChange = onChangeClubSearchText,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(screenHeight(16.0)))
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                    ) {
                        clubs.forEach { club ->
                            SelectableClubCard(
                                club = club,
                                selectedClubIds = listOf(home?.clubId ?: 0, away?.clubId ?: 0),
                                onSelectClub = {
                                    val selectedClub = clubs.find { club -> club.clubId == it}
                                    onSelectHomeClub(selectedClub!!)
                                    searchHomeClubExpanded = false
                                },
                                onRemoveClub = {
                                    onRemoveHomeClub()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(screenHeight(16.0)))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(screenWidth(16.0))
                    ) {
                        Text(
                            text = "Away club",
                            fontSize = screenFontSize(18.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(screenHeight(16.0)))
                if(away != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
//                    .background(color = Color.Transparent, shape = RoundedCornerShape(screenWidth(8.0)))
                                .border(
                                    width = screenWidth(1.0),
                                    shape = RoundedCornerShape(screenWidth(8.0)),
                                    color = Color.LightGray
                                )
                                .padding(screenWidth(16.0))
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = away.clubLogo.link,
                                    contentDescription = "Image of ${away.name}",
                                    modifier = Modifier
                                        .size(screenWidth(x = 48.0))
                                        .clip(RoundedCornerShape(screenWidth(x = 8.0))),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(screenHeight(4.0)))
                                Text(
                                    text = away.name,
                                    fontSize = screenFontSize(12.0).sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(screenHeight(8.0)))
                        TextButton(
                            onClick = {
                                searchAwayClubExpanded = true
                            }
                        ) {
                            Text(
                                text = "Change club",
                                fontSize = screenFontSize(14.0).sp
                            )
                        }
                    }
                } else {
                    TextButton(
                        onClick = {
                            searchAwayClubExpanded = true
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Select club",
                            fontSize = screenFontSize(14.0).sp
                        )
                    }
                }
                if(searchAwayClubExpanded) {
                    OutlinedTextFieldComposable(
                        label = "Search away club",
                        value = clubSearchText,
                        onValueChange = onChangeClubSearchText,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(screenHeight(16.0)))
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                    ) {
                        clubs.forEach { club ->
                            SelectableClubCard(
                                club = club,
                                selectedClubIds = listOf(home?.clubId ?: 0, away?.clubId ?: 0),
                                onSelectClub = {
                                    val selectedClub = clubs.find { club -> club.clubId == it}
                                    onSelectAwayClub(selectedClub!!)
                                    searchAwayClubExpanded = true
                                },
                                onRemoveClub = {
                                    onRemoveAwayClub()
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(16.0)))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(screenWidth(16.0))
                    ) {
                        Text(
                            text = "Match location",
                            fontSize = screenFontSize(18.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(16.0)))
                if(selectedLocation != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
//                    .background(color = Color.Transparent, shape = RoundedCornerShape(screenWidth(8.0)))
                                .border(
                                    width = screenWidth(1.0),
                                    shape = RoundedCornerShape(screenWidth(8.0)),
                                    color = Color.LightGray
                                )
                                .padding(screenWidth(16.0))

                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if(selectedLocation.photos?.isNotEmpty() == true) {
                                    val photo = selectedLocation.photos[0].link
                                    AsyncImage(
                                        model = photo,
                                        contentDescription = "Image of ${selectedLocation.venueName}",
                                        modifier = Modifier
                                            .size(screenWidth(x = 48.0))
                                            .clip(RoundedCornerShape(screenWidth(x = 8.0))),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Spacer(modifier = Modifier.height(screenHeight(4.0)))
                                Text(
                                    text = selectedLocation.venueName,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = screenFontSize(12.0).sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(screenHeight(8.0)))
                        TextButton(
                            onClick = {
                                searchLocationExpanded = true
                            },
                            modifier = Modifier
                        ) {
                            Text(
                                text = "Change venue",
                                fontSize = screenFontSize(14.0).sp
                            )
                        }
                    }
                } else {
                    TextButton(
                        onClick = {
                            searchLocationExpanded = true
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Select venue (match location)",
                            fontSize = screenFontSize(14.0).sp
                        )
                    }
                }
                if(searchLocationExpanded) {
                    OutlinedTextFieldComposable(
                        label = "Search match venue",
                        value = locationSearchText,
                        onValueChange = onChangeLocationSearchText,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(screenHeight(8.0)))
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                    ) {
                        locations.forEach { location ->
                            SelectableLocationCard(
                                location = location,
                                onSelectMatchLocation = {
                                    onSelectMatchLocation(location)
                                    searchLocationExpanded = false
                                },
                            )
                            Spacer(modifier = Modifier.width(screenWidth(4.0)))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(16.0)))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(screenWidth(16.0))
                    ) {
                        Text(
                            text = "Match date & time",
                            fontSize = screenFontSize(18.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(screenHeight(8.0)))

                // Date selection
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Date:",
                        fontSize = screenFontSize(14.0).sp,
                        modifier = Modifier.width(screenWidth(80.0))
                    )

                    TextButton(
                        onClick = { showDatePicker = true }
                    ) {
                        Text(
                            text = matchDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "Select date",
                            fontSize = screenFontSize(14.0).sp
                        )
                    }
                }

                // Time selection
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Time:",
                        fontSize = screenFontSize(14.0).sp,
                        modifier = Modifier.width(screenWidth(80.0))
                    )

                    TextButton(
                        onClick = { showTimePicker = true }
                    ) {
                        Text(
                            text = matchTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Select time",
                            fontSize = screenFontSize(14.0).sp
                        )
                    }
                }

                if (showDatePicker) {
                    val currentDate = LocalDate.now()
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = matchDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
                        yearRange = currentDate.year..(currentDate.year + 1), // Only allow current and next year
                        selectableDates = object : SelectableDates {
                            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                return utcTimeMillis >= currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            }

                            override fun isSelectableYear(year: Int): Boolean {
                                return year >= currentDate.year
                            }
                        }
                    )

                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            Button(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    onDateSelected(
                                        Instant.ofEpochMilli(millis)
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate()
                                    )
                                }
                                showDatePicker = false
                            }) {
                                Text(
                                    text = "OK",
                                    fontSize = screenFontSize(14.0).sp
                                )
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDatePicker = false }) {
                                Text(
                                    text = "Cancel",
                                    fontSize = screenFontSize(14.0).sp
                                )
                            }
                        }
                    ) {
                        DatePicker(
                            state = datePickerState
                        )
                    }
                }

                if (showTimePicker) {
                    val timePickerState = rememberTimePickerState(
                        initialHour = matchTime?.hour ?: 12,
                        initialMinute = matchTime?.minute ?: 0,
                        is24Hour = true // Set to false if you want 12-hour format
                    )

                    AlertDialog(
                        onDismissRequest = { showTimePicker = false },
                        confirmButton = {
                            Button(onClick = {
                                onTimeSelected(LocalTime.of(timePickerState.hour, timePickerState.minute))
                                showTimePicker = false
                            }) {
                                Text(
                                    text = "OK",
                                    fontSize = screenFontSize(14.0).sp
                                )
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showTimePicker = false }) {
                                Text(
                                    text = "Cancel",
                                    fontSize = screenFontSize(14.0).sp
                                )
                            }
                        },
                        title = { Text(
                            text = "Select Time",
                            fontSize = screenFontSize(16.0).sp
                        ) },
                        text = {
                            TimePicker(
                                state = timePickerState
                            )
                        }
                    )
                }
            }
        }

        Button(
            enabled = buttonEnabled,
            onClick = onCreateFixture,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(loadingStatus == LoadingStatus.LOADING) {
                Text(
                    text = "Loading...",
                    fontSize = screenFontSize(14.0).sp
                )
            } else {
                Text(
                    text = "Create match fixture",
                    fontSize = screenFontSize(14.0).sp
                )
            }

        }

    }
}

@Composable
fun SelectableLocationCard(
    location: MatchLocationData,
    onSelectMatchLocation: (MatchLocationData) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = {
            onSelectMatchLocation(location)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            if(location.photos?.isNotEmpty() == true) {
                val photo = location.photos[0].link
                AsyncImage(
                    model = photo,
                    contentDescription = "Image of ${location.venueName}",
                    modifier = Modifier
                        .size(screenWidth(x = 48.0))
                        .clip(RoundedCornerShape(screenWidth(x = 8.0))),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Text(
                text = location.venueName,
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FixtureCreationScreenPreview() {
    LigiopenadminTheme {
        FixtureCreationScreen(
            home = club,
            away = club,
            clubSearchText = "",
            onChangeClubSearchText = {},
            onSelectHomeClub = {},
            onRemoveAwayClub = {},
            clubs = clubs,
            locationSearchText = "",
            onChangeLocationSearchText = {},
            selectedLocation = matchLocation,
            locations = matchLocations,
            onSelectMatchLocation = {},
            onRemoveMatchLocation = {},
            navigateToPreviousScreen = {},
            onRemoveHomeClub = {},
            onSelectAwayClub = {},
            matchDate = null,
            onDateSelected = {},
            matchTime = null,
            onTimeSelected = {},
            buttonEnabled = false,
            onCreateFixture = {},
            loadingStatus = LoadingStatus.INITIAL
        )
    }
}