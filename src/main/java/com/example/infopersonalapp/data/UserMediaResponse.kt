package com.example.infopersonalapp.data

import com.google.gson.annotations.SerializedName

data class UserMediaResponse(
    val data: List<MediaItem>
)

data class MediaItem(
    val id: String,
    val caption: String?,
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("media_url") val mediaUrl: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?,
    val permalink: String
)
