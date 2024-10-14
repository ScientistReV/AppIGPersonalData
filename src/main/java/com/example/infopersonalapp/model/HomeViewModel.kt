package com.example.infopersonalapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.infopersonalapp.data.MediaItem
import com.example.infopersonalapp.data.UserMediaResponse
import com.example.infopersonalapp.data.UserProfile
import com.example.infopersonalapp.network.InstagramApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel(private val accessToken: String) : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _userMedia = MutableStateFlow<List<MediaItem>>(emptyList())
    val userMedia: StateFlow<List<MediaItem>> = _userMedia

    private val service: InstagramApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://graph.instagram.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(InstagramApiService::class.java)

        fetchUserProfile()
        fetchUserMedia()
    }

    private fun fetchUserProfile() {
        service.getUserProfile(accessToken = accessToken)
            .enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                    if (response.isSuccessful) {
                        _userProfile.value = response.body()
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    // Tratar erro
                }
            })
    }

    private fun fetchUserMedia() {
        service.getUserMedia(accessToken = accessToken)
            .enqueue(object : Callback<UserMediaResponse> {
                override fun onResponse(call: Call<UserMediaResponse>, response: Response<UserMediaResponse>) {
                    if (response.isSuccessful) {
                        _userMedia.value = response.body()?.data ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<UserMediaResponse>, t: Throwable) {
                    // Tratar erro
                }
            })
    }
}

class HomeViewModelFactory(private val accessToken: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(accessToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
