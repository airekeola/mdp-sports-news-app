package com.mariofc.sportsnewsapp.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mariofc.sportsnewsapp.R
import com.mariofc.sportsnewsapp.data.Archive
import com.mariofc.sportsnewsapp.viewholder.ArchivesViewHolder

class ArchivesAdapter (
    private val archives: List<Archive>, private val menuClickListener:
        (Int, Int) -> Boolean
) : RecyclerView.Adapter<ArchivesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchivesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_archives, parent, false)

        return ArchivesViewHolder(view)
    }

    override fun getItemCount(): Int = archives.size

    override fun onBindViewHolder(holder: ArchivesViewHolder, position: Int) {
        holder.nameTextView.text = archives[position].name
        holder.dateTextView.text = archives[position].date
        holder.descriptionTextView.text = archives[position].description

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