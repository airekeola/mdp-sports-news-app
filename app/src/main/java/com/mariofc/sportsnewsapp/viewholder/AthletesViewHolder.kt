package com.mariofc.sportsnewsapp.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mariofc.sportsnewsapp.R

class AthletesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val athleteNameTextView: TextView
    val sportNameTextView: TextView
    val countryNameTextView: TextView
    val performanceTextView: TextView
    val medalsTextView: TextView
    val factsTextView: TextView

    init{
        athleteNameTextView = view.findViewById(R.id.athleteNameTextView)
        sportNameTextView = view.findViewById(R.id.sportNameTextView)
        countryNameTextView = view.findViewById(R.id.countryNameTextView)
        performanceTextView = view.findViewById(R.id.performanceTextView)
        medalsTextView = view.findViewById(R.id.medalsTextView)
        factsTextView = view.findViewById(R.id.factsTextView)
    }
}