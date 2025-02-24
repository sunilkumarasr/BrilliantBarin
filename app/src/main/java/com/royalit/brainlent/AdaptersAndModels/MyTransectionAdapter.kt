package com.royalit.brainlent.AdaptersAndModels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.brainlent.R

class MyTransectionAdapter(
    private var items: List<DataClass>,
    private val onItemClick: (DataClass) -> Unit // Click listener function
) : RecyclerView.Adapter<MyTransectionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtdate: TextView = itemView.findViewById(R.id.txtdate)
        val txtOrderId: TextView = itemView.findViewById(R.id.txtOrderId)
        val txtTrId: TextView = itemView.findViewById(R.id.txtTrId)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)

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
            .inflate(R.layout.mytransections_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]


        holder.txtOrderId.text = "OrderID: "+item.order_id
        holder.txtTrId.text = "TransectionID: "+item.transcation_id
        holder.txtdate.text = "Date: "+item.created_date +" - "+ item.created_time
        holder.txtPrice.text = "₹"+item.total

    }

    override fun getItemCount(): Int {
        return items.size
    }

}