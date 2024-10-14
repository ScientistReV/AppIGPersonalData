package com.example.infopersonalapp.utils

import android.net.Uri

object Constants {
    const val APP_ID = "2257009144635433"
    const val APP_SECRET = "15f6eb2b6fa08cbe7eba40b05525483d" // Mantenha isso seguro
    const val REDIRECT_URI = "https://github.com/ScientistReV"
    val REDIRECT_URI_ENCODED = Uri.encode(REDIRECT_URI)
}
