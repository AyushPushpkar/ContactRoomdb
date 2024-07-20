package com.example.contact

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contact.databinding.ActivityLoginBinding
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig

class LoginActivity : AppCompatActivity() {

    private val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.statusBarColor = Color.BLACK

        binding.loginbutton.setOnClickListener {
            val myuserId = binding.etuserid.text.toString()

            if(myuserId.isNotEmpty()){
                intent = Intent(this,ZegoCloudActivity::class.java)
                intent.putExtra("userId",myuserId)
                startActivity(intent)

                setUpZegoUiUniit(myuserId)
            }
        }

    }

    private fun setUpZegoUiUniit(userId : String){

        val application : Application =  application // Android's application context
        val appID : Long = 1
        val appSign  =""
        val userName : String = userId

        val callInvitationConfig  =  ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallService.init(application, appID, appSign, userId , userName,callInvitationConfig);
    }

    override fun onDestroy() {
        super.onDestroy()
        ZegoUIKitPrebuiltCallService.unInit()   //stop the service
    }
}