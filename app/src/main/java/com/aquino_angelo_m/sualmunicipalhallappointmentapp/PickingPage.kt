package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
        val logo = findViewById<ImageView>(R.id.logo)
        val visitorBtn = findViewById<Button>(R.id.visitorBtn)
        val questionText = findViewById<TextView>(R.id.questiontxt)

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        residentBtn.startAnimation(fadeInAnimation)
        visitorBtn.startAnimation(fadeInAnimation)
        questionText.startAnimation(fadeInAnimation)

        residentBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                logo, "transition_name"
            )

            startActivity(intent, options.toBundle())
        }

        visitorBtn.setOnClickListener {
            val intent = Intent(this, VisitorsPage::class.java)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                logo, "transition_name"
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
