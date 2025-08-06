package com.admin.ligiopen.ui.screens.match.location

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.admin.ligiopen.AppViewModelFactory
import com.admin.ligiopen.R
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.ui.nav.AppNavigation
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.TextFieldComposable
import com.admin.ligiopen.utils.composables.PhotosSelection
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth

object LocationAdditionScreenDestination: AppNavigation {
    override val title: String = "Location addition screen"
    override val route: String = "location-addition-screen"
}

@Composable
fun LocationAdditionScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: LocationAdditionVewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(onBack = {
        if(uiState.loadingStatus == LoadingStatus.LOADING) {
            Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show()
        } else {
            navigateToPreviousScreen()
        }
    })


    // County Selection Dropdown
    val counties = remember {
        listOf(
            "Nairobi",
            "Mombasa", "Kisumu", "Nakuru", "Eldoret",
            "Kiambu", "Machakos", "Kakamega", // Add all 47 counties
        )
    }

    var showLocationAdditionSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        showLocationAdditionSuccessDialog = true
        viewModel.resetStatus()
    }

    if(showLocationAdditionSuccessDialog) {
        LocationAdditionSuccessDialog(
            onDismiss = {
                showLocationAdditionSuccessDialog = false
                navigateToPreviousScreen()
            }
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        LocationAdditionScreen(
            venueName = uiState.venueName,
            onChangeVenueName = viewModel::changeVenueName,
            country = uiState.country,
            onChangeCountry = {},
            counties = counties,
            county = uiState.county,
            onChangeCounty = viewModel::updateCounty,
            town = uiState.town,
            onChangeTown = viewModel::updateTown,
            photos = uiState.photos,
            onUploadPhoto = viewModel::uploadPhoto,
            onRemovePhoto = viewModel::removePhoto,
            buttonEnabled = uiState.buttonEnabled,
            loadingStatus = uiState.loadingStatus,
            navigateToPreviousScreen = navigateToPreviousScreen,
            onCreateLocation = {
                viewModel.addLocation(context)
            }
        )
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocationAdditionScreen(
    venueName: String,
    onChangeVenueName: (name: String) -> Unit,
    country: String,
    onChangeCountry: (country: String) -> Unit,
    counties: List<String>,
    county: String,
    onChangeCounty: (county: String) -> Unit,
    town: String,
    onChangeTown: (town: String) -> Unit,
    photos: List<Uri>,
    onUploadPhoto: (image: Uri) -> Unit,
    onRemovePhoto: (index: Int) -> Unit,
    buttonEnabled: Boolean,
    loadingStatus: LoadingStatus,
    navigateToPreviousScreen: () -> Unit,
    onCreateLocation: () -> Unit,
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
                text = "Add match location",
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        TextField(
            label = {
                Text(
                    text = "Stadium / venue",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.stadium),
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            value = venueName,
            onValueChange = onChangeVenueName,
            modifier = Modifier
                .fillMaxWidth()
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
        Text(
            text = "Upload stadium / venue photos",
            fontSize = screenFontSize(x = 16.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        if(photos.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                photos.forEachIndexed { index, uri ->
                    Image(
                        rememberAsyncImagePainter(model = uri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(
                                top = screenHeight(x = 5.0),
                                end = screenWidth(x = 3.0),
                                bottom = screenHeight(x = 5.0)
                            )
                            .size(screenWidth(x = 100.0))
                    )
                    IconButton(onClick = {
                        onRemovePhoto(index)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        }
        PhotosSelection(
            onUploadPhoto = onUploadPhoto,
            onRemovePhoto = onRemovePhoto,
            photos = emptyList()
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = buttonEnabled && loadingStatus != LoadingStatus.LOADING,
            onClick = onCreateLocation,
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
                    text = "Add location",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }

        }
    }
}



@Composable
fun LocationAdditionSuccessDialog(
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
                text = "Location added successfully",
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationAdditionScreenPreview() {
    LigiopenadminTheme {
        LocationAdditionScreen(
            venueName = "",
            onChangeVenueName = {},
            country = "",
            onChangeCountry = {},
            counties = emptyList(),
            county = "",
            onChangeCounty = {},
            town = "",
            onChangeTown = {},
            photos = emptyList(),
            onUploadPhoto = {},
            onRemovePhoto = {},
            buttonEnabled = false,
            loadingStatus = LoadingStatus.INITIAL,
            navigateToPreviousScreen = { /*TODO*/ },
            onCreateLocation = { /*TODO*/ }
        )
    }
}