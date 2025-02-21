package com.royalit.brainlent.AdaptersAndModels.Categorys

import android.graphics.Paint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient

class ClassesHomeAdapter(
    private val items: List<CategoriesModel>,
    private val onItemClick: (CategoriesModel) -> Unit // Click listener function
) : RecyclerView.Adapter<ClassesHomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDec: TextView = itemView.findViewById(R.id.txtDec)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val txtAcPrice: TextView = itemView.findViewById(R.id.txtAcPrice)

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
            .inflate(R.layout.all_classs_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.imgLogo)
            .load(RetrofitClient.Image_Path+item.image)
            .error(R.drawable.item_ic)
            .into(holder.imgLogo)
        holder.txtTitle.text = item.class_name
        holder.txtDec.text = Html.fromHtml(item.description, Html.FROM_HTML_MODE_LEGACY)
        holder.txtPrice.text = "₹"+item.offer_price

        holder.txtAcPrice.text = "₹"+item.actual_price
        holder.txtAcPrice.paintFlags = holder.txtAcPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

    }

    override fun getItemCount(): Int {
        return items.size
    }

}
