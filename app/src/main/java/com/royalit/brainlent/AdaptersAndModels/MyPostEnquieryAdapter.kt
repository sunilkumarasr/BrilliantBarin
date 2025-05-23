package com.royalit.brainlent.AdaptersAndModels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.brainlent.R

class MyPostEnquieryAdapter(
    private val items: List<EnquieryPostModel>,
    private val onItemClick: (EnquieryPostModel) -> Unit,// Click listener function
) : RecyclerView.Adapter<MyPostEnquieryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtEmail: TextView = itemView.findViewById(R.id.txtEmail)
        val txtPhone: TextView = itemView.findViewById(R.id.txtPhone)
        val txtMessage: TextView = itemView.findViewById(R.id.txtMessage)

        init {

//            linearView.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    onItemClick(items[position])
//                }
//            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_post_enquiry_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtName.text = "Name: "+item.name
        holder.txtEmail.text = "Email: "+item.email
        holder.txtPhone.text = "Mobile Number: "+item.phone
        holder.txtMessage.text = "Message: "+item.message


    }

    override fun getItemCount(): Int {
        return items.size
    }
}