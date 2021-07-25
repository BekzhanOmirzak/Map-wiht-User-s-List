package com.example.mysocialmediaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.models.ChatMessage

class ChatMessageAdapter : RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder>() {

    private val chat_messages = mutableListOf<ChatMessage>()

    fun updateList(list: List<ChatMessage>) {
        chat_messages.clear()
        chat_messages.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_message_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.message.setText(chat_messages.get(position).message)
        holder.user_name.setText(chat_messages.get(position).user.login)
    }

    override fun getItemCount() = chat_messages.size


    inner class MessageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val message = v.findViewById<TextView>(R.id.txtmessage)
        val user_name = v.findViewById<TextView>(R.id.send_name)
    }


}