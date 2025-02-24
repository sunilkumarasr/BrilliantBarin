package com.royalit.brainlent.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivityClassDetailsBinding
import com.royalit.brainlent.databinding.ActivityPlayBinding

class PlayActivity : AppCompatActivity() {

    val binding: ActivityPlayBinding by lazy {
        ActivityPlayBinding.inflate(layoutInflater)
    }

    val handler = Handler(Looper.getMainLooper())

    var url: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        url = intent.getStringExtra("url").toString()


        playVideo()

    }

    private fun playVideo() {

        binding.imgBack.setOnClickListener {
            finish()
        }


        //video
        Glide.with(binding.imgThumbnail)
            .load(RetrofitClient.Image_Path + url)
            .error(R.drawable.item_ic)
            .into(binding.imgThumbnail)
        val videoUri: Uri = Uri.parse(RetrofitClient.Image_Path + url)
        binding.videoBanner.setVideoURI(videoUri)


        // Skip 10 seconds backward
        binding.btnSkipBack.setOnClickListener {
            val currentPosition = binding.videoBanner.currentPosition
            val newPosition = currentPosition - 10000 // 10 seconds back
            binding.videoBanner.seekTo(if (newPosition < 0) 0 else newPosition)
        }

        // Skip 10 seconds forward
        binding.btnSkipForward.setOnClickListener {
            val currentPosition = binding.videoBanner.currentPosition
            val newPosition = currentPosition + 10000 // 10 seconds forward
            binding.videoBanner.seekTo(newPosition)
        }



        //play
        binding.imgPlay.setOnClickListener {
            // Hide the play button once the video starts playing
            binding.imgThumbnail.visibility = View.INVISIBLE
            binding.imgPlay.visibility = View.INVISIBLE


            // Start the video playback
            binding.videoBanner.setVideoURI(videoUri)
            binding.videoBanner.start()
        }

        //prepare
        binding.videoBanner.setOnPreparedListener {
            binding.progressBar.visibility = View.GONE
            // Execute after 10 seconds (10000ms)
            handler.postDelayed({
                binding.btnSkipBack.visibility = View.VISIBLE
            }, 3000)
            binding.btnSkipForward.visibility = View.VISIBLE
        }

        //error
        binding.videoBanner.setOnErrorListener { _, _, _ ->
            binding.imgThumbnail.visibility = View.INVISIBLE
            binding.btnSkipBack.visibility = View.INVISIBLE
            binding.btnSkipForward.visibility = View.INVISIBLE
            binding.imgPlay.visibility = View.VISIBLE
            return@setOnErrorListener true
        }


        // Optional: Hide the play button when the video ends
        binding.videoBanner.setOnCompletionListener {
            binding.imgThumbnail.visibility = View.INVISIBLE
            binding.btnSkipBack.visibility = View.INVISIBLE
            binding.btnSkipForward.visibility = View.INVISIBLE
            binding.imgPlay.visibility = View.VISIBLE
        }

    }
}