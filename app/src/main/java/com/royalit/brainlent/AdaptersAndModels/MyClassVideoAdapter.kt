package com.royalit.brainlent.AdaptersAndModels

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient

class MyClassVideoAdapter(
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
        val context = viewItem.context
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.video_popup)

        val videoView: VideoView = dialog.findViewById(R.id.videoViewPopup)
        val progressBar: ProgressBar = dialog.findViewById(R.id.progressBar)
        val imgClose: ImageView = dialog.findViewById(R.id.imgClose)
        val btnSkipBack: TextView = dialog.findViewById(R.id.btnSkipBack)
        val btnSkipForward: TextView = dialog.findViewById(R.id.btnSkipForward)

        videoView.setOnPreparedListener {
            progressBar.visibility = View.GONE
            btnSkipBack.visibility = View.VISIBLE
            btnSkipForward.visibility = View.VISIBLE
        }

        videoView.setOnErrorListener { _, _, _ ->
            progressBar.visibility = View.GONE
            btnSkipBack.visibility = View.VISIBLE
            btnSkipForward.visibility = View.VISIBLE
            return@setOnErrorListener true
        }

        // Set the video path and start the video
        videoView.setVideoPath(RetrofitClient.Image_Path+additionalVideo)
        progressBar.visibility = View.VISIBLE
        btnSkipBack.visibility = View.GONE
        btnSkipForward.visibility = View.GONE
        videoView.start()

        // Skip 10 seconds backward
        btnSkipBack.setOnClickListener {
            val currentPosition = videoView.currentPosition
            val newPosition = currentPosition - 10000 // 10 seconds back
            videoView.seekTo(if (newPosition < 0) 0 else newPosition)
        }

        // Skip 10 seconds forward
        btnSkipForward.setOnClickListener {
            val currentPosition = videoView.currentPosition
            val newPosition = currentPosition + 10000 // 10 seconds forward
            videoView.seekTo(newPosition)
        }

        // Close the dialog when the button is clicked
        imgClose.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

}
