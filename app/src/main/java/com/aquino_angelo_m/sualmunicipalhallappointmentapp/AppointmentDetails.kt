package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import java.util.Calendar
import java.util.Locale
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

        // Retrieve data from Resident
        val Rname = intent.getStringExtra("name")
        val Raddress = intent.getStringExtra("address")
        val Rbarangay = intent.getStringExtra("barangay")
        val Rcontact = intent.getStringExtra("contact")
        val Remail = intent.getStringExtra("email")

        // Retrieve data from Visitor
        val Vname = intent.getStringExtra("name")
        val Vaddress = intent.getStringExtra("address")
        val Vzip = intent.getStringExtra("zip")
        val Vprovince = intent.getStringExtra("province")
        val Vcontact = intent.getStringExtra("contact")
        val Vemail = intent.getStringExtra("email")

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

        // Buttons
        val timeButton = findViewById<Button>(R.id.timebtn)
        val dateButton = findViewById<Button>(R.id.datebtn)
        val backButton = findViewById<Button>(R.id.backbtn)
        val nextButton = findViewById<Button>(R.id.nextbtn)

        // Texts
        val text1 = findViewById<TextView>(R.id.datetxt)
        val text2 = findViewById<TextView>(R.id.timetxt)
        val text3 = findViewById<TextView>(R.id.officetxt)
        val text4 = findViewById<TextView>(R.id.purposetxt)
        val text5 = findViewById<TextView>(R.id.othertxt)

        // Inputs
        val purposeInput = findViewById<Spinner>(R.id.purposeInput)
        val otherInput = findViewById<EditText>(R.id.otherInput)

        //Initial State
        val viewsToAnimate = arrayOf(
            text1, dateButton, text2, timeButton,
            text3, officeInput,
            text4, purposeInput, text5, otherInput,
            nextButton, backButton
        )

        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        //Sequential Animation
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

        backButton.setOnClickListener {

            finish()
        }

        timeButton.setOnClickListener {
            // Custom dialog for time selection
            val dialogView = layoutInflater.inflate(R.layout.custom_time_picker, null)

            val hourPicker = dialogView.findViewById<NumberPicker>(R.id.hourPicker)
            val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minutePicker)
            val ampmSpinner = dialogView.findViewById<Spinner>(R.id.ampmSpinner)

            // Maximum and minimum values
            hourPicker.minValue = 1
            hourPicker.maxValue = 12
            minutePicker.minValue = 0
            minutePicker.maxValue = 59

            val dialog = AlertDialog.Builder(this)
                .setTitle("Select Time")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                    val selectedHour = hourPicker.value
                    val selectedMinute = minutePicker.value
                    val selectedAMPM = ampmSpinner.selectedItem.toString()

                    if (selectedAMPM == "PM" && selectedHour == 12) {
                        Toast.makeText(this, "Lunch break from 12 PM to 1 PM. Please choose another time.", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val isValidTime = when (selectedAMPM) {
                        "AM" -> selectedHour in 8..11
                        "PM" -> {

                            selectedHour in 1..4 || (selectedHour == 4 && selectedMinute <= 30)
                        }
                        else -> false
                    }

                    if (isValidTime) {

                        val formattedTime = String.format(
                            Locale.getDefault(),
                            "%d:%02d %s",
                            selectedHour,
                            selectedMinute,
                            selectedAMPM
                        )

                        timeButton.text = formattedTime
                    } else {
                        Toast.makeText(this, "Please select a time between 8:00 AM and 4:30 PM.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }
}
