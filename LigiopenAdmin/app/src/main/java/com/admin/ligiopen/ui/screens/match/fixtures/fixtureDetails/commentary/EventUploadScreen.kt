package com.admin.ligiopen.ui.screens.match.fixtures.fixtureDetails.commentary

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.R
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.ui.nav.AppNavigation
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.composables.OutlinedTextFieldComposable
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth

object EventUploadScreenDestination: AppNavigation {
    override val title: String = "Event upload screen"
    override val route: String = "event_upload_screen"
    val matchFixtureId: String = "matchFixtureId"
    val routeWithFixtureId: String = "$route/{$matchFixtureId}"
}

@Composable
fun EventUploadScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: EventUploadViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri ->
            if(uri != null) {
                viewModel.uploadFile(uri)
            }
        }
    )

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    var showConfirmDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        showSuccessDialog = true
        viewModel.resetStatus()
    } else if(uiState.loadingStatus == LoadingStatus.FAIL) {
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        viewModel.resetStatus()
    }

    if(showConfirmDialog) {
        EventUploadConfirmDialog(
            event = uiState.selectedEventType ?: "",
            onConfirm = {
                viewModel.addEvent(context)
                showConfirmDialog = false
            },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }

    if(showSuccessDialog) {
        EventUploadSuccessDialog(
            event = uiState.selectedEventType ?: "",
            onConfirm = {
                showSuccessDialog = false
                navigateToPreviousScreen()
            },
            onDismiss = {
                showSuccessDialog = false
                navigateToPreviousScreen()
            }
        )
    }

    val eventTypes = listOf(
        "Goal",
        "Own goal",
        "Substitution",
        "Foul",
        "Yellow card",
        "Red card",
        "Offside",
        "Corner kick",
        "Free kick",
        "Penalty",
        "Penalty missed",
        "Injury",
        "Throw in",
        "Goal kick",
        "Kick off",
        "Half time",
        "Full time"
    )

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        EventUploadScreen(
            eventTypes = eventTypes,
            expanded = expanded,
            selectedEventType = uiState.selectedEventType,
            file = uiState.file,
            onUploadFile = { galleryLauncher.launch("image/*") },
            onRemoveFile = { viewModel.uploadFile(null) },
            onSelectEvent = viewModel::updateEventType,
            onExpand = {
                expanded = !expanded
            },
            buttonEnabled = true,
            onAddEvent =  {
                showConfirmDialog = true
            },
            minute = uiState.minute,
            onChangeMinute = viewModel::updateMinute,
            title = uiState.title,
            onChangeTitle = viewModel::updateTitle,
            summary = uiState.summary,
            onChangeSummary = viewModel::updateSummary,
            searchMainPlayerText = uiState.mainPlayerText,
            onSearchMainPlayer = viewModel::onSearchMainPlayer,
            searchSecondaryPlayerText = uiState.secondaryPlayerText,
            onSelectSecondaryPlayer = viewModel::onSelectSecondaryPlayer,
            mainPlayer = uiState.mainPlayer,
            secondaryPlayer = uiState.secondaryPlayer,
            onSelectMainPlayer = viewModel::onSelectMainPlayer,
            onSearchSecondaryPlayer = viewModel::onSearchSecondaryPlayer,
            mainPlayerClubDataList = uiState.mainPlayerClubDataList,
            secondaryPlayerClubDataList = uiState.secondaryPlayerClubDataList,
            isYellowCard = uiState.isYellowCard,
            isRedCard = uiState.isRedCard,
            onChangeIsYellowCard = viewModel::updateIsYellowCard,
            onChangeIsRedCard = viewModel::updateIsRedCard,
            freeKickScored = uiState.freeKickScored,
            penaltyScored = uiState.penaltyScored,
            onChangeIsFreeKickScored = viewModel::updateIsFreeKickScored,
            onChangeIsPenaltyScored = viewModel::updateIsPenaltyScored,
            navigateToPreviousScreen = navigateToPreviousScreen,
            loadingStatus = uiState.loadingStatus
        )
    }


}

@Composable
fun EventUploadScreen(
    eventTypes: List<String>,
    expanded: Boolean,
    selectedEventType: String?,
    file: Uri?,
    onUploadFile: () -> Unit,
    onRemoveFile: () -> Unit,
    onSelectEvent: (event: String) -> Unit,
    onExpand: () -> Unit,
    buttonEnabled: Boolean,
    onAddEvent: () -> Unit,
    minute: String,
    onChangeMinute: (minute: String) -> Unit,
    title: String,
    onChangeTitle: (title: String) -> Unit,
    summary: String,
    onChangeSummary: (summary: String) -> Unit,
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    searchMainPlayerText: String,
    onSearchMainPlayer: (name: String) -> Unit,
    onSearchSecondaryPlayer: (name: String) -> Unit,
    searchSecondaryPlayerText: String,
    mainPlayer: PlayerClubData?,
    secondaryPlayer: PlayerClubData?,
    onSelectMainPlayer: (playerClubData: PlayerClubData) -> Unit,
    onSelectSecondaryPlayer: (playerClubData: PlayerClubData) -> Unit,
    isYellowCard: Boolean,
    onChangeIsYellowCard: () -> Unit,
    isRedCard: Boolean,
    onChangeIsRedCard: () -> Unit,
    penaltyScored: Boolean,
    onChangeIsPenaltyScored: () -> Unit,
    freeKickScored: Boolean,
    onChangeIsFreeKickScored: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    loadingStatus: LoadingStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = "Minute commentary",
                fontSize = screenFontSize(x = 16.0).sp,
            )
            if(selectedEventType != null) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = selectedEventType.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp,
                )
            }
        }
        Column(
            modifier = Modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextFieldComposable(
                    label = "Minute",
                    value = minute,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    shape = RoundedCornerShape(screenWidth(x = 8.0)),
                    onValueChange = onChangeMinute,
                    modifier = Modifier.weight(0.1f)
                )

                Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))

                Box(
                    modifier = Modifier
                        .weight(0.1f)
                        .clickable {
                            onExpand()
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                vertical = screenHeight(x = 16.0),
                                horizontal = screenWidth(x = 8.0)
                            )
                            .animateContentSize(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = selectedEventType ?: "Select event type")
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Icon(
                                imageVector = if(expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select an event"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = onExpand,
                            modifier = Modifier
                                .width(screenWidth(x = 180.0)) // Adjust width as needed
                                .heightIn(
                                    min = screenHeight(x = 50.0),
                                    max = screenHeight(x = 450.0)
                                ) // Set max height
                        ) {
                            eventTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = type.uppercase(),
                                            fontSize = screenFontSize(x = 14.0).sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    onClick = {
                                        onSelectEvent(type)
                                        onExpand()
                                    }
                                )
                                HorizontalDivider()
                            }
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
            ) {
                OutlinedTextFieldComposable(
                    label = "Title",
                    value = title,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = onChangeTitle,
                    shape = RoundedCornerShape(screenWidth(x = 8.0)),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                EventCustomTextFields(
                    mainPlayerClubDataList = mainPlayerClubDataList,
                    secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                    mainPlayer = mainPlayer,
                    secondaryPlayer = secondaryPlayer,
                    selectedEventType = selectedEventType ?: "",
                    searchMainPlayerText = searchMainPlayerText,
                    searchSecondaryPlayerText = searchSecondaryPlayerText,
                    onSearchMainPlayer = onSearchMainPlayer,
                    onSearchSecondaryPlayer = onSearchSecondaryPlayer,
                    onSelectMainPlayer = onSelectMainPlayer,
                    isYellowCard = isYellowCard,
                    onChangeIsYellowCard = onChangeIsYellowCard,
                    isRedCard = isRedCard,
                    onChangeIsRedCard = onChangeIsRedCard,
                    penaltyScored = penaltyScored,
                    onChangeIsPenaltyScored = onChangeIsPenaltyScored,
                    freeKickScored = freeKickScored,
                    onChangeIsFreeKickScored = onChangeIsFreeKickScored,
                    onSelectSecondaryPlayer = onSelectSecondaryPlayer
                )
                OutlinedTextFieldComposable(
                    label = "Summary",
                    value = summary,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = onChangeSummary,
                    shape = RoundedCornerShape(screenWidth(x = 8.0)),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = "Upload photo/video",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                if(file != null) {
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(file)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.loading_img),
                            error = painterResource(id = R.drawable.ic_broken_image),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Front ID",
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
                    EventFileUpload(
                        onUploadFile = onUploadFile
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 32.0)))
            Button(
                enabled = buttonEnabled && loadingStatus != LoadingStatus.LOADING,
                onClick = onAddEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenHeight(x = 8.0))
            ) {
                Text(
                    text = if (loadingStatus == LoadingStatus.LOADING) "Loading..." else "Add event",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EventCustomTextFields(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    secondaryPlayer: PlayerClubData?,
    selectedEventType: String,
    searchMainPlayerText: String,
    searchSecondaryPlayerText: String,
    onSearchMainPlayer: (name: String) -> Unit,
    onSearchSecondaryPlayer: (name: String) -> Unit,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSelectSecondaryPlayer: (player: PlayerClubData) -> Unit,
    isYellowCard: Boolean,
    onChangeIsYellowCard: () -> Unit,
    isRedCard: Boolean,
    onChangeIsRedCard: () -> Unit,
    penaltyScored: Boolean,
    onChangeIsPenaltyScored: () -> Unit,
    freeKickScored: Boolean,
    onChangeIsFreeKickScored: () -> Unit,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
    AnimatedContent(
        targetState = selectedEventType,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) with  fadeOut(animationSpec = tween(300))
        }
    ) { eventType ->
        when(eventType) {
            "Goal" -> GoalFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                secondaryPlayer = secondaryPlayer,
                searchMainPlayerText = searchMainPlayerText,
                searchSecondaryPlayerText = searchSecondaryPlayerText,
                onSearchMainPlayer = onSearchMainPlayer,
                onSearchSecondaryPlayer = onSearchSecondaryPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSelectSecondaryPlayer = onSelectSecondaryPlayer
            )
            "Own goal" -> OwnGoalFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                searchMainPlayerText = searchMainPlayerText
            )
            "Foul" -> FoulFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                secondaryPlayer = secondaryPlayer,
                searchMainPlayerText = searchMainPlayerText,
                searchSecondaryPlayerText = searchSecondaryPlayerText,
                onSearchMainPlayer = onSearchMainPlayer,
                onSearchSecondaryPlayer = onSearchSecondaryPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                isYellowCard = isYellowCard,
                isRedCard = isRedCard,
                onChangeIsYellowCard = onChangeIsYellowCard,
                onChangeIsRedCard = onChangeIsRedCard,
                onSelectSecondaryPlayer = onSelectSecondaryPlayer
            )
            "Substitution" -> SubstitutionFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                secondaryPlayer = secondaryPlayer,
                searchMainPlayerText = searchMainPlayerText,
                searchSecondaryPlayerText = searchSecondaryPlayerText,
                onSearchMainPlayer = onSearchMainPlayer,
                onSearchSecondaryPlayer = onSearchSecondaryPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSelectSecondaryPlayer = onSelectSecondaryPlayer
            )
            "Yellow card" -> YellowCardFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                searchMainPlayerText = searchMainPlayerText
            )
            "Red card" -> RedCardFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                searchMainPlayerText = searchMainPlayerText
            )
            "Offside" -> OffsideFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                searchMainPlayerText = searchMainPlayerText
            )
            "Corner kick" -> CornerKickFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                searchMainPlayerText = searchMainPlayerText
            )
            "Free kick" -> FreeKickFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                freeKickScored = freeKickScored,
                onChangeIsFreeKickScored = onChangeIsFreeKickScored,
                searchMainPlayerText = searchMainPlayerText
            )
            "Penalty" -> PenaltyFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                penaltyScored = penaltyScored,
                onChangeIsPenaltyScored = onChangeIsPenaltyScored,
                searchMainPlayerText = searchMainPlayerText
            )
            "Penalty missed" -> PenaltyMissedFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                searchMainPlayerText = searchMainPlayerText
            )
            "Injury" -> InjuryFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                searchMainPlayerText = searchMainPlayerText
            )
            "Throw in" -> ThrowInFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                searchMainPlayerText = searchMainPlayerText
            )
            "Goal kick" -> GoalKickFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                searchMainPlayerText = searchMainPlayerText
            )
            "Kick off" -> KickOffFieldComposable(
                mainPlayerClubDataList = mainPlayerClubDataList,
                secondaryPlayerClubDataList = secondaryPlayerClubDataList,
                mainPlayer = mainPlayer,
                onSelectMainPlayer = onSelectMainPlayer,
                onSearchMainPlayer = onSearchMainPlayer,
                searchMainPlayerText = searchMainPlayerText
            )
        }
    }
}

@Composable
fun GoalFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    secondaryPlayer: PlayerClubData?,
    searchMainPlayerText: String,
    searchSecondaryPlayerText: String,
    onSearchMainPlayer: (name: String) -> Unit,
    onSearchSecondaryPlayer: (name: String) -> Unit,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSelectSecondaryPlayer: (player: PlayerClubData) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Scorer:",
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Scorer",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }

//        Assist

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Assist:",
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            if(secondaryPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = secondaryPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(secondaryPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(secondaryPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Assist",
            value = searchSecondaryPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchSecondaryPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(secondaryPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    secondaryPlayerClubDataList.filter{
                        player -> player.playerId != mainPlayer?.playerId && player.clubId == mainPlayer?.clubId && !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectSecondaryPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectSecondaryPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun OwnGoalFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    modifier: Modifier = Modifier,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Own scorer:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Own scorer",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoulFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    secondaryPlayer: PlayerClubData?,
    searchMainPlayerText: String,
    searchSecondaryPlayerText: String,
    onSearchMainPlayer: (name: String) -> Unit,
    onSearchSecondaryPlayer: (name: String) -> Unit,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSelectSecondaryPlayer: (player: PlayerClubData) -> Unit,
    isYellowCard: Boolean,
    onChangeIsYellowCard: () -> Unit,
    isRedCard: Boolean,
    onChangeIsRedCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Offender:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Offender",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Victim:")
            Spacer(modifier = Modifier.weight(1f))
            if(secondaryPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = secondaryPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(secondaryPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(secondaryPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Victim",
            value = searchSecondaryPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchSecondaryPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(secondaryPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    secondaryPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectSecondaryPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectSecondaryPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Yellow card:")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = if (isYellowCard) "Cautioned" else "")
            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            Switch(
                checked = isYellowCard,
                onCheckedChange = {
                    onChangeIsYellowCard()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Yellow
                )
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Red card:")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = if (isRedCard) "Dismissed" else "")
            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            Switch(
                checked = isRedCard,
                onCheckedChange = {
                    onChangeIsRedCard()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Red
                )
            )
        }
    }
}

@Composable
fun SubstitutionFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    secondaryPlayer: PlayerClubData?,
    searchMainPlayerText: String,
    searchSecondaryPlayerText: String,
    onSearchMainPlayer: (name: String) -> Unit,
    onSearchSecondaryPlayer: (name: String) -> Unit,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSelectSecondaryPlayer: (player: PlayerClubData) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "In:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Substitute",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }

//        Out
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Out:")
            Spacer(modifier = Modifier.weight(1f))
            if(secondaryPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = secondaryPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(secondaryPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(secondaryPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Substituted player",
            value = searchSecondaryPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchSecondaryPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(secondaryPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    secondaryPlayerClubDataList.filter { player ->
                        !player.bench && mainPlayer?.clubId == player.clubId
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectSecondaryPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectSecondaryPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YellowCardFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Cautioned player:")
        Spacer(modifier = Modifier.weight(1f))
        if(mainPlayer != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Text(text = mainPlayer.name)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if(mainPlayer.home) "HOME" else "AWAY",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(mainPlayer.clubLogo)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Club logo",
                    modifier = Modifier
                        .size(screenWidth(x = 24.0))
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                )
            }
        }
    }
    OutlinedTextFieldComposable(
        label = "Cautioned player",
        value = searchMainPlayerText,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        onValueChange = onSearchMainPlayer,
        modifier = Modifier
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
    if(mainPlayerClubDataList.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight(x = 80.0))
                .padding(
                    vertical = screenHeight(x = 8.0),
                    horizontal = screenWidth(x = 8.0)
                )

        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                mainPlayerClubDataList.filter { player ->
                    !player.bench
                }.forEach { player ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                onSelectMainPlayer(player)
                            }
                    ) {
                        Text(text = player.name)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = if(player.home) "HOME" else "AWAY",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        TextButton(onClick = { onSelectMainPlayer(player) }) {
                            Text(
                                text = "Select",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RedCardFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Dismissed player:")
        Spacer(modifier = Modifier.weight(1f))
        if(mainPlayer != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Text(text = mainPlayer.name)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if(mainPlayer.home) "HOME" else "AWAY",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(mainPlayer.clubLogo)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Club logo",
                    modifier = Modifier
                        .size(screenWidth(x = 24.0))
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                )
            }
        }
    }
    OutlinedTextFieldComposable(
        label = "Dismissed player",
        value = searchMainPlayerText,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        onValueChange = onSearchMainPlayer,
        modifier = Modifier
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
    if(mainPlayerClubDataList.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight(x = 80.0))
                .padding(
                    vertical = screenHeight(x = 8.0),
                    horizontal = screenWidth(x = 8.0)
                )

        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                mainPlayerClubDataList.filter { player ->
                    !player.bench
                }.forEach { player ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                onSelectMainPlayer(player)
                            }
                    ) {
                        Text(text = player.name)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = if(player.home) "HOME" else "AWAY",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        TextButton(onClick = { onSelectMainPlayer(player) }) {
                            Text(
                                text = "Select",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OffsideFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Offside player:",
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Offside player",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CornerKickFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Corner taker:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Corner taker",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FreeKickFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    freeKickScored: Boolean,
    onChangeIsFreeKickScored: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Fre kick taker:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Free kick taker",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Free kick Scored:")
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = freeKickScored,
                onCheckedChange = {
                    onChangeIsFreeKickScored()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Green
                )
            )
            Text(text = if (freeKickScored) "Scored" else "Missed")
        }
    }

}

@Composable
fun PenaltyFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    penaltyScored: Boolean,
    onChangeIsPenaltyScored: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Penalty taker:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Penalty taker",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Penalty Scored:")
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = penaltyScored,
                onCheckedChange = {
                    onChangeIsPenaltyScored()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Green
                )
            )
            Text(text = if (penaltyScored) "Scored" else "Missed")
        }
    }
}


@Composable
fun PenaltyMissedFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Penalty taker:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Penalty taker",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InjuryFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Injured player:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Injured player",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThrowInFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Thrower:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Thrower",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoalKickFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Goalkeeper:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Goalkeeper",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KickOffFieldComposable(
    mainPlayerClubDataList: List<PlayerClubData>,
    secondaryPlayerClubDataList: List<PlayerClubData>,
    mainPlayer: PlayerClubData?,
    onSelectMainPlayer: (player: PlayerClubData) -> Unit,
    onSearchMainPlayer: (name: String) -> Unit,
    searchMainPlayerText: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Kick off taker:")
            Spacer(modifier = Modifier.weight(1f))
            if(mainPlayer != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(text = mainPlayer.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if(mainPlayer.home) "HOME" else "AWAY",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(mainPlayer.clubLogo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Club logo",
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(screenWidth(x = 10.0)))
                    )
                }
            }
        }
        OutlinedTextFieldComposable(
            label = "Kick off taker",
            value = searchMainPlayerText,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onSearchMainPlayer,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(mainPlayerClubDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight(x = 80.0))
                    .padding(
                        vertical = screenHeight(x = 8.0),
                        horizontal = screenWidth(x = 8.0)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    mainPlayerClubDataList.filter { player ->
                        !player.bench
                    }.forEach { player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onSelectMainPlayer(player)
                                }
                        ) {
                            Text(text = player.name)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if(player.home) "HOME" else "AWAY",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            TextButton(onClick = { onSelectMainPlayer(player) }) {
                                Text(
                                    text = "Select",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventFileUpload(
    onUploadFile: () -> Unit,
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
                onClick = onUploadFile
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.photo_upload),
                    contentDescription = "Upload logo"
                )
            }
        }
    }
}

@Composable
fun EventUploadConfirmDialog(
    event: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = event.uppercase(),
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to upload $event?",
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

@Composable
fun EventUploadSuccessDialog(
    event: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Success!",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "$event has been uploaded successfully!",
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
fun EventUploadScreenPreview() {
    LigiopenadminTheme {
        val eventTypes = listOf(
            "Goal",
            "Own goal",
            "Substitution",
            "Foul",
            "Yellow card",
            "Red card",
            "Offside",
            "Corner kick",
            "Free kick",
            "Penalty",
            "Penalty missed",
            "Injury",
            "Throw in",
            "Goal kick",
            "Kick off",
            "Half time",
            "Full time"
        )
        var expanded by rememberSaveable {
            mutableStateOf(false)
        }

        var selectedEventType by rememberSaveable {
            mutableStateOf<String?>("Goal")
        }

        EventUploadScreen(
            eventTypes = eventTypes,
            expanded = expanded,
            selectedEventType = selectedEventType,
            file = null,
            onUploadFile = { /*TODO*/ },
            onRemoveFile = { /*TODO*/ },
            onSelectEvent = {
                selectedEventType = it
            },
            onExpand = { /*TODO*/ },
            buttonEnabled = false,
            onAddEvent = { /*TODO*/ },
            minute = "",
            onChangeMinute = {},
            title = "",
            onChangeTitle = {},
            summary = "",
            onChangeSummary = {},
            onSelectMainPlayer = {},
            onSearchSecondaryPlayer = {},
            searchMainPlayerText = "",
            searchSecondaryPlayerText = "",
            onSelectSecondaryPlayer = {},
            mainPlayer = PlayerClubData(),
            secondaryPlayer = PlayerClubData(),
            mainPlayerClubDataList = emptyList(),
            secondaryPlayerClubDataList = emptyList(),
            onSearchMainPlayer = {},
            isYellowCard = false,
            isRedCard = false,
            onChangeIsYellowCard = {},
            onChangeIsRedCard = {},
            freeKickScored = false,
            penaltyScored = false,
            onChangeIsFreeKickScored = {},
            onChangeIsPenaltyScored = {},
            navigateToPreviousScreen = {},
            loadingStatus = LoadingStatus.INITIAL
        )
    }
}