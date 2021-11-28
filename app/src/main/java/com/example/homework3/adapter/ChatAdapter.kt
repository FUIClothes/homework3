package com.example.homework3.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homework3.R
import com.example.homework3.model.Chat
import com.example.homework3.util.getUserPref
import java.text.SimpleDateFormat

class ChatAdapter(private val context: Context, private val chats: MutableList<Chat>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = when (viewType) {
            SENDER -> LayoutInflater.from(context).inflate(
                R.layout.item_sender, parent, false)
            else -> LayoutInflater.from(context).inflate(R.layout.item_receiver, parent, false)
        }
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        holder.bind(chat)
    }

    override fun getItemViewType(position: Int): Int {
        val me = context.getUserPref()
        return if (chats[position].user == me) {
            SENDER
        } else {
            RECEIVER
        }
    }

    fun addChat(chat: Chat) {
        chats.add(chat)
        notifyDataSetChanged()
    }

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view){
        @SuppressLint("SimpleDateFormat")
        fun bind(chat: Chat) = itemView.run {
            val text_user: TextView = findViewById(R.id.input_username)
            val text_body: TextView = findViewById(R.id.text_body)
            val text_date: TextView = findViewById(R.id.text_date)
            text_user.text = chat.user
            text_body.text = chat.body

            val sdf = SimpleDateFormat("dd-MMMM-yyyy / hh:mm:ss")
            val date = sdf.format(chat.time)
            text_date.text = date
        }

}
    companion object {
        private const val SENDER = 0
        private const val RECEIVER = 1
    }
}
