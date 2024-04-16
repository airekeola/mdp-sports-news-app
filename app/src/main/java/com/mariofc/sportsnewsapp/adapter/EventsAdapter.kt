package com.mariofc.sportsnewsapp.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mariofc.sportsnewsapp.R
import com.mariofc.sportsnewsapp.data.Event
import com.mariofc.sportsnewsapp.viewholder.EventsViewHolder

class EventsAdapter(
    private val events: List<Event>, private val menuClickListener:
        (Int, Int) -> Boolean
) : RecyclerView.Adapter<EventsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_events, parent, false)

        return EventsViewHolder(view)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.nameTextView.text = events[position].name
        holder.dateTextView.text = events[position].date
        holder.descriptionTextView.text = events[position].description

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