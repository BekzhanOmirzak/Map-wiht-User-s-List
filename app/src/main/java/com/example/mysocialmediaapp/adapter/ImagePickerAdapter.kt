package com.example.mysocialmediaapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R

class ImagePickerAdapter(
    private val context: Context,
    private val image_uris: MutableList<Uri>,
    private val size: Int,
    private val image_clicked: ImageClickListener
) : RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {


    interface ImageClickListener {
        fun onPlaceHolderClicked()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagePickerAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_image, parent, false)

        val cardWidth = parent.width / 3
        val cardHeight = parent.height / 6
        val cardSizeLength = Math.min(cardHeight, cardWidth)

        val layout_parametres = view.findViewById<ImageView>(R.id.custom_image).layoutParams
        layout_parametres.width = cardSizeLength
        layout_parametres.height = cardHeight
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < image_uris.size) {
            holder.bind(image_uris.get(position))
        } else {
            holder.bind()
        }
    }

    override fun getItemCount() = 18

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val image_view = v.findViewById<ImageView>(R.id.custom_image)

        fun bind(uri: Uri) {
            image_view.setImageURI(uri)
            image_view.setOnClickListener(null)
        }

        fun bind() {
            image_view.setOnClickListener {
                image_clicked.onPlaceHolderClicked()
            }
        }





    }
}
