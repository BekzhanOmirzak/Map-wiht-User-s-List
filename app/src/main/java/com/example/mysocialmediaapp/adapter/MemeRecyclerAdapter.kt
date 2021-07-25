package com.example.mysocialmediaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.models.Meme
import java.lang.Exception


class MemeRecyclerAdapter(val context: Context, val decide: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val memes = mutableListOf<Meme>()
    fun updateMemes(memes: List<Meme>) {
        this.memes.addAll(memes)
        notifyDataSetChanged()
    }

    interface AddMeme {
        fun onAddMeme(post: Meme)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (decide) {
            1 -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.meme_item_home, parent, false)
                return HomeMemeViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.meme_item_profile, parent, false)
                return ProfileMemeViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HomeMemeViewHolder) {
            holder.txtTitle.setText(memes[position].title)
            Glide.with(context).asBitmap().load(memes[position].url).into(holder.img_meme)

            holder.img_meme.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {
                    try {
                        val onAddMeme = context as AddMeme
                        onAddMeme.onAddMeme(memes[position])
                    } catch (ex: Exception) {
                        Toast.makeText(context, "Class case Exception", Toast.LENGTH_LONG).show()
                    }
                    return true
                }
            })

        } else if (holder is ProfileMemeViewHolder) {
            Glide.with(context).asBitmap().load(memes[position].url).into(holder.img_meme)
        }
    }

    override fun getItemCount(): Int {
        return memes.size
    }


}
