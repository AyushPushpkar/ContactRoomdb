package com.example.contact.di

import com.example.contact.connect.ConnectViewModel
import com.example.contact.connect.VideoCallingApp
import com.example.contact.video.VideoCallViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    factory {
        val app = androidContext().applicationContext as VideoCallingApp
        app.client
    }

    viewModelOf(::ConnectViewModel)
    viewModelOf(::VideoCallViewModel)

}