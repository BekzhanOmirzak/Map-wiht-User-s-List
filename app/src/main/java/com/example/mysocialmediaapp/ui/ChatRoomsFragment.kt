package com.example.mysocialmediaapp.ui

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.models.ChatRoom
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatRoomsFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var list_view: ListView
    private var chatRooms = listOf<ChatRoom>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chat_rooms, container, false)
        fab = view.findViewById(R.id.fab)
        list_view = view.findViewById(R.id.list_view)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {

            val dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.create_chat_room_dialog)

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

            val btnNo = dialog.findViewById<Button>(R.id.btn_no)
            val btnYes = dialog.findViewById<Button>(R.id.btn_yes)
            dialog.create()
            dialog.show()

            btnNo.setOnClickListener {
                dialog.dismiss()
            }

            btnYes.setOnClickListener {
                val edt_name = dialog.findViewById<EditText>(R.id.edt_room_name).text.toString()
                SharedPreferences.createChatRooms(edt_name)
                createChatRooms()
                dialog.dismiss()
            }
        }
        createChatRooms()

    }

    private fun createChatRooms() {
        SharedPreferences.returnAllAllChatRooms {
            chatRooms = it
            val names = mutableListOf<String>()
            for (k in it) {
                names.add(k.name)
            }
            val adapter =
                ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, names)
            list_view.setAdapter(adapter)
        }

        list_view.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val chat_room = chatRooms[position]

                val messageFragment = FragmentChatMessage()
                val bundle = Bundle()
                bundle.putString("chat_id", chat_room.uid)
                messageFragment.arguments = bundle
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.container, messageFragment).commit()

            }
        })
        list_view.setOnItemLongClickListener { parent, view, position, id ->
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListMapFragment()).commit()
            return@setOnItemLongClickListener false
        }

    }
}