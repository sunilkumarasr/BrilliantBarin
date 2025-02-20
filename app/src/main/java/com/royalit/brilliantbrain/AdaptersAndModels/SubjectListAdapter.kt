package com.royalit.brilliantbrain.AdaptersAndModels

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.brilliantbrain.R

class SubjectListAdapter(
    private val items: List<SubjectResponse>,
    private val onItemClick: (SubjectResponse) -> Unit // Click listener function
) : RecyclerView.Adapter<SubjectListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSubName: TextView = itemView.findViewById(R.id.txtSubName)
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
            .inflate(R.layout.sbuject_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.txtSubName.text = item.subject
        holder.txtDec.text = item.description

    }

    override fun getItemCount(): Int {
        return items.size
    }

}