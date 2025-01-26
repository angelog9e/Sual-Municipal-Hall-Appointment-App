package com.aquino_angelo_m.sualmunicipalhallappointmentapp

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

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        applyWindowInsets()
        setupFrameLayoutAnimation()
        setupBarangaySpinner()
        setupViewPager2()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
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

    private fun setupBarangaySpinner() {
        val barangayInput: Spinner = findViewById(R.id.brgyInput)

        val barangayList = resources.getStringArray(R.array.barangay_list).toMutableList()

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            barangayList
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        barangayInput.adapter = adapter

        barangayInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    (view as? TextView)?.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.lightgrey))
                } else {
                    (view as? TextView)?.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.blackknight))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //Texts
        val text1 = findViewById<TextView>(R.id.text1)
        val text2 = findViewById<TextView>(R.id.text2)
        val text3 = findViewById<TextView>(R.id.text3)
        val text4 = findViewById<TextView>(R.id.text4)
        val text5 = findViewById<TextView>(R.id.text5)


        //Inputs
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val contactInput = findViewById<EditText>(R.id.contactInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val nextButton = findViewById<View>(R.id.nextbtn)

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Texts Animation
        text1.startAnimation(fadeInAnimation)
        text2.startAnimation(fadeInAnimation)
        text3.startAnimation(fadeInAnimation)
        text4.startAnimation(fadeInAnimation)
        text5.startAnimation(fadeInAnimation)


        // Inputs Animation
        addressInput.startAnimation(fadeInAnimation)
        contactInput.startAnimation(fadeInAnimation)
        nameInput.startAnimation(fadeInAnimation)
        nextButton.startAnimation(fadeInAnimation)
        emailInput.startAnimation(fadeInAnimation)
        barangayInput.startAnimation(fadeInAnimation)

        contactInput.filters = arrayOf(InputFilter.LengthFilter(11))
        nameInput.filters = arrayOf(InputFilter.LengthFilter(50))
        addressInput.filters = arrayOf(InputFilter.LengthFilter(100))

        nextButton.setOnClickListener {
            when {
                barangayInput.selectedItemPosition == 0 -> {
                    Toast.makeText(this, "Please select a valid barangay", Toast.LENGTH_SHORT).show()
                }
                nameInput.text.isEmpty() || nameInput.text.length < 12 -> {
                    Toast.makeText(this, "Please enter a valid name with at least 12 characters", Toast.LENGTH_SHORT).show()
                }
                addressInput.text.isEmpty() || addressInput.text.length < 10 -> {
                    Toast.makeText(this, "Please enter a valid address with at least 10 characters", Toast.LENGTH_SHORT).show()
                }
                contactInput.text.length != 11 || !contactInput.text.toString().matches("\\d{11}".toRegex()) -> {
                    Toast.makeText(this, "Please enter a valid 11-digit contact number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
