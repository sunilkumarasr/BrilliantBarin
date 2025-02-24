package com.royalit.brainlent.AdaptersAndModels.Notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.brainlent.R

class NotificationAdapter(
    private val items: List<NotificationModel>,
    private val onItemClick: (NotificationModel) -> Unit // Click listener function
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtDec: TextView = itemView.findViewById(R.id.txtDec)


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
            .inflate(R.layout.notifications_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.title
        holder.txtDate.text = item.created_date
        holder.txtDec.text = item.description
    }

    override fun getItemCount(): Int {
        return items.size
    }
}