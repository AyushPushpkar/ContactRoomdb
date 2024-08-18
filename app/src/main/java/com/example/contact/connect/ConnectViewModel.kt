package com.example.contact.connect

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class ConnectViewModel(
    private val app: Application
) : AndroidViewModel(app) {

    var state by mutableStateOf(ConnectState())
        private set

    fun onAction(action: ConnectAction){
        when(action){
            ConnectAction.onConnectClick -> {
                connectToRoom()
            }
            is ConnectAction.onNameChange -> {
                state = state.copy(name = action.name)
            }
        }
    }

    private fun connectToRoom(){
        state = state.copy(errorMessage = null)
        if(state.name.isBlank()){
            state = state.copy(
                errorMessage = "The name can't be empty"
            )
            return
        }

        (app as VideoCallingApp) .initVideoClient(state.name)

        state = state.copy(isConnected = true)
    }
}