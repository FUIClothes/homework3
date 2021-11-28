package com.example.homework3.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework3.R
import com.example.homework3.adapter.ChatAdapter
import com.example.homework3.model.Chat
import com.example.homework3.util.getUserPref
import com.example.homework3.util.logi
import com.utsman.rmqa.Rmqa
import org.json.JSONObject
import java.nio.file.Files.size


class ChatActivity : AppCompatActivity() {

    private val chats: MutableList<Chat> = mutableListOf()
    private lateinit var chatAdapter: ChatAdapter

    private val receiverChat = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val user = intent.getStringExtra("user") as String
            val body = intent.getStringExtra("body") as String
            val time = intent.getLongExtra("time", 0)

            val main_chat_list: RecyclerView = findViewById(R.id.main_chat_list)
            val chat = Chat(user,body,time)
            chatAdapter.addChat(chat)
            main_chat_list.smoothScrollTopPosition(Chat.size)
            logi(body)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setupRegisterBroadcastReceiver()
        setupRecyclerView()

        val btn_chat_now: Button = findViewById(R.id.btn_chat_now)
        val input_chat: EditText = findViewById(R.id.input_chat)

        val user = getUserPref()
        btn_chat_now.setOnClickListener {
            val body = input_chat.text.toString()
            val data = JSONObject()
            data.put("time", System.currentTimeMillis())
            data.put("body", body)

            Rmqa.publish("chat", user, data)

            input_chat.setText("")
        }
    }

    private fun setupRegisterBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("message_coming")
        registerReceiver(receiverChat, intentFilter)
    }

    private fun setupRecyclerView() {
        val main_chat_list: RecyclerView = findViewById(R.id.main_chat_list)
        chatAdapter = ChatAdapter(this, chats)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        main_chat_list.layoutManager = layoutManager
        main_chat_list.adapter = chatAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiverChat)
    }
}