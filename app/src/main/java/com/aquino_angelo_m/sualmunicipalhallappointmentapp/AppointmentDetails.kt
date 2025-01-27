package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class AppointmentDetails : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.appointment_details)

        applyWindowInsets()
        setupFrameLayoutAnimation()
        setupOfficeSpinner()
        setupViewPager2()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appointment)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupFrameLayoutAnimation() {
        val frameLayout = findViewById<FrameLayout>(R.id.sheet2)

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
        viewPager = findViewById(R.id.viewPager2)

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

    private fun animateViewsSequentially(vararg views: View) {
        val delay = 100L // Delay between animations
        views.forEachIndexed { index, view ->
            view.postDelayed({
                view.visibility = View.VISIBLE // Make the view visible
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
            }, index * delay)
        }
    }

    private fun setupOfficeSpinner() {
        val officeInput: Spinner = findViewById(R.id.officeInput)
        val officeList = resources.getStringArray(R.array.office_list).toMutableList()

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            officeList
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        officeInput.adapter = adapter

        officeInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    (view as? TextView)?.setTextColor(ContextCompat.getColor(this@AppointmentDetails, R.color.lightgrey))
                } else {
                    (view as? TextView)?.setTextColor(ContextCompat.getColor(this@AppointmentDetails, R.color.blackknight))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val backButton = findViewById<Button>(R.id.backbtn)
        val nextButton = findViewById<Button>(R.id.nextbtn)

        // Texts
        val text1 = findViewById<TextView>(R.id.datetxt)
        val text2 = findViewById<TextView>(R.id.timetxt)
        val text3 = findViewById<TextView>(R.id.officetxt)
        val text4 = findViewById<TextView>(R.id.purposetxt)
        val text5 = findViewById<TextView>(R.id.othertxt)

        // Inputs
        val dateButton = findViewById<View>(R.id.datebtn)
        val timeButton = findViewById<View>(R.id.timebtn)
        val purposeInput = findViewById<Spinner>(R.id.purposeInput)
        val otherInput = findViewById<EditText>(R.id.otherInput)

        // Set Initial State
        val viewsToAnimate = arrayOf(
            text1, dateButton, text2, timeButton,
            text3, officeInput,
            text4, purposeInput, text5, otherInput,
            nextButton
        )

        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        // Call Sequential Animation
        val frameLayout = findViewById<FrameLayout>(R.id.sheet2)
        frameLayout.post {
            animateViewsSequentially(*viewsToAnimate)
        }

        nextButton.setOnClickListener {
            when {
                officeInput.selectedItemPosition == 0 -> {
                    Toast.makeText(this, "Please select a office", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
