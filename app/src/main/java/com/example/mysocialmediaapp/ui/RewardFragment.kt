package com.example.mysocialmediaapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R

class RewardFragment : Fragment(R.layout.reward_fragment) {

    private lateinit var container: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.reward_fragment, container, false)
        this.container = view.findViewById(R.id.container)
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, ChatRoomsFragment()).commit()
        return view
    }









}