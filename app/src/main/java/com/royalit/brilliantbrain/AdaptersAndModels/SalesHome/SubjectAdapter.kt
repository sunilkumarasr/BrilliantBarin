package com.royalit.brilliantbrain.AdaptersAndModels.SalesHome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.brilliantbrain.R

class SubjectAdapter(
    private var items: List<ProductData>,
    private val onItemClick: (ProductData) -> Unit // Click listener function
) : RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
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
            .inflate(R.layout.subject_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]


//        Glide.with(holder.imgLogo)
//            .load(RetrofitClient.Image_Path + firstImageUrl)
//            .placeholder(R.drawable.close_ic)
//            .error(R.drawable.close_ic)
//            .into(holder.imgLogo)

      //  holder.txtTitle.text = item.product.product


    }

    override fun getItemCount(): Int {
        return items.size
    }

}