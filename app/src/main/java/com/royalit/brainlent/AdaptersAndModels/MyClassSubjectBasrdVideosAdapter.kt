package com.royalit.brainlent.AdaptersAndModels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.royalit.brainlent.R

class MyClassSubjectBasrdVideosAdapter(
    private val items: List<VideosDataResponse>,
    private val onItemClick: (VideosDataResponse) -> Unit // Click listener function
) : RecyclerView.Adapter<MyClassSubjectBasrdVideosAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val recyclerview: RecyclerView = itemView.findViewById(R.id.recyclerview)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_based_videos_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.txtTitle.text = item.title

        // Set up the nested RecyclerView for additional videos
        val myClassVideoAdapter = MyClassVideoAdapter(item.additional_videos)
        holder.recyclerview.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
        holder.recyclerview.adapter = myClassVideoAdapter

    }

    override fun getItemCount(): Int {
        return items.size
    }

}