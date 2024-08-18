package com.example.contact

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contact.connect.ConnectScreen
import com.example.contact.connect.ConnectViewModel
import com.example.contact.ui.theme.ContactTheme
import com.example.contact.video.CallState
import com.example.contact.video.VideoCallScreen
import com.example.contact.video.VideoCallViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

class StreamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = connectRoute ,
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable<connectRoute>{
                            val viewModel = koinViewModel<ConnectViewModel>()
                            val state = viewModel.state
                            
                            LaunchedEffect(key1 = state.isConnected) {
                                if(state.isConnected){
                                    navController.navigate(videoCallRoute){
                                        popUpTo(connectRoute){  // can't hit back to go back there
                                            inclusive = true
                                        }
                                    }
                                }
                            }

                            ConnectScreen(state = state , onAction = viewModel::onAction)
                        }
                        composable<videoCallRoute>{
                            val viewModel = koinViewModel<VideoCallViewModel>()
                            val state = viewModel.state

                            LaunchedEffect(key1 = state.callState) {
                                if(state.callState == CallState.ENDED){
                                    navController.navigate(connectRoute){
                                        popUpTo(connectRoute){  // can't hit back to go back there
                                            inclusive = true
                                        }
                                    }
                                }
                            }

                            VideoCallScreen(state = state , onAction = viewModel::onAction)
                        }
                    }
                }
            }
        }
    }
}

@Serializable
data object connectRoute

@Serializable
data object videoCallRoute

