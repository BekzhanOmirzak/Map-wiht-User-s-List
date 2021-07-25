package com.example.mysocialmediaapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.adapter.Person_Adapter
import com.example.mysocialmediaapp.util.SharedPreferences

class FollowersAndFollowingsFragment : DialogFragment() {

    private lateinit var rec_view: RecyclerView
    private lateinit var adapter: Person_Adapter

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.followers_following_dialog, container, false)
        rec_view = view.findViewById(R.id.rec_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            val uid = bundle.getString("uid")
            val key = bundle.getString("key")
            adapter = Person_Adapter(requireActivity(), uid!!)
            if (key!!.equals("followers")) {
                SharedPreferences.getAllFollowers(uid) {
                    adapter.updatePeopleList(it)
                }
            } else {
                SharedPreferences.getAllFollowingsLists(uid) {
                    adapter.updatePeopleList(it)
                }
            }
        }
        rec_view.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireActivity())
        }



    }




}