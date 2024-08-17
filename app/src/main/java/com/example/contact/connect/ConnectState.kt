package com.example.contact.connect

data class ConnectState(
    val name : String = "" ,
    val isConnecting : Boolean = false ,
    val errorMessage : String? = null
)
