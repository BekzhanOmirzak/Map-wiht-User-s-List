package com.example.mysocialmediaapp.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.example.mysocialmediaapp.models.ChatMessage
import com.example.mysocialmediaapp.models.ChatRoom
import com.example.mysocialmediaapp.models.User
import com.example.mysocialmediaapp.models.UserLocation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object SharedPreferences {

    private const val TAG = "SharedPreferences"

    private lateinit var sharedPreferences: SharedPreferences
    val turnsType = object : TypeToken<User>() {}.type

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("db_name", MODE_PRIVATE)
    }

    fun saveUserDetails(user: User) {
        val value_str = Gson().toJson(user)
        val editor = sharedPreferences.edit()
        editor.putString("user", value_str)
        editor.apply()
    }

    fun getStoredUserDetails(): User? {
        val user_str = sharedPreferences.getString("user", "")
        if (user_str != "") {
            val user = Gson().fromJson<User>(user_str, turnsType)
            return user
        }
        return null
    }

    fun removeUserDetails() {
        sharedPreferences.edit().clear().apply()
    }

    fun getAllFollowingsLists(uid: String, myCallBack: (List<User>) -> Unit) {
        val users = mutableListOf<User>()
        val collection = FirebaseFirestore.getInstance().collection("users")
            .document(uid).collection("followings")

        collection.get().addOnCompleteListener {
            for (k in it.result!!) {
                Log.i(TAG, "getAllFollowingsLists: ${k.toObject(User::class.java)} ")
                users.add(k.toObject(User::class.java))
            }
            myCallBack(users)
        }
    }

    fun getAllFollowers(uid: String, myCallBack: (List<User>) -> Unit) {
        val followers = mutableListOf<User>()
        val collection = FirebaseFirestore.getInstance().collection("users")
            .document(uid).collection("followers")
        collection.get().addOnCompleteListener {
            for (k in it.result!!) {
                followers.add(k.toObject(User::class.java))
            }
            myCallBack(followers)
        }
    }

    fun removeParticulareUserFromThroughUnFollow(uid: String) {
        FirebaseFirestore.getInstance().collection("users")
            .document(getStoredUserDetails()!!.uid).collection("followings").document(uid).delete()
    }

    fun addUserToFollowings(user: User) {
        FirebaseFirestore.getInstance().collection("users")
            .document(getStoredUserDetails()!!.uid).collection("followings").document(user.uid)
            .set(user)
    }

    fun addUserToFollowers(uid: String) {
        val cur_user = getStoredUserDetails()!!
        FirebaseFirestore.getInstance().collection("users")
            .document(uid).collection("followers").document(cur_user.uid).set(cur_user)
    }

    fun removeUserFromFollowers(uid: String) {
        val cur_user = getStoredUserDetails()!!
        FirebaseFirestore.getInstance().collection("users")
            .document(uid).collection("followers").document(cur_user.uid).delete()

    }

    fun createChatRooms(name: String) {
        val fire_instance = FirebaseFirestore.getInstance()
        val uid_chat = fire_instance.collection("users").document().id
        Log.i(TAG, "createChatRooms: Bekzhan: $uid_chat")
        val chatRoom = ChatRoom(name)
        chatRoom.uid = uid_chat
        fire_instance.collection("chat_rooms").document(uid_chat)
            .set(chatRoom)
        fire_instance.collection("chat_rooms").document(uid_chat).collection("users")
            .document(getStoredUserDetails()?.uid.toString()).set(getStoredUserDetails()!!)
    }

    fun returnAllAllChatRooms(myCallBack: (List<ChatRoom>) -> Unit) {
        FirebaseFirestore.getInstance().collection("chat_rooms").get().addOnCompleteListener {
            val chatRooms = mutableListOf<ChatRoom>()
            for (k in it.result!!) {
                chatRooms.add(k.toObject(ChatRoom::class.java))
            }
            myCallBack(chatRooms)
        }
    }

    fun sendMessage(chat_id: String, message: ChatMessage) {
        val fire_instance = FirebaseFirestore.getInstance()
        val message_uid = fire_instance.collection("users").document().id
        message.message_id = message_uid
        FirebaseFirestore.getInstance().collection("chat_rooms").document(chat_id)
            .collection("messages").document(message_uid).set(message)
    }

    fun returnAllMessages(room_uid: String, myCallBack: (List<ChatMessage>) -> Unit) {
        val fire_instance = FirebaseFirestore.getInstance()
        fire_instance.collection("chat_rooms").document(room_uid).collection("messages")
            .orderBy("timeStamp", Query.Direction.ASCENDING).get().addOnCompleteListener {
                val chatMessages = mutableListOf<ChatMessage>()
                for (k in it.result!!) {
                    chatMessages.add(k.toObject(ChatMessage::class.java))
                }
                myCallBack(chatMessages)
            }
    }

    fun addUserLocationToServer(userLocation: UserLocation) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("user_locations").document(userLocation.user.uid).set(userLocation)
    }


}