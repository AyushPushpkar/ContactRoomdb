package com.example.contact.connect

import android.app.Application
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType

//live as long as application
class VideoCallingApp : Application() {

    private var currentName : String? = null
    var client : StreamVideo? = null

    fun initVideoClient(username : String){
        if(client == null || currentName != username){

            StreamVideo.removeClient()
            currentName = username

            client = StreamVideoBuilder(
                context = this ,
                apiKey = "9a2spndhgc4m" ,
                user = User(
                    id = username ,
                    name = username ,
                    type = UserType.Guest
                )
            ).build()

        }
    }
}