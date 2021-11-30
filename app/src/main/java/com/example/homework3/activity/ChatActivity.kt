package com.example.homework3.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework3.R
import com.example.homework3.adapter.ChatAdapter
import com.example.homework3.model.Chat
import com.example.homework3.util.getUserPref
import com.example.homework3.util.logi
import com.utsman.rmqa.Rmqa
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {

    private val chats: MutableList<Chat> = mutableListOf()
    private lateinit var chatAdapter: ChatAdapter
    private val CHANNEL_ID = "channel_id_01"
    private val notificationId = 101

    private val receiverChat = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val user = intent.getStringExtra("user") as String
            val body = intent.getStringExtra("body") as String
            val time = intent.getLongExtra("time", 0)

            val mainChatList: RecyclerView = findViewById(R.id.main_chat_list)

            val chat = Chat(user,body,time)
            chatAdapter.addChat(chat)
            mainChatList.smoothScrollToPosition(1)
            logi(body)
            sendNotification(user, body)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setupRegisterBroadcastReceiver()
        setupRecyclerView()

        val btnChatNow: Button = findViewById(R.id.btn_chat_now)
        val inputChat: EditText = findViewById(R.id.input_chat)

        val user = getUserPref()
        btnChatNow.setOnClickListener {
            val body = inputChat.text.toString()
            val data = JSONObject()
            data.put("time", System.currentTimeMillis())
            data.put("body", body)

            Rmqa.publish("chat", user, data)

            inputChat.setText("")
        }
        createNotificationChannel()
    }

    private fun setupRegisterBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("message_coming")
        registerReceiver(receiverChat, intentFilter)
    }

    private fun setupRecyclerView() {
        val mainChatList: RecyclerView = findViewById(R.id.main_chat_list)
        chatAdapter = ChatAdapter(this, chats)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        mainChatList.layoutManager = layoutManager
        mainChatList.adapter = chatAdapter
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notificationTitle"
            val descText = "notificationDesc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description= descText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(user: String, body: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(user)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiverChat)
    }
}
