package com.royalit.brainlent.AdaptersAndModels.SalesHome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.brainlent.AdaptersAndModels.DataClass
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient

class MyClassAdapter(
    private var items: List<DataClass>,
    private val onItemClick: (DataClass) -> Unit // Click listener function
) : RecyclerView.Adapter<MyClassAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtOrderId: TextView = itemView.findViewById(R.id.txtOrderId)
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
            .inflate(R.layout.myorders_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]


        Glide.with(holder.imgLogo)
            .load(RetrofitClient.Image_Path + (item.class_details?.image ?: "default_image_path"))
            .placeholder(R.drawable.close_ic)
            .error(R.drawable.close_ic)
            .into(holder.imgLogo)

        holder.txtTitle.text = item.class_details?.class_name ?: "Title"
        holder.txtOrderId.text = "OrderId: "+item.order_id
        holder.txtPrice.text = "₹"+item.total

    }

    override fun getItemCount(): Int {
        return items.size
    }

}