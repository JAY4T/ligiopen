package com.jabulani.ligiopen.ui.inapp.home

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.data.network.model.match.fixture.fixtures
import com.jabulani.ligiopen.data.network.model.news.NewsDto
import com.jabulani.ligiopen.data.network.model.news.news
import com.jabulani.ligiopen.ui.inapp.clubs.ClubsScreenComposable
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus
import com.jabulani.ligiopen.ui.inapp.fixtures.FixturesScreenComposable
import com.jabulani.ligiopen.ui.inapp.news.NewsScreenComposable
import com.jabulani.ligiopen.ui.inapp.profile.ProfileScreenComposable
import com.jabulani.ligiopen.ui.inapp.standings.StandingsScreenComposable
import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenWidth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object MainScreenDestination : AppNavigation {
    override val title: String = "Home screen"
    override val route: String = "home-screen"

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreenComposable(
    onSwitchTheme: () -> Unit,
    navigateToNewsDetailsScreen: (newsId: String) -> Unit,
    navigateToClubDetailsScreen: (clubId: String) -> Unit,
    navigateToFixtureDetailsScreen: () -> Unit,
    navigateToHighlightsScreen: () -> Unit,
    navigateToLoginScreenWithArgs: (email: String, password: String) -> Unit,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    navigateToAllVideosScreen: () -> Unit,
    navigateToSingleVideoScreen: (videoId: String, videoTitle: String, videoDate: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: MainScreenViewModel = viewModel(factory = AppViewModelFactory.Factory)
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



    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    val tabs = listOf(
        HomeScreenTabItem(
            name = "Home",
            icon = R.drawable.home2,
            tab = HomeScreenTab.HOME
        ),
        HomeScreenTabItem(
            name = "Matches",
            icon = R.drawable.score2,
            tab = HomeScreenTab.MATCHES
        ),
        HomeScreenTabItem(
            name = "Clubs",
            icon = R.drawable.football_club,
            tab = HomeScreenTab.CLUBS
        ),
        HomeScreenTabItem(
            name = "News",
            icon = R.drawable.newspaper,
            tab = HomeScreenTab.NEWS
        ),
//        HomeScreenTabItem(
//            name = "Standings",
//            icon = R.drawable.league_2,
//            tab = HomeScreenTab.STANDINGS
//        ),
    )

    var currentTab by rememberSaveable {
        mutableStateOf(HomeScreenTab.HOME)
    }

    BackHandler(onBack = {
        if(currentTab != HomeScreenTab.HOME) {
            currentTab = HomeScreenTab.HOME
        } else {
            (context as Activity?)?.finish()
        }
    })

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        MainScreen(
            darkMode = uiState.userAccount.darkMode,
            onSwitchTheme = {
                scope.launch {
                    onSwitchTheme()
                    delay(1000)
                    drawerState.close()
                    Toast.makeText(context, "Theme switched", Toast.LENGTH_SHORT).show()
                }
            },
            username = uiState.userAccount.username,
            scope = scope,
            drawerState = drawerState,
            currentTab = currentTab,
            onChangeTab = {
                currentTab = it
            },
            tabs = tabs,
            fixtures = uiState.fixtures,
            news = uiState.news,
            navigateToNewsDetailsScreen = navigateToNewsDetailsScreen,
            navigateToClubDetailsScreen = navigateToClubDetailsScreen,
            navigateToFixtureDetailsScreen = navigateToFixtureDetailsScreen,
            navigateToHighlightsScreen = navigateToHighlightsScreen,
            navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
            navigateToPostMatchScreen = navigateToPostMatchScreen,
            navigateToAllVideosScreen = navigateToAllVideosScreen,
            navigateToSingleVideoScreen = navigateToSingleVideoScreen
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    username: String,
    darkMode: Boolean,
    onSwitchTheme: () -> Unit,
    scope: CoroutineScope?,
    drawerState: DrawerState?,
    currentTab: HomeScreenTab,
    onChangeTab: (tab: HomeScreenTab) -> Unit,
    tabs: List<HomeScreenTabItem>,
    fixtures: List<FixtureData>,
    news: List<NewsDto>,
    navigateToNewsDetailsScreen: (newsId: String) -> Unit,
    navigateToClubDetailsScreen: (clubId: String) -> Unit,
    navigateToFixtureDetailsScreen: () -> Unit,
    navigateToHighlightsScreen: () -> Unit,
    navigateToLoginScreenWithArgs: (email: String, password: String) -> Unit,
    navigateToPostMatchScreen: (postMatchId: String, fixtureId: String, locationId: String) -> Unit,
    navigateToAllVideosScreen: () -> Unit,
    navigateToSingleVideoScreen: (videoId: String, videoTitle: String, videoDate: String) -> Unit,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {

    Scaffold(
        bottomBar = {
            SportyBottomNavigationBar(
                tabs = tabs,
                currentTab = currentTab,
                onChangeTab = onChangeTab
            )
        },
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when(currentTab) {
                    HomeScreenTab.NEWS -> NewsScreenComposable(
                        addTopPadding = true,
                        navigateToNewsDetailsScreen = navigateToNewsDetailsScreen,
                        modifier = Modifier

                    )
                    HomeScreenTab.SCORES -> {}
                    HomeScreenTab.CLUBS -> ClubsScreenComposable(
                        switchToHomeTab = {
                            onChangeTab(HomeScreenTab.NEWS)
                        },
                        navigateToClubDetailsScreen = navigateToClubDetailsScreen,
                        modifier = Modifier

                    )
                    HomeScreenTab.MATCHES -> {
                        FixturesScreenComposable(
                            navigateToPostMatchScreen = navigateToPostMatchScreen,
                            navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
                            modifier = Modifier
                        )
                    }
                    HomeScreenTab.FINANCING -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()

                        ) {
                            Text(text = "Financing")
                        }
                    }
                    HomeScreenTab.PROFILE -> {
                        ProfileScreenComposable(
                            navigateToHomeScreen = {
                                onChangeTab(HomeScreenTab.NEWS)
                            },
                            navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
                            modifier = Modifier

                        )
                    }

                    HomeScreenTab.HOME -> {
                        HomeScreenComposable(
                            fixtures = fixtures,
                            news = news,
                            navigateToPostMatchScreen = navigateToPostMatchScreen,
                            navigateToSingleVideoScreen = navigateToSingleVideoScreen,
                            navigateToAllVideosScreen = navigateToAllVideosScreen,
                            navigateToNewsDetailsScreen = navigateToNewsDetailsScreen
                        )
                    }
                    HomeScreenTab.STANDINGS -> {
                        StandingsScreenComposable()
                    }
                }
            }
        }
    }
}

@Composable
fun SportyBottomNavigationBar(
    tabs: List<HomeScreenTabItem>,
    currentTab: HomeScreenTab,
    onChangeTab: (tab: HomeScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    // Use colors that match your header
    val primaryColor = MaterialTheme.colorScheme.primary
    val accentColor = MaterialTheme.colorScheme.secondary
    val surfaceColor = MaterialTheme.colorScheme.surface

    // Enhanced colors for better visibility
    val unselectedColor = Color.White.copy(alpha = 0.85f) // More visible unselected items
    val selectedIconColor = surfaceColor
    val selectedTextColor = Color.White

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.9f), // Slightly more opaque for better contrast
                            primaryColor.copy(alpha = 0.6f)
                        )
                    )
                )
                .height(72.dp)
        ) {
            // Subtle sporty pattern
            Canvas(modifier = Modifier.matchParentSize()) {
                val patternSize = 40.dp.toPx()
                val patternColor = Color.White.copy(alpha = 0.08f) // More subtle white pattern

                // Draw diagonal lines pattern
                for (i in -size.height.toInt() until size.width.toInt() step patternSize.toInt()) {
                    drawLine(
                        color = patternColor,
                        start = Offset(i.toFloat(), 0f),
                        end = Offset(i.toFloat() + size.height, size.height),
                        strokeWidth = 1.5.dp.toPx()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    SportyNavItem(
                        tab = tab,
                        isSelected = currentTab == tab.tab,
                        primaryColor = primaryColor,
                        accentColor = accentColor,
                        surfaceColor = surfaceColor,
                        unselectedColor = unselectedColor,
                        selectedIconColor = selectedIconColor,
                        selectedTextColor = selectedTextColor,
                        onClick = { onChangeTab(tab.tab) }
                    )
                }
            }
        }
    }
}

@Composable
fun SportyNavItem(
    tab: HomeScreenTabItem,
    isSelected: Boolean,
    primaryColor: Color,
    accentColor: Color,
    surfaceColor: Color,
    unselectedColor: Color,
    selectedIconColor: Color,
    selectedTextColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val animatedElevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 0.dp,
        animationSpec = tween(300),
        label = "elevation"
    )

    // Color animation for smooth transitions
    val animatedIconColor by animateColorAsState(
        targetValue = if (isSelected) selectedIconColor else unselectedColor,
        animationSpec = tween(300),
        label = "iconColor"
    )

    val animatedTextColor by animateColorAsState(
        targetValue = if (isSelected) selectedTextColor else unselectedColor,
        animationSpec = tween(300),
        label = "textColor"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                indication = rememberRipple(
                    bounded = false,
                    radius = 24.dp,
                    color = Color.White.copy(alpha = 0.3f)
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .scale(animatedScale)
    ) {
        Card(
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(animatedElevation),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) {
                    accentColor
                } else {
                    Color.White.copy(alpha = 0.15f) // Subtle background for unselected
                }
            ),
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = tab.icon),
                    contentDescription = tab.name,
                    tint = animatedIconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = tab.name,
            color = animatedTextColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Active indicator with animation
        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
            exit = scaleOut(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
        ) {
            Column {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .width(16.dp)
                        .height(3.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(50)
                        )
                        .clip(RoundedCornerShape(50))
                )
            }
        }
    }
}

@Composable
fun MainBottomNavBar(
    selectedTab: HomeScreenTab,
    tabs: List<HomeScreenTabItem>,
    onChangeTab: (tab: HomeScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar {
        for(tab in tabs) {
            NavigationBarItem(
                label = {
                    Text(
                        text = tab.name,
                        fontSize = screenFontSize(x = 10.0).sp
                    )
                },
                selected = tab.tab == selectedTab,
                onClick = {
                    onChangeTab(tab.tab)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = tab.icon),
                        contentDescription = tab.name
                    )
                }
            )
        }
    }
}

@Composable
fun ThemeSwitcher(
    darkTheme: Boolean = false,
    size: Dp = screenWidth(x = 150.0),
    iconSize: Dp = size / 3,
    padding: Dp = screenWidth(x = 10.0),
    borderWidth: Dp = screenWidth(x = 1.0),
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val offset by animateDpAsState(
        targetValue = if (darkTheme) 0.dp else size,
        animationSpec = animationSpec
    )

    Box(modifier = modifier
        .width(size * 2)
        .height(size)
        .clip(shape = parentShape)
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    tint = if (darkTheme) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary,
                    painter = painterResource(id = R.drawable.dark_mode),
                    contentDescription = "Theme icon",
                    modifier = Modifier.size(iconSize),
                )
            }
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    tint = if (darkTheme) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer,
                    painter = painterResource(id = R.drawable.light_mode),
                    contentDescription = "Theme icon",
                    modifier = Modifier.size(iconSize),
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    LigiopenTheme {
        MainScreen(
            darkMode = false,
            onSwitchTheme = {},
            username = "Sam N",
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
            currentTab = HomeScreenTab.NEWS,
            onChangeTab = {},
            tabs = listOf(),
            fixtures = fixtures,
            news = news,
            scope = null,
            navigateToNewsDetailsScreen = {},
            navigateToClubDetailsScreen = {},
            navigateToFixtureDetailsScreen = {},
            navigateToHighlightsScreen = {},
            navigateToLoginScreenWithArgs = {email, password ->},
            navigateToPostMatchScreen = {postMatchId, fixtureId, locationId ->},
            navigateToSingleVideoScreen = {_,_,_ ->},
            navigateToAllVideosScreen = {}
        )
    }
}