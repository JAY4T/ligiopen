package com.jabulani.ligiopen.ui.inapp.standings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.jabulani.ligiopen.R
import com.jabulani.ligiopen.ui.inapp.home.HomeScreenTab
import com.jabulani.ligiopen.ui.theme.LigiopenTheme
import com.jabulani.ligiopen.utils.screenFontSize
import com.jabulani.ligiopen.utils.screenWidth

@Composable
fun StandingsScreenComposable() {
    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        StandingsScreen()
    }
}

@Composable
fun StandingsScreen(
    modifier: Modifier = Modifier
) {
    Column {
        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = screenWidth(x = 8.0)
                )

        ) {
            Icon(
                painter = painterResource(id = R.drawable.ligiopen_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(screenWidth(x = 56.0))
            )
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Text(
                text = HomeScreenTab.STANDINGS.name.lowercase().replaceFirstChar { first -> first.uppercase() },
                fontSize = screenFontSize(x = 26.0).sp,
                fontWeight = FontWeight.W900
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                enabled = false,
                onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.more),
                    contentDescription = "More"
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = "Standings")
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StandingsScreenPreview() {
    LigiopenTheme {
        StandingsScreen()
    }
}