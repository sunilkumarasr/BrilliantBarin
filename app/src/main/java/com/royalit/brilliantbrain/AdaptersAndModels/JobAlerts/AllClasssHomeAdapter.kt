package com.royalit.brilliantbrain.AdaptersAndModels.JobAlerts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.brilliantbrain.R

class AllClasssHomeAdapter(
    private val items: List<JobAlertModel>,
    private val onItemClick: (JobAlertModel) -> Unit // Click listener function
) : RecyclerView.Adapter<AllClasssHomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
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
            .inflate(R.layout.class_home_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        //holder.txtTitle.text = item.title
       // holder.txtDec.text = Html.fromHtml(item.description, Html.FROM_HTML_MODE_LEGACY)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}