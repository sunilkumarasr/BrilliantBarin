package com.royalit.brilliantbrain.AdaptersAndModels.Categorys

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.brilliantbrain.R

class ClassesHomeAdapter(
    private val items: List<CategoriesModel>,
    private val onItemClick: (CategoriesModel) -> Unit // Click listener function
) : RecyclerView.Adapter<ClassesHomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)

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
        //Glide.with(holder.imgLogo).load(item.category_image).into(holder.imgLogo)
       // holder.txtTitle.text = item.category
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
