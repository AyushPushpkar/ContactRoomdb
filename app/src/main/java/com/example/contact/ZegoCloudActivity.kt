package com.example.contact

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contact.databinding.ActivityZegoCloudBinding
import com.zegocloud.uikit.service.defines.ZegoUIKitUser

class ZegoCloudActivity : AppCompatActivity() {

    private val binding : ActivityZegoCloudBinding by lazy {
        ActivityZegoCloudBinding.inflate(layoutInflater)
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

        val myuserId = intent.getStringExtra("userId")
        binding.useridtext.text = "Hi $myuserId , \n Whom you want to call ?"

        binding.etuserid.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){
                
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val targetUserID = binding.etuserid.text.toString()

                binding.linearicon.visibility = View.VISIBLE

                if(targetUserID.isNotEmpty()){
                    startVideoCall(targetUserID)
                    startVoiceCall(targetUserID)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


    }

    private fun startVideoCall(targetUserID : String) {
        val targetUserName = targetUserID
        var videocallbtn = binding.vcbtn

        videocallbtn.setIsVideoCall(true)
        videocallbtn.resourceID = "zego_uikit_call"
        videocallbtn.setInvitees(listOf(ZegoUIKitUser(targetUserID,targetUserName)))
    }

    private fun startVoiceCall(targetUserID : String) {
        val targetUserName = targetUserID
        var voicecallbtn = binding.voicebtn

        voicecallbtn.setIsVideoCall(false)
        voicecallbtn.resourceID = "zego_uikit_call"
        voicecallbtn.setInvitees(listOf(ZegoUIKitUser(targetUserID,targetUserName)))
    }
}