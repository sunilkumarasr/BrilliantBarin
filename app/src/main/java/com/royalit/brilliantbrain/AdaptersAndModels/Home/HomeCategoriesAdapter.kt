package com.royalit.brilliantbrain.AdaptersAndModels.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.brilliantbrain.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.brilliantbrain.R

class HomeCategoriesAdapter(
    private val items: List<CategoriesModel>,
    private val onItemClick: (CategoriesModel) -> Unit,
    private val onMoreClick: () -> Unit // Callback for the "more" icon
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val NORMAL_ITEM = 0
    private val MORE_ITEM = 1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && position < items.size) {
                    onItemClick(items[position])
                }
            }
        }
    }

    inner class MoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgMore: ImageView = itemView.findViewById(R.id.imgMore)
        init {
            itemView.setOnClickListener {
                onMoreClick()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) NORMAL_ITEM else MORE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NORMAL_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_all_categories_items_list, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.more_item_layout, parent, false) // Custom layout for the "more" item
            MoreViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val item = items[position]
            //holder.txtTitle.text = item.category
            holder.txtTitle.text = "1st Class"
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1 // Add 1 for the "more" icon
    }

}

