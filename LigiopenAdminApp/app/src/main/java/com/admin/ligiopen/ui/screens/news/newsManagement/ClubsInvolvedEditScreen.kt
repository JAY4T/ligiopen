package com.admin.ligiopen.ui.screens.news.newsManagement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.clubs
import com.admin.ligiopen.ui.theme.LigiopenadminTheme
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth

@Composable
fun ClubsInvolvedEditScreenComposable(
    modifier: Modifier = Modifier
) {

}

@Composable
fun ClubsInvolvedEditScreen(
    selectedClubIds: List<Int>,
    onSelectClub: (clubId: Int) -> Unit,
    onRemoveClub: (clubId: Int) -> Unit,
    clubs: List<ClubData>,
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
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            Text(
                text = "News: update",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Select / Deselect the club(s) involved",
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            LazyColumn {
                items(clubs) { club ->
                    SelectableClubCard(
                        club = club,
                        selectedClubIds = selectedClubIds,
                        onSelectClub = onSelectClub,
                        onRemoveClub = onRemoveClub
                    )
                }
            }
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Update",
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubsInvolvedEditScreenPreview() {
    LigiopenadminTheme {
        ClubsInvolvedEditScreen(
            clubs = clubs,
            selectedClubIds = listOf(2, 5),
            onSelectClub = {},
            onRemoveClub = {}
        )
    }
}