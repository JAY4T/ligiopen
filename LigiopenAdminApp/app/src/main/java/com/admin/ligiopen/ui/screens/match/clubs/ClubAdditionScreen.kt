package com.admin.ligiopen.ui.screens.match.clubs

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.admin.ligiopen.utils.TextFieldComposable
import com.admin.ligiopen.utils.composables.DatePicker
import com.admin.ligiopen.utils.composables.dateFormatter
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object ClubAdditionScreenDestination: AppNavigation {
    override val title: String = "Club addition screen"
    override val route: String = "club-addition-screen"
}

@Composable
fun ClubAdditionScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ClubAdditionViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(onBack = {
        if(uiState.loadingStatus == LoadingStatus.LOADING) {
            Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show()
        } else {
            navigateToPreviousScreen()
        }
    })

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri ->
            if(uri != null) {
                viewModel.onUploadClubLogo(uri)
            }
        }
    )


    // County Selection Dropdown
    val counties = remember {
        listOf(
            "Nairobi",
            "Mombasa", "Kisumu", "Nakuru", "Eldoret",
            "Kiambu", "Machakos", "Kakamega", // Add all 47 counties
        )
    }

    var showClubAdditionSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        showClubAdditionSuccessDialog = true
        viewModel.resetStatus()
    }

    if(showClubAdditionSuccessDialog) {
        ClubAdditionSuccessDialog(
            onDismiss = {
                showClubAdditionSuccessDialog = false
                navigateToPreviousScreen()
            }
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        ClubAdditionScreen(
            clubName = uiState.clubName,
            onChangeClubName = viewModel::onChangeClubName,
            clubDescription = uiState.clubDescription,
            onChangeClubDescription = viewModel::onChangeClubDesc,
            country = uiState.country,
            onChangeCountry = viewModel::onChangeCountry,
            counties = counties,
            county = uiState.county,
            onChangeCounty = viewModel::onChangeCounty,
            town = uiState.town,
            onChangeTown = viewModel::onChangeTown,
            selectedDate = uiState.startedOn ?: LocalDate.now(),
            onChangeDate = viewModel::changeDate,
            clubLogo = uiState.clubLogo,
            onUploadClubLogo = {
                galleryLauncher.launch("image/*")
            },
            onRemoveClubLogo = viewModel::onRemoveClubLogo,
            photos = emptyList(),
            onUploadPhoto = {},
            onRemovePhoto = {},
            buttonEnabled = true,
            loadingStatus = uiState.loadingStatus,
            onAddClub = {
                viewModel.onAddClub(context)
            },
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubAdditionScreen(
    clubName: String,
    onChangeClubName: (name: String) -> Unit,
    clubDescription: String,
    onChangeClubDescription: (desc: String) -> Unit,
    country: String,
    onChangeCountry: (country: String) -> Unit,
    counties: List<String>,
    county: String,
    onChangeCounty: (county: String) -> Unit,
    town: String,
    onChangeTown: (town: String) -> Unit,
    selectedDate: LocalDate,
    onChangeDate: (date: LocalDate) -> Unit,
    clubLogo: Uri?,
    onUploadClubLogo: () -> Unit,
    onRemoveClubLogo: () -> Unit,
    photos: List<Uri>,
    onUploadPhoto: (image: Uri) -> Unit,
    onRemovePhoto: (index: Int) -> Unit,
    buttonEnabled: Boolean,
    loadingStatus: LoadingStatus,
    onAddClub: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                enabled = loadingStatus != LoadingStatus.LOADING,
                onClick = navigateToPreviousScreen
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            Text(
                text = "Add new club",
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        TextFieldComposable(
            label = "Club name",
            value = clubName,
            onValueChange = onChangeClubName,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextFieldComposable(
            label = "Club description",
            value = clubDescription,
            onValueChange = onChangeClubDescription,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))

        // Country Selection (Fixed to Kenya)
        TextFieldComposable(
            label = "Country",
            value = country,
            onValueChange = onChangeCountry,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier
                    .weight(1f)
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor(),
                    readOnly = true,
                    value = county,
                    onValueChange = onChangeCounty,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    label = { Text("County") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    counties.forEach { county ->
                        DropdownMenuItem(
                            text = { Text(county) },
                            onClick = {
                                onChangeCounty(county)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            TextFieldComposable(
                label = "Town",
                value = town,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = onChangeTown,
                modifier = Modifier
                    .weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Started on:",
                fontSize = screenFontSize(x = 14.0).sp
            )
            TextButton(
                onClick = { /*TODO*/ },

            ) {
                Text(
                    text = dateFormatter.format(selectedDate),
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            DatePicker(date = selectedDate, onChangeDate = onChangeDate)
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Upload club logo",
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        if(clubLogo != null) {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(clubLogo)
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
                    onClick = onRemoveClubLogo
                ) {
                    Icon(
                        tint = Color.White,
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Remove front id"
                    )
                }
            }
        } else {
            ClubLogoUpload(
                logo = clubLogo,
                onUploadLogo = onUploadClubLogo
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = buttonEnabled && loadingStatus != LoadingStatus.LOADING,
            onClick = onAddClub,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(loadingStatus == LoadingStatus.LOADING) {
                Text(
                    text = "Loading...",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            } else {
                Text(
                    text = "Add club",
                    fontSize = screenFontSize(x = 14.0).sp
                )

            }

        }
    }
}

@Composable
fun ClubLogoUpload(
    logo: Uri?,
    onUploadLogo: () -> Unit,
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
                onClick = onUploadLogo
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
fun ClubAdditionSuccessDialog(
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                text = "Success",
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Club added successfully",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,

        confirmButton = {
            Button(onClick = onDismiss) {
                Text(
                    text = "Exit",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubAdditionScreenPreview() {
    LigiopenadminTheme {
        ClubAdditionScreen(
            clubName = "",
            onChangeClubName = {},
            clubDescription = "",
            onChangeClubDescription = {},
            country = "",
            onChangeCountry = {},
            counties = emptyList(),
            county = "",
            onChangeCounty = {},
            town = "",
            selectedDate = LocalDate.now(),
            onChangeDate = {},
            onChangeTown = {},
            photos = emptyList(),
            clubLogo = null,
            onRemoveClubLogo = {},
            onUploadClubLogo = {},
            onUploadPhoto = {},
            onRemovePhoto = {},
            buttonEnabled = false,
            onAddClub = {},
            loadingStatus = LoadingStatus.INITIAL,
            navigateToPreviousScreen = { /*TODO*/ }
        )
    }
}