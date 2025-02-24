package com.royalit.brainlent.AdaptersAndModels

import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.brainlent.Activitys.ClassDetailsActivity
import com.royalit.brainlent.Activitys.MySubjectsVideosActivity
import com.royalit.brainlent.Activitys.PlayActivity
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient

class MyClassVideoAdapter(
    public val contact: MySubjectsVideosActivity,
    private val additionalVideos: List<AdditionalVideo>
) : RecyclerView.Adapter<MyClassVideoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle) // Assuming you use a VideoView to display the video
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.videoview_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = additionalVideos[position]
        // Set up the video in VideoView
        holder.txtTitle.text = video.additional_image


        holder.itemView.setOnClickListener {
            videoPopUp(holder.itemView,video.additional_image)
        }

    }

    override fun getItemCount(): Int {
        return additionalVideos.size
    }


    private fun videoPopUp(viewItem: View, additionalVideo: String) {

        contact.startActivity(Intent(contact, PlayActivity::class.java).apply {
            putExtra("url",additionalVideo)
        })

    }

}
