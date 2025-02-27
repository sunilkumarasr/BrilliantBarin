package com.royalit.brainlent.Activitys

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivityPlayBinding

class PlayActivity : AppCompatActivity() {

    val binding: ActivityPlayBinding by lazy {
        ActivityPlayBinding.inflate(layoutInflater)
    }

    private lateinit var player: ExoPlayer

    var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.black), false)

        url = intent.getStringExtra("url").toString()

        playVideo()

    }

    private fun playVideo() {

        binding.imgBack.setOnClickListener {
            finish()
        }

        player = ExoPlayer.Builder(this).build()
        binding.videoView.player = player

        // Load video URL
        val videoUri = Uri.parse(RetrofitClient.Image_Path + url) // Replace with your URL
        val mediaItem = MediaItem.fromUri(videoUri)

        // Set media and start playing
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

    }

    override fun onStop() {
        super.onStop()
        player.release() // Release player to free memory
    }

}