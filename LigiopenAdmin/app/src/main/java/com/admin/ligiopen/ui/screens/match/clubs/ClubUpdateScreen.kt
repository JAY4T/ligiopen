package com.admin.ligiopen.ui.screens.match.clubs

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.admin.ligiopen.AppViewModelFactory
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

object ClubUpdateScreenDestination: AppNavigation {
    override val title: String = "Club update screen"
    override val route: String = "club-update-screen"
    val clubId: String = "clubId"
    val routeWithClubId: String = "$route/{$clubId}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClubUpdateScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: ClubUpdateViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()



    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        Toast.makeText(context, "Club details updated", Toast.LENGTH_SHORT).show()
    } else if(uiState.loadingStatus == LoadingStatus.FAIL) {
        Toast.makeText(context, "Failed to update club details", Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        ClubUpdateScreen(
            name = uiState.name,
            onChangeName = viewModel::changeName,
            abbreviation = uiState.abbreviation,
            onChangeAbbreviation = viewModel::changeAbbreviation,
            description = uiState.description,
            onChangeDescription = viewModel::changeDescription,
            startDate = uiState.startedOn ?: LocalDate.now(),
            onChangeStartDate = viewModel::changeStartDate,
            country = uiState.country,
            onChangeCountry = viewModel::changeCountry,
            county = uiState.county,
            onChangeCounty = viewModel::changeCounty,
            town = uiState.town,
            onChangeTown = viewModel::changeTown,
            buttonEnabled = uiState.buttonEnabled,
            loadingStatus = uiState.loadingStatus,
            onUpdateClubDetails = viewModel::updateClubDetails,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClubUpdateScreen(
    name: String,
    onChangeName: (name: String) -> Unit,
    abbreviation: String,
    onChangeAbbreviation: (abbr: String) -> Unit,
    description: String,
    onChangeDescription: (desc: String) -> Unit,
    startDate: LocalDate,
    onChangeStartDate: (date: LocalDate) -> Unit,
    county: String,
    onChangeCounty: (county: String) -> Unit,
    country: String,
    onChangeCountry: (country: String) -> Unit,
    town: String,
    onChangeTown: (town: String) -> Unit,
    loadingStatus: LoadingStatus,
    buttonEnabled: Boolean,
    onUpdateClubDetails: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(16.0),
                horizontal = screenWidth(16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
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
                text = "Update club details",
                fontSize = screenFontSize(16.0).sp
            )

        }

        Spacer(modifier = Modifier.height(screenHeight(16.0)))

        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Column {
                TextFieldComposable(
                    label = "Club name",
                    value = name,
                    onValueChange = onChangeName,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(8.0)))
                TextFieldComposable(
                    label = "Club abbreviation",
                    value = abbreviation,
                    onValueChange = onChangeAbbreviation,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(8.0)))
                TextFieldComposable(
                    label = "Club description",
                    value = description,
                    onValueChange = onChangeDescription,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(8.0)))
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
                            text = dateFormatter.format(startDate),
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    }
                    DatePicker(date = startDate, onChangeDate = onChangeStartDate)
                }
                Spacer(modifier = Modifier.height(screenHeight(8.0)))
                TextFieldComposable(
                    label = "Club country",
                    value = country,
                    onValueChange = onChangeCountry,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(8.0)))
                TextFieldComposable(
                    label = "Club county",
                    value = county,
                    onValueChange = onChangeCounty,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(8.0)))
                TextFieldComposable(
                    label = "Club town",
                    value = town,
                    onValueChange = onChangeTown,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(8.0)))

            }
            
        }

        Button(
            enabled = buttonEnabled && loadingStatus != LoadingStatus.LOADING,
            onClick = onUpdateClubDetails,
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
                    text = "Update club details",
                    fontSize = screenFontSize(14.0).sp
                )
            }


        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubUpdateScreenPreview(
    modifier: Modifier = Modifier
) {
    LigiopenadminTheme {
        ClubUpdateScreen(
            startDate = LocalDate.now(),
            onChangeStartDate = {},
            buttonEnabled = false,
            loadingStatus = LoadingStatus.INITIAL,
            onUpdateClubDetails = {},
            name = "",
            onChangeName = {},
            abbreviation = "",
            onChangeAbbreviation = {},
            description = "",
            onChangeDescription = {},
            country = "",
            onChangeCountry = {},
            county = "",
            onChangeCounty = {},
            town = "",
            onChangeTown = {},
            navigateToPreviousScreen = {}
        )
    }
}

