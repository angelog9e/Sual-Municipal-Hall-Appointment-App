package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.app.ActivityOptionsCompat

class ApprovedPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.approved_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.approvedpage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val homeButton = findViewById<Button>(R.id.homebtn)
        val logo = findViewById<ImageView>(R.id.logo)
        val approvedText = findViewById<TextView>(R.id.approvedtxt)
        val checkIcon = findViewById<ImageView>(R.id.check)
        val whiteBg = findViewById<ImageView>(R.id.bg)
        val contactButton = findViewById<TextView>(R.id.contacttxt)
        val confirmationText = findViewById<TextView>(R.id.confirmationtxt)

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        homeButton.startAnimation(fadeInAnimation)
        approvedText.startAnimation(fadeInAnimation)
        logo.startAnimation(fadeInAnimation)
        checkIcon.startAnimation(fadeInAnimation)
        whiteBg.startAnimation(fadeInAnimation)
        confirmationText.startAnimation(fadeInAnimation)
        contactButton.startAnimation(fadeInAnimation)

        homeButton.setOnClickListener {
            val intent = Intent(this, StartScreen::class.java)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                logo, "transition_name"
            )

            startActivity(intent, options.toBundle())
        }

        contactButton.setOnClickListener {
            val popup = ContactUs()
            popup.show(supportFragmentManager, "popup_tag")
        }
    }
}
