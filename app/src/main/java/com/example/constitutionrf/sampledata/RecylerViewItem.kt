package com.example.constitutionrf.sampledata

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.constitutionrf.R
import kotlinx.coroutines.launch

class RecylerViewItem(
    private val articles: List<StateModel>,
    private val listener: OnItemClickListener,
    private val stateViewModel: StateViewModel
): RecyclerView.Adapter<RecylerViewItem.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val text: TextView = itemView.findViewById(R.id.tv_item)
        val iv_fav: ImageView = itemView.findViewById(R.id.iv_fav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = articles[position]
        holder.text.text = "Статья ${article.num}"

        if (article.favourites) {
            holder.iv_fav.visibility = View.VISIBLE
        } else {
            holder.iv_fav.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(article.num - 1)
        }
    }
    override fun getItemCount(): Int {
        return articles.size
    }
}