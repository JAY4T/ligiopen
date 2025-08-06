package com.admin.ligiopen.ui.screens.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth

@Composable
fun ContentReviewDashboardScreenComposable(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        ContentReviewDashboardScreen()
    }

}

@Composable
fun ContentReviewDashboardScreen(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = screenWidth(16.0),
                vertical = screenHeight(16.0)
            )
    ) {
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Teams"
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(8.0)))
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "News"
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(8.0)))
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Match highlights"
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(8.0)))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContentReviewDashboardScreenPreview() {
    LigiopenadminTheme {
        ContentReviewDashboardScreen()
    }
}