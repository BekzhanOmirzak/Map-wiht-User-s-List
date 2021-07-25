package com.example.mysocialmediaapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.adapter.ChatMessageAdapter
import com.example.mysocialmediaapp.models.ChatMessage
import com.example.mysocialmediaapp.util.SharedPreferences

class FragmentChatMessage : Fragment() {

    private lateinit var rec_view: RecyclerView
    private lateinit var edt_message: EditText
    private lateinit var icon_send: ImageView
    private lateinit var adapter: ChatMessageAdapter
    private val TAG = "FragmentChatMessage"

    interface onSendMessage {
        fun onSendMessage(chat_id: String, message: ChatMessage)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.room_messages_fragment, container, false)
        rec_view = view.findViewById(R.id.rec_view)
        edt_message = view.findViewById(R.id.editText)
        icon_send = view.findViewById(R.id.send_icon)
        adapter = ChatMessageAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val chat_id = bundle?.getString("chat_id")

        icon_send.setOnClickListener {
            val mess = edt_message.text.toString()
            val time = System.currentTimeMillis()
            val message = ChatMessage(time, mess, SharedPreferences.getStoredUserDetails()!!)
            try {
                val onSend = activity as onSendMessage
                onSend.onSendMessage(chat_id!!, message)
            } catch (e: Exception) {
                Log.i(TAG, "onViewCreated: class cast exception")
            }
            edt_message.setText("")
            populateWithMessages(chat_id!!)
        }

        populateWithMessages(chat_id!!)

    }

    private fun populateWithMessages(chat_id: String) {

        rec_view.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireActivity())
        }
        SharedPreferences.returnAllMessages(chat_id) {
            Log.i(TAG, "populateWithMessages: ${it.size}")
            adapter.updateList(it)
        }


    }

}