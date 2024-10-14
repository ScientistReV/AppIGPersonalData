package com.example.infopersonalapp.data

import com.google.gson.annotations.SerializedName

data class UserProfile(
    val id: String,
    val username: String,
    @SerializedName("account_type") val accountType: String
)