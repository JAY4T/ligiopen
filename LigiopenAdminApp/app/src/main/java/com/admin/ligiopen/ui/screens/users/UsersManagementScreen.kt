package com.admin.ligiopen.ui.screens.users

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.user.UserDto
import com.admin.ligiopen.data.network.models.user.users
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth
import kotlinx.coroutines.launch

@Composable
fun UsersManagementScreenComposable(
    navigateToUserSearchScreen: (adminType: String) -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: UsersManagementViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        UsersManagementScreen(
            users = uiState.users,
            currentTab = uiState.currentTab,
            onChangeTab = viewModel::switchTab,
            loadingStatus = uiState.loadingStatus,
            navigateToUserSearchScreen = navigateToUserSearchScreen
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UsersManagementScreen(
    users: List<UserDto>,
    currentTab: UserRole,
    onChangeTab: (tab: UserRole) -> Unit,
    loadingStatus: LoadingStatus,
    navigateToUserSearchScreen: (adminType: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val roles = listOf("Super Admin", "Team Admin", "Content Admin")
    val pagerState = rememberPagerState(pageCount = {roles.size})

    LaunchedEffect(pagerState.currentPage) {
        val selectedTab = roles[pagerState.currentPage]
        onChangeTab(UserRole.valueOf(selectedTab.replace(" ", "_").uppercase()))
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(16.0),
                horizontal = screenWidth(16.0)
            )
    ) {
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
            }
        ) {
            roles.forEachIndexed { index, title ->
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

        Spacer(modifier = Modifier.height(screenHeight(8.0)))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
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
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (users.isEmpty()) {
                    EmptyUsersPlaceholder(role = roles[page])
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = screenWidth(16.0), vertical = screenHeight(8.0)),
                        verticalArrangement = Arrangement.spacedBy(screenWidth(12.0))
                    ) {
                        items(users) { user ->
                            UserCard(
                                user = user,
                                onClick = {}
                            )

                        }
                    }
                }
            }

        }

        Button(
            onClick = {
                navigateToUserSearchScreen(roles[pagerState.currentPage].lowercase().replace(" ", "-"))
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Add ${currentTab.name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }}",
                fontSize = screenFontSize(14.0).sp
            )
        }

    }
}

@Composable
fun UserCard(
    user: UserDto,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(screenWidth(12.0)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = screenHeight(2.0))
    ) {
        Column(
            modifier = Modifier.padding(screenWidth(16.0))
        ) {
            // Header with role and archived status
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Role pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(screenWidth(16.0)))
                        .background(
                            when (user.role.lowercase()) {
                                "admin" -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                                "manager" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                            }
                        )
                        .padding(horizontal = screenWidth(12.0), vertical = screenHeight(4.0))
                ) {
                    Text(
                        text = user.role.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        color = when (user.role.lowercase()) {
                            "admin" -> MaterialTheme.colorScheme.error
                            "manager" -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.secondary
                        }
                    )
                }

                // Archived status
                if (user.archived) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(screenWidth(16.0)))
                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                            .padding(horizontal = screenWidth(12.0), vertical = screenHeight(4.0))
                    ) {
                        Text(
                            text = "Archived",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(screenHeight(12.0)))

            // User name and email
            Column {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(screenHeight(4.0)))

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(screenHeight(12.0)))

            // Additional info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "User ID",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = user.id.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Club ID",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = user.administeringClubId?.toString() ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (user.administeringClubId != null) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeight(12.0)))

            // Footer with creation/archival date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Created: ${user.createdAt.substringBefore("T")}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                if (user.archived && user.archivedAt != null) {
                    Text(
                        text = "Archived: ${user.archivedAt.substringBefore("T")}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyUsersPlaceholder(role: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(screenWidth(32.0)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No $role users",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UsersManagementScreenPreview() {
    LigiopenadminTheme {
        UsersManagementScreen(
            users = users,
            currentTab = UserRole.SUPER_ADMIN,
            onChangeTab = {},
            loadingStatus = LoadingStatus.INITIAL,
            navigateToUserSearchScreen = {}
        )
    }
}
