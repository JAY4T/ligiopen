package com.admin.ligiopen.ui.screens.news.newsManagement

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.R
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.clubs
import com.admin.ligiopen.ui.nav.AppNavigation
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth

object NewsAdditionScreenDestination: AppNavigation {
    override val title: String = "News addition screen"
    override val route: String = "news-addition-screen"
}

@Composable
fun NewsAdditionScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToNewsDetailsScreen: (newsId: String) -> Unit,
) {
    val context = LocalContext.current
    val viewModel: NewsAdditionViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        navigateToNewsDetailsScreen(uiState.publishedNews.id.toString())
        viewModel.resetStatus()
    }

    var showPublishPopup by rememberSaveable { mutableStateOf(false) }

    var currentScreen by rememberSaveable { mutableStateOf(NewsAdditionScreenStage.CLUB_SELECTION_SECTION) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri ->
            if(uri != null) {
                viewModel.changePhoto(uri)
            }
        }
    )

    if(showPublishPopup) {
        PublishPopup(
            onConfirm = {
                viewModel.publishNews(context)
                showPublishPopup = !showPublishPopup
            },
            onDismiss = { showPublishPopup = !showPublishPopup }
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        NewsAdditionScreen(
            title = uiState.title,
            onChangeTitle = viewModel::changeTitle,
            subtitle = uiState.subtitle,
            onChangeSubTitle = viewModel::changeSubTitle,
            coverPhoto = uiState.coverPhoto,
            clubs = uiState.clubs,
            selectedClubIds = uiState.selectedClubIds,
            onSelectClub = viewModel::selectClub,
            onRemoveClub = viewModel::removeClub,
            onUploadCoverPhoto = {
                galleryLauncher.launch("image/*")
            },
            onRemoveCoverPhoto = viewModel::removePhoto,
            newsAdditionScreenStage = currentScreen,
            onChangeScreen = {
                currentScreen = it
            },
            navigateToPreviousScreen = navigateToPreviousScreen,
            onPublish = {
                showPublishPopup = !showPublishPopup
            },
            buttonEnabled = uiState.buttonEnabled,
            loadingStatus = uiState.loadingStatus
        )
    }
}

@Composable
fun NewsAdditionScreen(
    title: String,
    subtitle: String,
    onChangeTitle: (title: String) -> Unit,
    onChangeSubTitle: (subTitle: String) -> Unit,
    clubs: List<ClubData>,
    selectedClubIds: List<Int>,
    onSelectClub: (clubId: Int) -> Unit,
    onRemoveClub: (clubId: Int) -> Unit,
    coverPhoto: Uri?,
    onUploadCoverPhoto: () -> Unit,
    onRemoveCoverPhoto: () -> Unit,
    newsAdditionScreenStage: NewsAdditionScreenStage,
    navigateToPreviousScreen: () -> Unit,
    onChangeScreen: (screen: NewsAdditionScreenStage) -> Unit,
    onPublish: () -> Unit,
    buttonEnabled: Boolean,
    loadingStatus: LoadingStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = screenWidth(x = 16.0),
                vertical = screenHeight(x = 16.0)
            )
    ) {
        when(newsAdditionScreenStage) {
            NewsAdditionScreenStage.CLUB_SELECTION_SECTION -> {
                ClubSelectionSectionScreen(
                    selectedClubIds = selectedClubIds,
                    onSelectClub = onSelectClub,
                    onRemoveClub = onRemoveClub,
                    navigateToPreviousScreen = navigateToPreviousScreen,
                    navigateToNextScreen = {
                        onChangeScreen(NewsAdditionScreenStage.HEADING_SECTION)
                    },
                    clubs = clubs
                )
            }
            NewsAdditionScreenStage.HEADING_SECTION -> {
                NewsHeadingSectionScreen(
                    title = title,
                    onChangeTitle = onChangeTitle,
                    subtitle = subtitle,
                    onChangeSubTitle = onChangeSubTitle,
                    coverPhoto = coverPhoto,
                    onUploadCoverPhoto = onUploadCoverPhoto,
                    onRemoveCoverPhoto = onRemoveCoverPhoto,
                    navigateToPreviousScreen = {
                        onChangeScreen(NewsAdditionScreenStage.CLUB_SELECTION_SECTION)
                    },
                    onPublish = onPublish,
                    buttonEnabled = buttonEnabled,
                    loadingStatus = loadingStatus
                )
            }
            NewsAdditionScreenStage.ITEMS_SECTION -> {}
        }
    }
}

@Composable
fun ClubSelectionSectionScreen(
    clubs: List<ClubData>,
    selectedClubIds: List<Int>,
    onSelectClub: (clubId: Int) -> Unit,
    onRemoveClub: (clubId: Int) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    navigateToNextScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Column {
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
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = "News",
                        fontSize = screenFontSize(x = 16.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "Select the club(s) involved",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                LazyColumn {
                    items(clubs) {club ->
                        SelectableClubCard(
                            club = club,
                            selectedClubIds = selectedClubIds,
                            onSelectClub = onSelectClub,
                            onRemoveClub = onRemoveClub
                        )
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    }
                }
            }
        }
        Button(
            onClick = navigateToNextScreen,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = if(selectedClubIds.isEmpty()) "Skip" else "Next",
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
    }
}

@Composable
fun SelectableClubCard(
    club: ClubData,
    selectedClubIds: List<Int>,
    onSelectClub: (clubId: Int) -> Unit,
    onRemoveClub: (clubId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = club.clubLogo.link,
            contentDescription = "Image of ${club.name}",
            modifier = Modifier
                .size(screenWidth(x = 48.0))
                .clip(RoundedCornerShape(screenWidth(x = 8.0))),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
        Text(
            text = club.name,
            fontSize = screenFontSize(x = 14.0).sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            if(selectedClubIds.contains(club.clubId)) {
                onRemoveClub(club.clubId)
            } else {
                onSelectClub(club.clubId)
            }
        }) {
            Icon(
                painter = painterResource(id = if(selectedClubIds.contains(club.clubId)) R.drawable.checked_box else R.drawable.unchecked_box),
                contentDescription = "Select club"
            )
        }
    }
}

@Composable
fun NewsHeadingSectionScreen(
    title: String,
    onChangeTitle: (title: String) -> Unit,
    onChangeSubTitle: (subTitle: String) -> Unit,
    subtitle: String,
    coverPhoto: Uri?,
    onUploadCoverPhoto: () -> Unit,
    onRemoveCoverPhoto: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    onPublish: () -> Unit,
    buttonEnabled: Boolean,
    loadingStatus: LoadingStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Column {
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
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = "News",
                        fontSize = screenFontSize(x = 16.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "News heading",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                TextField(
                    label = {
                        Text(
                            text = "Title",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    },
                    value = title,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    onValueChange = onChangeTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                TextField(
                    label = {
                        Text(
                            text = "Subtitle",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    },
                    value = subtitle,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    onValueChange = onChangeSubTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "Upload cover photo",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                if(coverPhoto != null) {
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(coverPhoto)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.loading_img),
                            error = painterResource(id = R.drawable.ic_broken_image),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Cover photo",
                            modifier = Modifier
                                .height(screenHeight(x = 250.0))
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                        )
                        IconButton(
                            modifier = Modifier
                                .alpha(0.5f)
                                .background(Color.Black)
                                .padding(
                                    start = screenWidth(x = 5.0),
                                    end = screenWidth(x = 5.0),
                                )
                                .align(Alignment.TopEnd),
                            onClick = onRemoveCoverPhoto
                        ) {
                            Icon(
                                tint = Color.White,
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Remove front id"
                            )
                        }
                    }
                } else {
                    CoverPhotoUpload(
                        onUploadPhoto = onUploadCoverPhoto
                    )
                }

            }
        }
        Button(
            enabled = buttonEnabled && loadingStatus != LoadingStatus.LOADING,
            onClick = onPublish,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(loadingStatus == LoadingStatus.LOADING) {
                Text(
                    text = "Publishing...",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Publish",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Icon(
                        painter = painterResource(id = R.drawable.save),
                        contentDescription = "Publish news"
                    )
                }
            }
        }

    }
}

@Composable
fun CoverPhotoUpload(
    onUploadPhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(screenHeight(x = 250.0))
                .fillMaxWidth()
                .border(
                    width = screenWidth(x = 1.0),
                    color = Color.LightGray,
                    shape = RoundedCornerShape(screenWidth(x = 10.0))
                )
        ) {
            IconButton(
//                enabled = verificationStatus != VerificationStatus.PENDING_VERIFICATION && verificationStatus != VerificationStatus.VERIFIED,
                onClick = onUploadPhoto
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.photo_upload),
                    contentDescription = "Upload photo"
                )
            }
        }
    }
}

@Composable
fun PublishPopup(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Publish news",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to publish this news?",
                fontSize = screenFontSize(x = 14.0).sp,
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
            Button(onClick = onConfirm) {
                Text(
                    text = "Publish",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsAdditionScreenPreview() {
    LigiopenadminTheme {
        NewsAdditionScreen(
            title = "",
            onChangeTitle = {},
            subtitle = "",
            onChangeSubTitle = {},
            clubs = clubs,
            selectedClubIds = listOf(1, 2, 5),
            onSelectClub = {},
            onRemoveClub = {},
            coverPhoto = null,
            onUploadCoverPhoto = {},
            onRemoveCoverPhoto = {},
            newsAdditionScreenStage = NewsAdditionScreenStage.ITEMS_SECTION,
            navigateToPreviousScreen = {},
            onChangeScreen = {},
            onPublish = {},
            buttonEnabled = false,
            loadingStatus = LoadingStatus.INITIAL
        )
    }
}