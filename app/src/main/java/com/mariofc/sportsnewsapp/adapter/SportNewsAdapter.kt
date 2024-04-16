package com.mariofc.sportsnewsapp.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mariofc.sportsnewsapp.R
import com.mariofc.sportsnewsapp.data.SportNew
import com.mariofc.sportsnewsapp.viewholder.SportNewViewHolder

class SportNewsAdapter(
    private val sportNews: List<SportNew>,
    private val menuClickListener: (Int, Int) -> Boolean
) :
    RecyclerView.Adapter<SportNewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportNewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sports, parent, false)

        return SportNewViewHolder(view)
    }

    override fun getItemCount(): Int = sportNews.size

    override fun onBindViewHolder(holder: SportNewViewHolder, position: Int) {
        holder.typeTextView.text = sportNews[position].type
        holder.headerTextView.text = sportNews[position].header
        holder.bodyTextView.text = sportNews[position].body

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