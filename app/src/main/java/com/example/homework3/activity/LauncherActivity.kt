package com.example.homework3.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.homework3.R
import com.example.homework3.service.ChatService
import com.example.homework3.util.ProgressHelper
import com.example.homework3.util.saveUserPref



class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val progressHelper = ProgressHelper()

        val btn_login: Button = findViewById(R.id.btn_login)
        val input_username: EditText = findViewById(R.id.input_username)
        btn_login.setOnClickListener {
            val username = input_username.text.toString()
            saveUserPref(username)

            progressHelper.showProgressDialog(this)

            @Suppress("DEPRECATION")
            Handler().postDelayed({
                val chatService = Intent(this, ChatService::class.java)
                startService(chatService)

                progressHelper.hideProgressDialog()
                startActivity(Intent(this, ChatActivity::class.java))
                finish()
            }, 2000)
        }
    }
}