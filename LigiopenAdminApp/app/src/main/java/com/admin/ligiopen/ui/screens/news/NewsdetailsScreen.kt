package com.admin.ligiopen.ui.screens.news

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.R
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.news.NewsItemDto
import com.admin.ligiopen.data.network.models.news.newsItems
import com.admin.ligiopen.data.network.models.news.singleNews
import com.admin.ligiopen.ui.nav.AppNavigation
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth
import java.text.SimpleDateFormat
import java.util.Locale

object NewsDetailsScreenDestination : AppNavigation {
    override val title: String = "News details screen"
    override val route: String = "news-details-screen"
    val newsId: String = "newsId"
    val routeWithNewsId: String = "$route/{$newsId}"
}

@Composable
fun NewsDetailsScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToNewsItemAdditionScreen: (newsId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity

    val viewModel: NewsDetailsViewModel = viewModel(factory = AppViewModelFactory.Factory)
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

    var showNewsEditPopup by rememberSaveable {
        mutableStateOf(false)
    }

    var showDeleteNewsPopup by rememberSaveable {
        mutableStateOf(false)
    }

    if(showNewsEditPopup) {
        NewsEditPopup(
            onDismiss = {
                showNewsEditPopup = !showNewsEditPopup
            },
            onConfirm = {
                showNewsEditPopup = !showNewsEditPopup
            },
            onEditClubsInvolved = {},
            onEditHeader = {
//                showNewsEditPopup = !showNewsEditPopup
            },
            onAddParagraph = {
                showNewsEditPopup = !showNewsEditPopup
                navigateToNewsItemAdditionScreen(uiState.news.id.toString())
            }
        )
    }

    if(showDeleteNewsPopup) {
        NewsDeletionConfirmPopup(
            onDismiss = {
                showDeleteNewsPopup = !showDeleteNewsPopup
            },
            onConfirm = {
                showDeleteNewsPopup = !showDeleteNewsPopup
            }
        )
    }

    BackHandler(onBack = navigateToPreviousScreen)
    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        NewsDetailsScreen(
            title = uiState.news.title,
            subtitle = uiState.news.subTitle,
            coverPhoto = uiState.news.coverPhoto.link,
            newsItems = uiState.news.newsItems,
            newsStatus = uiState.news.newsStatus,
            newsPulishDate = uiState.news.publishedAt,
            loadingStatus = uiState.loadingStatus,
            onClickEditNews = {
                showNewsEditPopup = !showNewsEditPopup
            },
            onClickDeleteNews = {
                showDeleteNewsPopup = !showDeleteNewsPopup
            },
            changeNewsStatus = viewModel::changeNewsStatus,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}

@Composable
fun NewsDetailsScreen(
    title: String?,
    subtitle: String?,
    coverPhoto: String?,
    newsItems: List<NewsItemDto>,
    newsStatus: String,
    newsPulishDate: String,
    loadingStatus: LoadingStatus,
    onClickEditNews: () -> Unit,
    onClickDeleteNews: () -> Unit,
    changeNewsStatus: (status: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val statusOptions = listOf("PENDING", "APPROVED", "REJECTED")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(screenWidth(16.0))
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = navigateToPreviousScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Spacer(modifier = Modifier.width(screenWidth(8.0)))
                    Text(
                        text = title ?: "News Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (loadingStatus == LoadingStatus.LOADING) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(screenHeight(16.0)))

                        // Status Management Section
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(screenWidth(12.0))
                        ) {
                            Column(
                                modifier = Modifier.padding(screenWidth(16.0))
                            ) {
                                Text(
                                    text = "News Status",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(screenHeight(12.0)))

                                // Current Status
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = screenHeight(12.0))
                                ) {
                                    Text(
                                        text = "Current:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(screenWidth(8.0)))
                                    StatusChip(status = newsStatus)
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = screenHeight(8.0)),
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                )

                                // Status Change Options
                                Text(
                                    text = "Change Status:",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = screenHeight(8.0))
                                )

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
                                            val isSelected = newsStatus == status
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
                                                    .clickable { changeNewsStatus(status) },
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

                        Spacer(modifier = Modifier.height(screenHeight(16.0)))

                        // News Content Section
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(screenWidth(12.0))
                        ) {
                            Column(
                                modifier = Modifier.padding(screenWidth(16.0))
                            ) {
                                if (coverPhoto != null) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(coverPhoto)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "News cover",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(16f / 9f)
                                            .clip(RoundedCornerShape(screenWidth(8.0)))
                                    )
                                    Spacer(modifier = Modifier.height(screenHeight(16.0)))
                                }

                                Text(
                                    text = title ?: "",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(screenHeight(8.0)))

                                Text(
                                    text = "Author Name • ${formatDate(newsPulishDate)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(screenHeight(16.0)))

                                HorizontalDivider()

                                Spacer(modifier = Modifier.height(screenHeight(16.0)))

                                newsItems.forEach { item ->
                                    NewsItemCard(newsItem = item)
                                    Spacer(modifier = Modifier.height(screenHeight(16.0)))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(screenHeight(16.0)))


                    }
                }

            }
        }

        // Action Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onClickEditNews,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(screenWidth(8.0))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "Edit",
                        modifier = Modifier.size(screenWidth(18.0))
                    )
                    Spacer(modifier = Modifier.width(screenWidth(8.0)))
                    Text("Edit")
                }
            }

            Spacer(modifier = Modifier.width(screenWidth(8.0)))

            Button(
                onClick = onClickDeleteNews,
                modifier = Modifier
                    .width(screenWidth(100.0))
                    .weight(1f),
                shape = RoundedCornerShape(screenWidth(8.0)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Delete",
                        modifier = Modifier.size(screenWidth(18.0))
                    )
                    Spacer(modifier = Modifier.width(screenWidth(8.0)))
                    Text(
                        text = "Delete",
                        fontSize = screenFontSize(14.0).sp
                    )
                }
            }
        }
    }
}

@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (status.uppercase()) {
        "APPROVED" -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        "PENDING" -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        "REJECTED" -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(screenWidth(16.0)))
            .background(backgroundColor)
            .padding(horizontal = screenWidth(12.0), vertical = screenHeight(6.0))
    ) {
        Text(
            text = status.replaceFirstChar { it.uppercase() },
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
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

@Composable
fun NewsItemCard(
    newsItem: NewsItemDto,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(screenWidth(x = 16.0))
    ) {
        if(newsItem.title != null) {
            Text(
                text = newsItem.title,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
        }
        if(newsItem.subTitle != null) {
            Text(
                text = newsItem.subTitle,
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 6.0)))
        }
        Text(
            text = newsItem.paragraph,
            fontSize = screenFontSize(x = 14.0).sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Justify
        )
        if (newsItem.file?.link != null) {
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(newsItem.file.link)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.Crop,
                contentDescription = "News Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = screenHeight(x = 250.0))
                    .padding(vertical = screenHeight(x = 8.0))
            )
        }
    }
}

@Composable
fun NewsEditPopup(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onEditClubsInvolved: () -> Unit,
    onEditHeader: () -> Unit,
    onAddParagraph: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Edit news",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable { onEditClubsInvolved() }
                        .weight(1f)
                        .border(
                            width = screenWidth(x = 1.0),
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(screenWidth(x = 8.0))
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(screenWidth(x = 8.0))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.football_club),
                            contentDescription = "Edit clubs involved"
                        )
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Text(
                            text = "Clubs involved",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable { onEditHeader() }
                        .weight(1f)
                        .border(
                            width = screenWidth(x = 1.0),
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(screenWidth(x = 8.0))
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(screenWidth(x = 8.0))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.header),
                            contentDescription = "Edit header section"
                        )
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Text(
                            text = "Header section",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable { onAddParagraph() }
                        .weight(1f)
                        .border(
                            width = screenWidth(x = 1.0),
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(screenWidth(x = 8.0))
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(screenWidth(x = 8.0))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "Add paragraph"
                        )
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Text(
                            text = "Add paragraph",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
        confirmButton = {

        }
    )
}

@Composable
fun NewsDeletionConfirmPopup(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Delete news",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete this news?",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                onClick = onConfirm
            ) {
                Text(
                    text = "Delete",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsDetailsScreenPreview() {
    LigiopenadminTheme {
        NewsDetailsScreen(
            title = singleNews.title,
            subtitle = singleNews.subTitle,
            coverPhoto = singleNews.coverPhoto.link,
            newsItems = newsItems,
            newsStatus = "PENDING",
            newsPulishDate = "2025-05-30T11:01:38.78474",
            changeNewsStatus = {},
            loadingStatus = LoadingStatus.INITIAL,
            onClickEditNews = {},
            onClickDeleteNews = {},
            navigateToPreviousScreen = {}
        )
    }
}
