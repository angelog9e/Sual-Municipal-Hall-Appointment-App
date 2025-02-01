package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.app.DatePickerDialog
import android.content.Intent
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
                dateButton.text.isEmpty() -> {
                    Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                }
                timeButton.text.isEmpty() -> {
                    Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show()
                }
                officeInput.selectedItemPosition == 0 -> {
                    Toast.makeText(this, "Please select an office", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Collect data
                    val office = officeInput.selectedItem.toString()
                    val date = dateButton.text.toString()
                    val time = timeButton.text.toString()

                    // Retrieve data from Resident
                    val rName = intent.getStringExtra("name")
                    val rAddress = intent.getStringExtra("address")
                    val rBarangay = intent.getStringExtra("barangay")
                    val rContact = intent.getStringExtra("contact")
                    val rEmail = intent.getStringExtra("email")

                    // Retrieve data from Visitor
                    val vName = intent.getStringExtra("name")
                    val vAddress = intent.getStringExtra("address")
                    val vZip = intent.getStringExtra("zip")
                    val vProvince = intent.getStringExtra("province")
                    val vContact = intent.getStringExtra("contact")
                    val vEmail = intent.getStringExtra("email")

                    // Pass data to VerificationPage
                    val intent = Intent(this, VerificationPage::class.java).apply {
                        putExtra("office", office)
                        putExtra("date", date)
                        putExtra("time", time)

                        // Pass additional resident and visitor data if needed
                        putExtra("rName", rName)
                        putExtra("rAddress", rAddress)
                        putExtra("rBarangay", rBarangay)
                        putExtra("rContact", rContact)
                        putExtra("rEmail", rEmail)

                        putExtra("vName", vName)
                        putExtra("vAddress", vAddress)
                        putExtra("vZip", vZip)
                        putExtra("vProvince", vProvince)
                        putExtra("vContact", vContact)
                        putExtra("vEmail", vEmail)
                    }
                    startActivity(intent)
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

                    val isValidTime = when (selectedAMPM) {
                        "AM" -> selectedHour in 8..11
                        "PM" -> selectedHour == 12 || selectedHour in 1..4
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
                        Toast.makeText(this, "Please select a time between 8:00 AM and 4:00 PM.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }

        dateButton.setOnClickListener {
            // Get the current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create and show the DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

                    val dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)

                    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                        Toast.makeText(this, "Weekends are not allowed. Please select a weekday.", Toast.LENGTH_SHORT).show()
                    } else {
                        val formattedDate = String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d",
                            selectedDay,
                            selectedMonth + 1,
                            selectedYear
                        )
                        dateButton.text = formattedDate
                    }
                },
                year, month, day
            )

            datePickerDialog.datePicker.minDate = calendar.timeInMillis // Set the minimum date to today
            datePickerDialog.show()
        }

    }
}
