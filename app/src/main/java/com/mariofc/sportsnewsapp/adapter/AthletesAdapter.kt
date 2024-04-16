package com.mariofc.sportsnewsapp.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mariofc.sportsnewsapp.R
import com.mariofc.sportsnewsapp.data.Athlete
import com.mariofc.sportsnewsapp.viewholder.AthletesViewHolder

class AthletesAdapter(
    private val athletes: List<Athlete>, private val menuClickListener:
        (Int, Int) -> Boolean
) : RecyclerView.Adapter<AthletesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthletesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_athletes, parent, false)

        return AthletesViewHolder(view)
    }

    override fun getItemCount(): Int = athletes.size

    override fun onBindViewHolder(holder: AthletesViewHolder, position: Int) {
        holder.athleteNameTextView.text = "Name: " + athletes[position].name
        holder.sportNameTextView.text = "Sport: " + athletes[position].sport
        holder.countryNameTextView.text = "Country: " + athletes[position].country
        holder.performanceTextView.text = "Best Performance: " + athletes[position].performance
        holder.medalsTextView.text = "Medals Awarded: " + athletes[position].medals
        holder.factsTextView.text = "Facts: " + athletes[position].facts

        holder.itemView.setOnCreateContextMenuListener { menu, _, _ ->
            // Inflate menu and set item click listener
            menu?.add(Menu.NONE, 0, Menu.NONE, "Edit")?.setOnMenuItemClickListener {
                menuClickListener(it.itemId, position)
            }
            menu?.add(Menu.NONE, 1, Menu.NONE, "Delete")?.setOnMenuItemClickListener {
                menuClickListener(it.itemId, position)
            }
        }
    }
}