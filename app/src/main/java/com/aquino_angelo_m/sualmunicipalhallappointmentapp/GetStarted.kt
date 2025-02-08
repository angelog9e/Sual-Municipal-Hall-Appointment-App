package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class GetStarted : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_started)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        // List of layouts for the ViewPager
        val layouts = listOf(
            R.layout.viewpager_item1,
            R.layout.viewpager_item2,
        )

        // Set up the adapter
        val adapter = ViewPagerAdapter(layouts)
        viewPager.adapter = adapter
    }
}
