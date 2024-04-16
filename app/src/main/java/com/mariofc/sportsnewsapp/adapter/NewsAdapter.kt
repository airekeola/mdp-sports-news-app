package com.mariofc.sportsnewsapp.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mariofc.sportsnewsapp.R
import com.mariofc.sportsnewsapp.data.New
import com.mariofc.sportsnewsapp.viewholder.NewsViewHolder

class NewsAdapter(
    private val news: List<New>, private val menuClickListener:
        (Int, Int) -> Boolean
) : RecyclerView.Adapter<NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)

        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(news[position].imageUrl).into(holder.imageView)
        holder.titleTextView.text = news[position].title
        holder.urlTextView.text = news[position].imageUrl
        holder.descriptionTextView.text = news[position].description

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