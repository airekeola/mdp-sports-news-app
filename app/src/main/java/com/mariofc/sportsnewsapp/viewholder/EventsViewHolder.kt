package com.mariofc.sportsnewsapp.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mariofc.sportsnewsapp.R

class EventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nameTextView: TextView
    val dateTextView: TextView
    val descriptionTextView: TextView

    init {
        nameTextView = view.findViewById(R.id.nameTextView)
        dateTextView = view.findViewById(R.id.dateTextView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)
    }
}