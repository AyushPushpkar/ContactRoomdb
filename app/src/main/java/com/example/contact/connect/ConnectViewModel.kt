package com.example.contact.connect

import android.app.Application
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class ConnectViewModel(
    private val app: Application
) : AndroidViewModel(app) {

    var state = mutableStateOf(ConnectState())
        private set

    fun onAction(action: ConnectAction){
        when(action){
            ConnectAction.onConnectClick -> {
                connectToRoom()
            }
            is ConnectAction.onNameChange -> {
                state.value = state.value.copy(name = action.name)
            }
        }
    }

    private fun connectToRoom(){
        state.value = state.value.copy(errorMessage = null)
        if(state.value.name.isBlank()){
            state.value = state.value.copy(
                errorMessage = "The name can't be empty"
            )
            return
        }

        (app as VideoCallingApp) .initVideoClient(state.value.name)

        state.value = state.value.copy(isConnecting = true)
    }
}