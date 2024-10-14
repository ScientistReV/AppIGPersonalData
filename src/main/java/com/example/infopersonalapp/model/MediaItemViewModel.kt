package com.example.infopersonalapp.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.infopersonalapp.data.MediaItem
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun MediaItemViewModel(mediaItem: MediaItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val imageUrl = if (mediaItem.mediaType == "VIDEO") {
            mediaItem.thumbnailUrl ?: ""
        } else {
            mediaItem.mediaUrl
        }

        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = mediaItem.caption ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Text(
            text = mediaItem.caption ?: "",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

