package com.admin.ligiopen.ui.screens.match.clubs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.R
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.clubs
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth
import kotlinx.coroutines.launch

// Enum for club status


@Composable
fun ClubsScreenComposable(
    navigateToLoginScreenWithArgs: (email: String, password: String) -> Unit,
    navigateToClubAdditionScreen: () -> Unit,
    navigateToClubDetailsScreen: (clubId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: ClubsViewModel = viewModel(factory = AppViewModelFactory.Factory)
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

    if(uiState.loadingStatus == LoadingStatus.FAIL) {
        if(uiState.unauthorized) {
            val email = uiState.userAccount.email
            val password = uiState.userAccount.password

            navigateToLoginScreenWithArgs(email, password)
        }
        viewModel.resetStatus()
    }

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        ClubsScreen(
            clubs = uiState.clubs,
            changeTab = viewModel::changeTab,
            loadingStatus = uiState.loadingStatus,
            navigateToClubDetailsScreen = navigateToClubDetailsScreen,
            navigateToClubAdditionScreen = navigateToClubAdditionScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClubsScreen(
    clubs: List<ClubData>,
    loadingStatus: LoadingStatus,
    changeTab: (tab: ClubStatus) -> Unit,
    navigateToClubAdditionScreen: () -> Unit,
    navigateToClubDetailsScreen: (clubId: String) -> Unit,
    modifier: Modifier = Modifier
) {
//    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCounty by remember { mutableStateOf("Nairobi") }

//    val tabTitles = listOf("Approved", "Pending", "Rejected")
//    val clubStatuses = listOf(ClubStatus.PENDING, ClubStatus.APPROVED, ClubStatus.REJECTED)

    val tabTitles = listOf("Approved", "Pending", "Rejected")
    val pagerState = rememberPagerState(pageCount = {tabTitles.size})

    LaunchedEffect(pagerState.currentPage) {
        val selectedTab = tabTitles[pagerState.currentPage]
        changeTab(ClubStatus.valueOf(selectedTab.replace(" ", "_").uppercase()))
    }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToClubAdditionScreen) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new club"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .padding(
                        horizontal = screenWidth(16.0),
//                        vertical = screenHeight(16.0)
                    )
            ) {
                IconButton(
                    onClick = { showFilterDialog = true },
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter),
                        contentDescription = "Filter clubs"
                    )
                }
                // ScrollableTabRow for club statuses
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = Color.Transparent,
                    edgePadding = screenWidth(16.0),
                    contentColor = MaterialTheme.colorScheme.primary,
                    divider = {},
                    indicator = {tabPositions ->
                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                .fillMaxWidth()
                                .height(screenHeight(3.0))
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(screenWidth(4.0))
                                )
                        )
                    },
//                modifier = Modifier.fillMaxWidth()
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            modifier = Modifier
                                .padding(horizontal = screenWidth(8.0)),
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelLarge,
                                    maxLines = 1,
                                    color = if (pagerState.currentPage == index) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(screenHeight(16.0)))

                HorizontalPager(
                    state = pagerState,
//                    modifier = Modifier.weight(1f)
                ) { page ->
//                val filteredOutcomes = outcomes.filter {
//                    it.status.equals(outcomeCategories[page].lowercase(), ignoreCase = true) &&
//                            (searchQuery.isEmpty() ||
//                                    it.client_name.contains(searchQuery, ignoreCase = true) ||
//                                    it.client_nrc.contains(searchQuery, ignoreCase = true) ||
//                                    it.phone_number.contains(searchQuery, ignoreCase = true))
//                }

                    if(loadingStatus == LoadingStatus.LOADING) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        if(clubs.isEmpty()) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text(
                                    text = "No ${tabTitles[pagerState.currentPage].lowercase()} clubs"
                                )
                            }
                        } else {
                            LazyColumn {
                                items(clubs) { club ->
                                    ClubCard(
                                        club = club,
                                        navigateToClubDetailsScreen = navigateToClubDetailsScreen
                                    )
                                    Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                                }
                            }
                        }

                    }

                }

            }


        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        FilterDialog(
            searchQuery = searchQuery,
            selectedCounty = selectedCounty,
            onSearchQueryChange = { searchQuery = it },
            onCountyChange = { selectedCounty = it },
            onDismiss = { showFilterDialog = false },
            onApply = { showFilterDialog = false }
        )
    }
}

@Composable
fun FilterDialog(
    searchQuery: String,
    selectedCounty: String,
    onSearchQueryChange: (String) -> Unit,
    onCountyChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    var showCountyDropdown by remember { mutableStateOf(false) }
    val counties = listOf("Nairobi", "Mombasa", "Kisumu", "Nakuru", "Eldoret") // Add more counties as needed

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Filter Clubs",
                fontSize = screenFontSize(x = 18.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Search field
                TextField(
                    label = {
                        Text(
                            text = "Club name",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = null
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = searchQuery,
                    shape = RoundedCornerShape(screenWidth(x = 10.0)),
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))

                // County selector
                Text(
                    text = "County",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Card(
                    onClick = { showCountyDropdown = !showCountyDropdown }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(screenWidth(x = 12.0))
                    ) {
                        Text(
                            text = selectedCounty,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                }

                // Simple county selection (you might want to implement a proper dropdown)
                if (showCountyDropdown) {
                    Column {
                        counties.forEach { county ->
                            TextButton(
                                onClick = {
                                    onCountyChange(county)
                                    showCountyDropdown = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = county,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onApply) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ClubCard(
    club: ClubData,
    navigateToClubDetailsScreen: (clubId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateToClubDetailsScreen(club.clubId.toString())
            }
    ) {
        AsyncImage(
            model = club.clubLogo!!.link,
            contentDescription = "Image of ${club.name}",
            modifier = Modifier
                .size(screenWidth(x = 72.0))
                .clip(RoundedCornerShape(screenWidth(x = 8.0))),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
        Column {
            Text(
                text = club.name,
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
            Text(
                text = club.country,
                fontSize = screenFontSize(x = 14.0).sp,
            )
            Text(
                text = "${club.county}, ${club.town}",
                fontSize = screenFontSize(x = 14.0).sp,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { navigateToClubDetailsScreen(club.clubId.toString()) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubsScreenPreview() {
    LigiopenadminTheme {
        ClubsScreen(
            clubs = clubs,
            changeTab = {},
            loadingStatus = LoadingStatus.INITIAL,
            navigateToClubDetailsScreen = {},
            navigateToClubAdditionScreen = {}
        )
    }
}