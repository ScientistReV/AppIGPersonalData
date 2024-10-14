package com.example.infopersonalapp.ui.theme.screens

import com.example.infopersonalapp.data.AccessTokenResponse
import com.example.infopersonalapp.network.InstagramApiService
import com.example.infopersonalapp.utils.Constants
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.web.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun AuthScreen(onAuthSuccess: (String) -> Unit) {
    val authUrl = "https://api.instagram.com/oauth/authorize" +
            "?client_id=${Constants.APP_ID}" +
            "&redirect_uri=${Constants.REDIRECT_URI_ENCODED}" +
            "&scope=user_profile,user_media" +
            "&response_type=code"

    val webViewState = rememberWebViewState(url = authUrl)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    // WebView Navigator to control WebView actions
    val webViewNavigator = rememberWebViewNavigator()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = { Text("Autenticação Instagram") })
        }
    ) { paddingValues ->
        if (isLoading) {
            // Exibir indicador de carregamento enquanto obtém o access_token
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            WebView(
                state = webViewState,
                navigator = webViewNavigator,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onCreated = { webView ->
                    webView.settings.javaScriptEnabled = true
                }
            )
        }
    }

    // Observa mudanças de URL no estado da WebView
    LaunchedEffect(webViewState.content.getCurrentUrl()) {
        val currentUrl = webViewState.content.getCurrentUrl()
        if (currentUrl != null && currentUrl.startsWith(Constants.REDIRECT_URI)) {
            // Intercepta o redirecionamento de autenticação
            val uri = android.net.Uri.parse(currentUrl)
            val code = uri.getQueryParameter("code")
            if (code != null) {
                isLoading = true
                // Trocar o código pelo token de acesso
                exchangeCodeForToken(code, onAuthSuccess, scaffoldState) {
                    isLoading = false
                }
            } else if (uri.getQueryParameter("error") != null) {
                // Tratamento de erro
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Erro na autenticação")
                }
            }
        }
    }
}

fun exchangeCodeForToken(
    code: String,
    onAuthSuccess: (String) -> Unit,
    scaffoldState: ScaffoldState,
    onComplete: () -> Unit
) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.instagram.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(InstagramApiService::class.java)

    service.getAccessToken(
        clientId = Constants.APP_ID,
        clientSecret = Constants.APP_SECRET,
        grantType = "authorization_code",
        redirectUri = Constants.REDIRECT_URI,
        code = code
    ).enqueue(object : Callback<AccessTokenResponse> {
        override fun onResponse(
            call: Call<AccessTokenResponse>,
            response: Response<AccessTokenResponse>
        ) {
            if (response.isSuccessful) {
                val accessToken = response.body()?.accessToken
                if (accessToken != null) {
                    onAuthSuccess(accessToken)
                } else {
                    // Atualizar a interface para exibir o Snackbar de falha
                    showSnackbar(scaffoldState, "Falha ao obter token de acesso")
                }
            } else {
                // Atualizar a interface para exibir o Snackbar de resposta inválida
                showSnackbar(scaffoldState, "Resposta inválida do servidor")
            }
            onComplete()
        }

        override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
            // Atualizar a interface para exibir o Snackbar de erro de rede
            showSnackbar(scaffoldState, "Erro na rede: ${t.message}")
            onComplete()
        }
    })
}

// Função auxiliar para exibir o Snackbar
fun showSnackbar(scaffoldState: ScaffoldState, message: String) {
    GlobalScope.launch {
        scaffoldState.snackbarHostState.showSnackbar(message)
    }
}


