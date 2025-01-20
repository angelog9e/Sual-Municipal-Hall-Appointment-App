package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StartScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.start_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.start)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val startBtn = findViewById<Button>(R.id.button)
        val logo = findViewById<ImageView>(R.id.logo)

        startBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            val options = androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, logo, "transition_name"
            )
            startActivity(intent, options.toBundle())
        }
    }
}