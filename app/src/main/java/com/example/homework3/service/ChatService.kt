package com.example.homework3.service

import android.app.Service
import android.app.Service.START_STICKY
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.homework3.util.getUserPref
import com.example.homework3.util.logi
import com.utsman.rmqa.Rmqa
import com.utsman.rmqa.RmqaConnection

class  ChatService : Service() {


    private var rmqaConnection: RmqaConnection? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        rmqaConnection = RmqaConnection.Builder(this)
            .setServer("gerbil.rmq.cloudamqp.com")
            .setUsername("ibvyjuin")
            .setPassword("5i3SgKKfedAlChV0bzNm9UBF1XxcJAVz")
            .setVhost("ibvyjuin")
            .setExchangeName("chat")
            .setConnectionName("connection")
            .setRoutingKey("route_chat")
            .setAutoClearQueue(false)
            .build()

        val user = getUserPref()

        Rmqa.connect(rmqaConnection, user){ senderId, data ->
            logi("$data from $senderId")

            val i = Intent("message_coming")
            i.putExtra("user", senderId)
            i.putExtra("body", data["body"] as String)
            i.putExtra("time", data["time"] as Long)
            sendBroadcast(i)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Rmqa.disconnect(rmqaConnection)
    }
}