package com.jabulani.ligiopen.ui.inapp.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreenComposable(
    navigateToHomeScreen: () -> Unit,
    navigateToLoginScreenWithArgs: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier
){

    BackHandler(onBack = navigateToHomeScreen)

    val viewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()

    var loggingOut by rememberSaveable {
        mutableStateOf(false)
    }

    var showLogoutDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(showLogoutDialog) {
        LogOutDialog(
            logginOut = loggingOut,
            onConfirm = {
                scope.launch {
                    loggingOut = true
                    val email = uiState.userAccount.email
                    val password = uiState.userAccount.password
                    viewModel.deleteUsers()
                    delay(2000)
                    navigateToLoginScreenWithArgs(email, password)
                    loggingOut = false
                }
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        ProfileScreen(
            username = uiState.userAccount.username,
            email = uiState.userAccount.email,
            onLogout = {
                showLogoutDialog = true
            }
        )
    }
}

@Composable
fun ProfileScreen(
    username: String,
    email: String,
    onLogout: () -> Unit,
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
        Card(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(screenWidth(x = 16.0))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.blank_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(screenWidth(x = 96.0))
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = username,
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = email,
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Column {
            Text(
                text = "Account Info",
                fontSize = screenFontSize(x = 22.0).sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(
                        horizontal = screenWidth(x = 16.0)
                    )
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            horizontal = screenWidth(x = 8.0),
                            vertical = screenHeight(x = 8.0)
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = username,
                        fontSize = screenFontSize(x = 14.0).sp,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Edit username"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            horizontal = screenWidth(x = 8.0),
                            vertical = screenHeight(x = 8.0)
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = email,
                        fontSize = screenFontSize(x = 14.0).sp,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Edit email"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Card(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            horizontal = screenWidth(x = 8.0),
                            vertical = screenHeight(x = 16.0)
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.password),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = "Change password",
                        fontSize = screenFontSize(x = 14.0).sp,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Change password"
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Log out",
                    fontSize = screenFontSize(x = 14.0).sp,
                )
            }
        }
    }
}

@Composable
fun LogOutDialog(
    logginOut: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Log out",
                fontSize = screenFontSize(x = 22.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to log out?",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                enabled = !logginOut,
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
        confirmButton = {
            Button(
                enabled = !logginOut,
                onClick = onConfirm
            ) {
                if(logginOut) {
                    Text(
                        text = "Logging out...",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                } else {
                    Text(
                        text = "Log out",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    LigiopenTheme {
        ProfileScreen(
            username = "Sam N",
            email = "sam@gmail.com",
            onLogout = {}
        )
    }
}