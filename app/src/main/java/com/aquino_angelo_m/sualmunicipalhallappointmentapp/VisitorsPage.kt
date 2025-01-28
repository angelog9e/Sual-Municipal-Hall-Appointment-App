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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.visitors)) { view, insets ->
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

    private fun animateViewsSequentially(vararg views: View) {
        val delay = 100L // Delay between animations
        views.forEachIndexed { index, view ->
            view.postDelayed({
                view.visibility = View.VISIBLE
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
            }, index * delay)
        }
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

        //Buttons
        val backButton = findViewById<View>(R.id.backbtn)
        val nextButton = findViewById<View>(R.id.nextbtn)


        // Texts
        val text1 = findViewById<TextView>(R.id.text1)
        val text2 = findViewById<TextView>(R.id.text2)
        val text3 = findViewById<TextView>(R.id.text3)
        val text4 = findViewById<TextView>(R.id.text4)
        val text5 = findViewById<TextView>(R.id.text5)
        val text6 = findViewById<TextView>(R.id.zip)

        // Inputs
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val contactInput = findViewById<EditText>(R.id.contactInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)

        val zipInput = findViewById<EditText>(R.id.zipInput)

        // Initial State
        val viewsToAnimate = arrayOf(
            text1, nameInput, text2, addressInput,
            text6, zipInput, text3, provinceInput,
            text4, contactInput, text5, emailInput,
            backButton, nextButton
        )

        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        zipInput.filters = arrayOf(InputFilter.LengthFilter(4))
        contactInput.filters = arrayOf(InputFilter.LengthFilter(11))
        nameInput.filters = arrayOf(InputFilter.LengthFilter(50))
        addressInput.filters = arrayOf(InputFilter.LengthFilter(100))

        // Sequential Animation
        val frameLayout = findViewById<FrameLayout>(R.id.sheet)
        frameLayout.post {
            animateViewsSequentially(*viewsToAnimate)
        }

        nextButton.setOnClickListener {
            when {
                nameInput.text.isEmpty() || nameInput.text.length < 12 -> {
                    Toast.makeText(this, "Please enter a name with at least 12 characters", Toast.LENGTH_SHORT).show()
                }
                addressInput.text.isEmpty() || addressInput.text.length < 10 -> {
                    Toast.makeText(this, "Please enter a address with at least 10 characters", Toast.LENGTH_SHORT).show()
                }
                zipInput.text.length != 4 || !zipInput.text.toString().matches("\\d{4}".toRegex()) -> {
                    Toast.makeText(this, "Please enter a 4-digit zip code", Toast.LENGTH_SHORT).show()
                }
                provinceInput.selectedItemPosition == 0 -> {
                    Toast.makeText(this, "Please select a province", Toast.LENGTH_SHORT).show()
                }
                contactInput.text.length != 11 || !contactInput.text.toString().matches("\\d{11}".toRegex()) -> {
                    Toast.makeText(this, "Please enter a 11-digit contact number", Toast.LENGTH_SHORT).show()
                }
                else -> {

                    val bundle = Bundle().apply {
                        putString("name", nameInput.text.toString())
                        putString("address", addressInput.text.toString())
                        putString("province", provinceInput.selectedItem.toString())
                        putString("contact", contactInput.text.toString())
                        putString("email", emailInput.text.toString())
                        putString("zip", zipInput.text.toString())
                    }

                    val intent = Intent(this@VisitorsPage, AppointmentDetails::class.java).apply {
                        putExtras(bundle)
                    }

                    startActivity(intent)
                }
            }
        }

        backButton.setOnClickListener {

            finish()
        }
    }
}
