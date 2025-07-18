package com.jabulani.ligiopen.ui.auth.login

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.ui.auth.registration.RegistrationScreenDestination
import com.jabulani.ligiopen.ui.auth.registration.SoccerPasswordField
import com.jabulani.ligiopen.ui.auth.registration.SoccerTextField
import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.ui.theme.backgroundDark
import com.jabulani.ligiopen.ui.theme.backgroundLight
import com.jabulani.ligiopen.ui.theme.onPrimaryDark
import com.jabulani.ligiopen.ui.theme.onPrimaryLight
import com.jabulani.ligiopen.ui.theme.primaryDark
import com.jabulani.ligiopen.ui.theme.primaryLight
import com.jabulani.ligiopen.ui.theme.secondaryDark
import com.jabulani.ligiopen.ui.theme.secondaryLight
import com.jabulani.ligiopen.utils.reusables.composables.PasswordFieldComposable
import com.jabulani.ligiopen.utils.reusables.composables.TextFieldComposable
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth

object LoginScreenDestination : AppNavigation {
    override val title: String = "Login screen"
    override val route: String = "login-screen"
    val email: String = "email"
    val password: String = "password"
    val routeWithArgs: String = "$route/{$email}/{$password}"
}
@Composable
fun LoginScreenComposable(
    navigateToMainScreen: () -> Unit,
    navigateToRegistrationScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    val viewModel: LoginViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.loginStatus == LoginStatus.SUCCESS) {
        navigateToMainScreen()
        viewModel.resetStatus()
    } else if(uiState.loginStatus == LoginStatus.FAIL) {
        Toast.makeText(context, uiState.loginMessage, Toast.LENGTH_LONG).show()
        viewModel.resetStatus()
    }

    BackHandler(onBack = {
        activity.finish()
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .safeDrawingPadding()
        ) {
            LoginScreen(
                darkMode = false,
                email = uiState.email,
                password = uiState.password,
                isButtonEnabled = uiState.isButtonEnabled,
                onChangeEmail = {
                    viewModel.updateEmail(it)
                },
                onChangePassword = {
                    viewModel.updatePassword(it)
                },
                onLoginUser = viewModel::loginUser,
                navigateToRegistrationScreen = navigateToRegistrationScreen,
                loginStatus = uiState.loginStatus
            )
        }
    }
}

@Composable
fun LoginScreen(
    email: String,
    onChangeEmail: (email: String) -> Unit,
    password: String,
    onChangePassword: (password: String) -> Unit,
    isButtonEnabled: Boolean,
    onLoginUser: () -> Unit,
    navigateToRegistrationScreen: () -> Unit,
    loginStatus: LoginStatus,
    darkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryColor = if (darkMode) primaryDark else primaryLight
    val onPrimaryColor = if (darkMode) onPrimaryDark else onPrimaryLight
    val secondaryColor = if (darkMode) secondaryDark else secondaryLight
    val background = if (darkMode) backgroundDark else backgroundLight

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.1f),
                        background
                    )
                )
            )
    ) {
        // Soccer field background elements
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = primaryColor.copy(alpha = 0.05f),
                center = Offset(size.width * 0.2f, size.height * 0.2f),
                radius = size.width * 0.3f
            )
            drawCircle(
                color = secondaryColor.copy(alpha = 0.05f),
                center = Offset(size.width * 0.8f, size.height * 0.7f),
                radius = size.width * 0.4f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header with soccer badge effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = 0.8f),
                                    secondaryColor.copy(alpha = 0.8f)
                                )
                            )
                        )
                        .border(
                            width = 2.dp,
                            color = onPrimaryColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ligiopen_icon),
                        contentDescription = null,
                        tint = onPrimaryColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "WELCOME BACK",
                        color = onPrimaryColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            shadow = Shadow(
                                color = if (darkMode) Color.Black else Color.DarkGray,
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sign in to access your account",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form fields with stadium-like appearance
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .border(
                        width = 1.dp,
                        color = primaryColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                SoccerTextField(
                    label = "Email",
                    value = email,
                    onValueChange = onChangeEmail,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = R.drawable.email,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                SoccerPasswordField(
                    label = "Password",
                    value = password,
                    onValueChange = onChangePassword,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Forgot password link
                Text(
                    text = "Forgot Password?",
                    color = primaryColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { /* Handle forgot password */ }
                        .padding(end = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login button with soccer ball effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = if (isButtonEnabled) listOf(
                                primaryColor,
                                secondaryColor
                            ) else listOf(
                                Color.Gray.copy(alpha = 0.5f),
                                Color.DarkGray.copy(alpha = 0.5f)
                            )
                        )
                    )
                    .clickable(
                        enabled = isButtonEnabled,
                        onClick = onLoginUser
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                onPrimaryColor.copy(alpha = 0.3f),
                                onPrimaryColor.copy(alpha = 0.7f),
                                onPrimaryColor.copy(alpha = 0.3f)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (loginStatus == LoginStatus.LOADING) {
                    CircularProgressIndicator(
                        color = onPrimaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ball),
                            contentDescription = null,
                            tint = onPrimaryColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SIGN IN".uppercase(),
                            color = onPrimaryColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge.copy(
                                shadow = Shadow(
                                    color = if (darkMode) Color.Black else Color.DarkGray,
                                    offset = Offset(1f, 1f),
                                    blurRadius = 2f
                                )
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Registration prompt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New to Ligi Open? ",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Text(
                    text = "REGISTER",
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { navigateToRegistrationScreen() },
                    style = MaterialTheme.typography.labelLarge.copy(
                        shadow = Shadow(
                            color = if (darkMode) Color.Black.copy(alpha = 0.3f)
                            else Color.DarkGray.copy(alpha = 0.3f),
                            offset = Offset(1f, 1f),
                            blurRadius = 2f
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Reuse the same SoccerTextField and SoccerPasswordField composables from the registration screen


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LigiopenTheme {
        LoginScreen(
            darkMode = false,
            email = "",
            password = "",
            onChangeEmail = {},
            onChangePassword = {},
            isButtonEnabled = false,
            loginStatus = LoginStatus.INITIAL,
            onLoginUser = {},
            navigateToRegistrationScreen = {}
        )
    }
}