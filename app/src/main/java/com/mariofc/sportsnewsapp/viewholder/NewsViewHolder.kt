package com.mariofc.sportsnewsapp.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mariofc.sportsnewsapp.R

class NewsViewHolder(view:View) : RecyclerView.ViewHolder(view) {
    val imageView: ImageView
    val titleTextView: TextView
    val urlTextView: TextView
    val descriptionTextView: TextView

    init {
        imageView = view.findViewById(R.id.imageView)
        titleTextView = view.findViewById(R.id.titleTextView)
        urlTextView = view.findViewById(R.id.urlTextView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)
    }
}