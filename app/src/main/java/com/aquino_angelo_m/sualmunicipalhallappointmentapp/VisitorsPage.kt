package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class VisitorsPage : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.visitors_page)

        applyWindowInsets()
        setupFrameLayoutAnimation()
        setupProvinceSpinner()
        setupViewPager2()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.visitor)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupFrameLayoutAnimation() {
        val frameLayout = findViewById<FrameLayout>(R.id.sheet)

        val slideTransition = Slide(Gravity.BOTTOM).apply {
            duration = 600L
            interpolator = AccelerateDecelerateInterpolator()
        }

        frameLayout.visibility = FrameLayout.INVISIBLE

        frameLayout.post {
            frameLayout.visibility = FrameLayout.VISIBLE
            slideTransition.addTarget(frameLayout)
            slideTransition.startDelay = 100L
            frameLayout.startAnimation(
                android.view.animation.TranslateAnimation(
                    0f, 0f, frameLayout.height.toFloat(), 0f
                ).apply {
                    duration = 600
                    interpolator = AccelerateDecelerateInterpolator()
                }
            )
        }
    }

    private fun setupViewPager2() {
        viewPager = findViewById(R.id.viewPager)

        val images = listOf(
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4
        )

        val adapter = ImagePagerAdapter(images)
        viewPager.adapter = adapter

        val autoSlide = object : Runnable {
            override fun run() {
                currentPage = (currentPage + 1) % images.size
                viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(this, 10000)
            }
        }

        handler.postDelayed(autoSlide, 5000)

        viewPager.setPageTransformer { page, position ->
            page.alpha = 1 - abs(position)
            page.scaleX = 1 - 0.2f * abs(position)
            page.scaleY = 1 - 0.2f * abs(position)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setupProvinceSpinner() {
        val provinceInput: Spinner = findViewById(R.id.provinceInput)

        val provinceList = resources.getStringArray(R.array.province_list).toMutableList()

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            provinceList
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        provinceInput.adapter = adapter

        provinceInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    (view as? TextView)?.setTextColor(ContextCompat.getColor(this@VisitorsPage, R.color.lightgrey))
                } else {
                    (view as? TextView)?.setTextColor(ContextCompat.getColor(this@VisitorsPage, R.color.blackknight))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val contactInput = findViewById<EditText>(R.id.contactInput)
        val nextButton = findViewById<View>(R.id.nextbtn)
        val zipInput = findViewById<EditText>(R.id.zipInput)

        nextButton.setOnClickListener {
            when {
                provinceInput.selectedItemPosition == 0 -> {
                    Toast.makeText(this, "Please select a valid province", Toast.LENGTH_SHORT).show()
                }
                nameInput.text.isEmpty() -> {
                    Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                }
                addressInput.text.isEmpty() -> {
                    Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show()
                }

                zipInput.text.isEmpty() -> {
                    Toast.makeText(this, "Please enter your zip code", Toast.LENGTH_SHORT).show()
                }
                contactInput.text.isEmpty() -> {
                    Toast.makeText(this, "Please enter your contact number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
