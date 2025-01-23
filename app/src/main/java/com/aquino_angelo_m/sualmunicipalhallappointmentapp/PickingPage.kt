package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.app.ActivityOptionsCompat

class PickingPage : AppCompatActivity() {

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.picking_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.picking)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        videoView = findViewById(R.id.videobackground)
        val uri = Uri.parse("android.resource://${packageName}/${R.raw.bg_video}")
        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { mp -> mp.isLooping = true }
        videoView.start()

        val residentBtn = findViewById<Button>(R.id.residentBtn)
        val shadow = findViewById<CardView>(R.id.cardView)
        val visitorBtn = findViewById<Button>(R.id.visitorBtn)

        residentBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                shadow, "transition_name"
            )

            startActivity(intent, options.toBundle())
        }

        visitorBtn.setOnClickListener {
            val intent = Intent(this, VisitorsPage::class.java)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                shadow, "transition_name"
            )

            startActivity(intent, options.toBundle())
        }

    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
    }

    override fun onResume() {
        super.onResume()
        videoView.start()
    }
}
