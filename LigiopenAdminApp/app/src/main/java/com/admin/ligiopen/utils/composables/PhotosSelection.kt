package com.admin.ligiopen.utils.composables

import android.net.Uri
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import com.admin.ligiopen.R
import com.admin.ligiopen.utils.screenHeight
import com.admin.ligiopen.utils.screenWidth

@Composable
fun PhotosSelection(
    onUploadPhoto: (image: Uri) -> Unit,
    onRemovePhoto: (index: Int) -> Unit,
    photos: List<Uri>,
    modifier: Modifier = Modifier
) {
    val images = remember { mutableStateListOf<Uri>() }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = {uriList ->
            if(uriList.isNotEmpty()) {
                for(uri in uriList) {
                    onUploadPhoto(uri)
                }
            }

        }
    )

    Column {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            photos.forEachIndexed { index, uri ->
                Row {
                    Image(
                        rememberImagePainter(data = uri),
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
        }
        Spacer(modifier = Modifier.height(screenWidth(x = 20.0)))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .border(
                    width = screenWidth(x = 1.0),
                    color = Color.Gray,
                    shape = RoundedCornerShape(screenWidth(x = 10.0))
                )
                .clickable {
                    galleryLauncher.launch("image/*")
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(screenWidth(x = 20.0))

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.photo_upload),
                        contentDescription = null
                    )
                    if(images.isEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Click to upload images"
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 3.0)))
                            Text(
                                text = "*",
                                color = Color.Red
                            )
                        }
                    } else {
                        Text(
                            text = "Click to upload images"
                        )
                    }

                }
            }
        }
    }
}