package com.mariofc.sportsnewsapp.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mariofc.sportsnewsapp.R

class SportNewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val typeTextView: TextView
    val headerTextView: TextView
    val bodyTextView: TextView

    init {
        typeTextView = view.findViewById(R.id.typeTextView)
        headerTextView = view.findViewById(R.id.headerTextView)
        bodyTextView = view.findViewById(R.id.bodyTextView)
    }
}