package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GetStarted : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_started)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val layouts = listOf(
            R.layout.viewpager_item1,
            R.layout.viewpager_item2,
            R.layout.viewpager_item3,
            R.layout.viewpager_item4,
        )

        val adapter = ViewPagerAdapter(layouts)
        viewPager.adapter = adapter

        val handler = Handler(Looper.getMainLooper())
        val autoSlideRunnable = object : Runnable {
            var currentPage = 0

            override fun run() {
                if (currentPage == layouts.size) currentPage = 0
                viewPager.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, 3000)
            }
        }

        handler.postDelayed(autoSlideRunnable, 3000)

        TabLayoutMediator(tabLayout, viewPager) { tab, _ ->
            val tabView = LayoutInflater.from(this).inflate(R.layout.tab_item, null)
            tab.customView = tabView
        }.attach()

        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            val tabIcon = tab?.customView?.findViewById<ImageView>(R.id.tabIcon)
            if (i == viewPager.currentItem) {
                tabIcon?.alpha = 1f
            } else {
                tabIcon?.alpha = 0.5f
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until tabLayout.tabCount) {
                    val tab = tabLayout.getTabAt(i)
                    val tabIcon = tab?.customView?.findViewById<ImageView>(R.id.tabIcon)
                    if (i == position) {
                        tabIcon?.alpha = 1f
                    } else {
                        tabIcon?.alpha = 0.4f
                    }
                }
            }
        })

        val getStartedButton = findViewById<Button>(R.id.getstartedbtn)
        getStartedButton.setOnClickListener {
            val intent = Intent(this, StartScreen::class.java)
            startActivity(intent)
        }
    }
}
