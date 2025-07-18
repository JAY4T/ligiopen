package com.jabulani.ligiopen.ui.start

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jabulani.ligiopen.AppViewModelFactory
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.ui.nav.AppNavigation
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.ui.theme.onPrimaryDark
import com.jabulani.ligiopen.ui.theme.onPrimaryLight
import com.jabulani.ligiopen.ui.theme.primaryDark
import com.jabulani.ligiopen.ui.theme.primaryLight
import com.jabulani.ligiopen.ui.theme.secondaryDark
import com.jabulani.ligiopen.ui.theme.secondaryLight
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenHeight
import com.jabulani.ligiopen.utils.screenWidth
import kotlinx.coroutines.delay

object SplashScreenDestination : AppNavigation {
    override val title: String = "Splash screen"
    override val route: String = "splash-screen"
}

@Composable
fun SplashScreenComposable(
    navigateToLoginScreen: () -> Unit,
    navigateToRegistrationScreen: () -> Unit,
    navigateToMainScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        delay(2000)
        if(uiState.isLoading) {
            if(uiState.users.isEmpty()) {
                navigateToRegistrationScreen()
            } else {
                navigateToMainScreen()
            }
        }
        viewModel.changeLoadingStatus()
    }

    Box(
        modifier = Modifier
//            .safeDrawingPadding()
    ) {
        SplashScreen(
            darkMode = false
        )
    }
}

@Composable
fun SplashScreen(
    darkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryColor = if (darkMode) primaryDark else primaryLight
    val onPrimaryColor = if (darkMode) onPrimaryDark else onPrimaryLight
    val secondaryColor = if (darkMode) secondaryDark else secondaryLight

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.background
                    ),
                    center = Offset.Unspecified,
                    radius = 1000f
                )
            )
    ) {
        // Animated soccer field circles
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = primaryColor.copy(alpha = 0.1f),
                center = center,
                radius = size.minDimension * 0.4f,
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = secondaryColor.copy(alpha = 0.1f),
                center = center,
                radius = size.minDimension * 0.2f,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // App logo with stadium-like border
        Box(
            modifier = Modifier
                .size(screenWidth(200.0))
                .shadow(
                    elevation = screenWidth(16.0),
                    shape = CircleShape,
                    spotColor = primaryColor.copy(alpha = 0.3f)
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = screenWidth(3.0),
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            primaryColor,
                            secondaryColor,
                            primaryColor
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ligiopen_icon),
                contentDescription = "Ligi Open Logo",
                tint = onPrimaryColor,
                modifier = Modifier.size(screenWidth(120.0))
            )
        }

        // Loading indicator at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = screenHeight(64.0))
        ) {
            CircularProgressIndicator(
                color = primaryColor,
                modifier = Modifier.size(screenWidth(36.0)),
                strokeWidth = screenWidth(3.0)
            )
        }

        // App name with subtle animation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = screenHeight(120.0))
        ) {
            Text(
                text = "LIGI OPEN",
                color = onPrimaryColor,
                fontSize = screenFontSize(24.0).sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge.copy(
                    shadow = Shadow(
                        color = if (darkMode) Color.Black else Color.DarkGray,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    LigiopenTheme {
        SplashScreen(
            darkMode = false
        )
    }
}