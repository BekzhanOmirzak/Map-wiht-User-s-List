package com.example.mysocialmediaapp.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.models.User
import com.example.mysocialmediaapp.ui.MainActivity
import com.example.mysocialmediaapp.ui.PersonProfileFragment
import com.example.mysocialmediaapp.util.SharedPreferences

class Person_Adapter(val context: Context, val uid: String) :
    RecyclerView.Adapter<Person_Adapter.PersonViewHolder>() {

    val people = mutableListOf<User>()
    var user_followings = listOf<User>()

    init {
        SharedPreferences.getAllFollowingsLists(uid) {
            user_followings = it
        }
    }


    fun updatePeopleList(people: List<User>) {
        this.people.addAll(people)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.person_item, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.person_name.setText(people[position].login)

        val login = people.get(position).login

        Log.i("Adapter", "onBindViewHolder: user followings ${user_followings.size}")
        for (k in user_followings) {
            if (k.login.equals(login)) {
                holder.follow.setText("Unfollow")
            } else {
                holder.follow.setText("Follow")
            }
        }

        holder.follow.setOnClickListener {
            if (holder.follow.text.toString().equals("Unfollow")) {
                SharedPreferences.removeParticulareUserFromThroughUnFollow(people[position].uid)
                SharedPreferences.removeUserFromFollowers(people[position].uid)
                holder.follow.setText("Follow")
            } else {
                SharedPreferences.addUserToFollowings(people[position])
                SharedPreferences.addUserToFollowers(people[position].uid)
                holder.follow.setText("Unfollow")
            }
        }


        holder.person_name.setOnClickListener {
            val fragment = PersonProfileFragment()
            val bundle = Bundle()
            bundle.putString("uid", people.get(position).uid)
            fragment.arguments = bundle
            fragment.show(
                (context as MainActivity).supportFragmentManager.beginTransaction(),
                "show a dialog"
            )
        }


    }

    override fun getItemCount() = people.size

    inner class PersonViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val person_name = v.findViewById<TextView>(R.id.txt_name_username)
        val follow = v.findViewById<TextView>(R.id.txt_follow)
    }


}