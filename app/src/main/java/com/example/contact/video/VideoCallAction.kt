package com.example.contact.video

sealed interface VideoCallAction {

    data object onDisconnectClick : VideoCallAction
    data object joinCall : VideoCallAction
}