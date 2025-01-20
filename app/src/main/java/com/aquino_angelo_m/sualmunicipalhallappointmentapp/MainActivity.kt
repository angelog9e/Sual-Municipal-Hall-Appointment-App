package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val transition: Transition = TransitionInflater.from(this)
            .inflateTransition(android.R.transition.move)
        transition.duration = 600L
        window.sharedElementEnterTransition = transition
        window.sharedElementReturnTransition = transition
    }
}