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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.admin.ligiopen.ui.nav.AppNavigation
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth


object NewsItemAdditionScreenDestination: AppNavigation {
    override val title: String = "News item addition screen"
    override val route: String = "news-item-addition-screen"
    val newsId: String = "newsId"
    val routeWithNewsId: String = "$route/{$newsId}"
}

@Composable
fun NewsItemAdditionScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: NewsItemAdditionViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        navigateToPreviousScreen()
        viewModel.resetStatus()
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri ->
            if(uri != null) {
                viewModel.changePhoto(uri)
            }
        }
    )

    var showParagraphConfirmationDialog by remember { mutableStateOf(false) }

    if(showParagraphConfirmationDialog) {
        ConfirmParagraphPopup(
            onConfirm = {
                viewModel.publishNewsItem(context)
                showParagraphConfirmationDialog = false
            },
            onDismiss = { showParagraphConfirmationDialog = false }
        )

    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        NewsItemAdditionScreen(
            title = uiState.title,
            subtitle = uiState.subtitle,
            paragraph = uiState.paragraph,
            paragraphFile = uiState.photo,
            onUploadFile = {
                galleryLauncher.launch("image/*")
            },
            onRemoveFile = viewModel::removePhoto,
            onChangeTitle = viewModel::changeTitle,
            onChangeSubTitle = viewModel::changeSubTitle,
            onChangeParagraph = viewModel::changeParagraph,
            onAddParagraph = {
                showParagraphConfirmationDialog = !showParagraphConfirmationDialog
            },
            navigateToPreviousScreen = navigateToPreviousScreen,
            loadingStatus = uiState.loadingStatus
        )
    }
}

@Composable
fun NewsItemAdditionScreen(
    title: String,
    onChangeTitle: (title: String) -> Unit,
    subtitle: String,
    onChangeSubTitle: (subtitle: String) -> Unit,
    paragraph: String,
    onChangeParagraph: (paragraph: String) -> Unit,
    paragraphFile: Uri?,
    onUploadFile: () -> Unit = {},
    onRemoveFile: () -> Unit = {},
    onAddParagraph: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
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
                text = "News: Paragraph",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Create a new paragraph",
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Column {
                OutlinedTextField(
                    label = {
                        Text(
                            text = "Title"
                        )
                    },
                    value = title,
                    onValueChange = onChangeTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                OutlinedTextField(
                    label = {
                        Text(
                            text = "Subtitle"
                        )
                    },
                    value = subtitle,
                    onValueChange = onChangeSubTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                OutlinedTextField(
                    label = {
                        Text(
                            text = "Paragraph"
                        )
                    },
                    value = paragraph,
                    onValueChange = onChangeParagraph,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "Upload photo",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                if(paragraphFile != null) {
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(paragraphFile)
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
                            onClick = onRemoveFile
                        ) {
                            Icon(
                                tint = Color.White,
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Remove front id"
                            )
                        }
                    }
                } else {
                    ParagraphPhotoUpload(
                        onUploadPhoto = onUploadFile
                    )
                }

            }
        }
        Button(
            enabled = loadingStatus != LoadingStatus.LOADING,
            onClick = onAddParagraph,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(loadingStatus == LoadingStatus.LOADING) {
                Text(text = "Loading...")
            } else {
                Text(text = "Add paragraph")
            }
        }


    }
}

@Composable
fun ParagraphPhotoUpload(
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
fun ConfirmParagraphPopup(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "New paragraph",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to add this paragraph?",
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
            Button(onClick = onConfirm) {
                Text(
                    text = "Confirm",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsItemAdditionScreenPreview() {
    LigiopenadminTheme {
        NewsItemAdditionScreen(
            title = "",
            onChangeTitle = {},
            subtitle = "",
            onChangeSubTitle = {},
            paragraph = "",
            onChangeParagraph = {},
            paragraphFile = null,
            onAddParagraph = { /*TODO*/ },
            navigateToPreviousScreen = { /*TODO*/ },
            loadingStatus = LoadingStatus.INITIAL
        )
    }
}