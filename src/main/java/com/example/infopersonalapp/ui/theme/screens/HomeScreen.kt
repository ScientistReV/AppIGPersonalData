package com.example.infopersonalapp.ui.theme.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.infopersonalapp.model.HomeViewModel
import com.example.infopersonalapp.model.HomeViewModelFactory
import com.example.infopersonalapp.model.MediaItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(accessToken: String) {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(accessToken))

    val userProfile by viewModel.userProfile.collectAsState()
    val userMedia by viewModel.userMedia.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Bem-vindo, ${userProfile?.username ?: ""}") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(userMedia) { mediaItem ->
                MediaItemViewModel(mediaItem)
            }
        }
    }
}
