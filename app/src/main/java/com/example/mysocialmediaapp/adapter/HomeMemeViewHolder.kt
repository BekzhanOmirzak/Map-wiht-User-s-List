package com.example.mysocialmediaapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R

class HomeMemeViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    val txtTitle = v.findViewById<TextView>(R.id.title_meme)
    val img_meme = v.findViewById<ImageView>(R.id.img_meme)



}