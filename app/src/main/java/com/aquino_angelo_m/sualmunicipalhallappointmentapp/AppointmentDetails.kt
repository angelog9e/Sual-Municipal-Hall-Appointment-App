package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.*
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
        setupButtonActions()
        initializeAnimations()
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
        val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4)
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


    private fun setupOfficeSpinner() {
        val officeInput: Spinner = findViewById(R.id.officeInput)
        val purposeInput: Spinner = findViewById(R.id.purposeInput)

        val officeList = resources.getStringArray(R.array.office_list).toMutableList()

        // Custom adapter for the office spinner
        val officeAdapter = object : ArrayAdapter<String>(
            this@AppointmentDetails,
            R.layout.spinner_item,
            officeList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(
                    if (position == 0)
                        ContextCompat.getColor(this@AppointmentDetails, R.color.lightgrey)
                    else
                        ContextCompat.getColor(this@AppointmentDetails, R.color.blackknight)
                )
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(
                    if (position == 0)
                        ContextCompat.getColor(this@AppointmentDetails, R.color.lightgrey)
                    else
                        ContextCompat.getColor(this@AppointmentDetails, R.color.moonlight)
                )
                return view
            }
        }
        officeInput.adapter = officeAdapter

        officeInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val purposes = when (position) {
                    1 -> resources.getStringArray(R.array.purpose_municipal_mayor)
                    2 -> resources.getStringArray(R.array.purpose_municipal_vice_mayor)
                    3 -> resources.getStringArray(R.array.purpose_municipal_secretary)
                    4 -> resources.getStringArray(R.array.purpose_municipal_human_resource_management_officer)
                    5 -> resources.getStringArray(R.array.purpose_municipal_treasurer)
                    6 -> resources.getStringArray(R.array.purpose_municipal_assessor)
                    7 -> resources.getStringArray(R.array.purpose_municipal_budget_officer)
                    8 -> resources.getStringArray(R.array.purpose_municipal_planning_and_development_officer)
                    9 -> resources.getStringArray(R.array.purpose_municipal_engineer)
                    10 -> resources.getStringArray(R.array.purpose_municipal_health_officer)
                    11 -> resources.getStringArray(R.array.purpose_municipal_civil_registrar)
                    12 -> resources.getStringArray(R.array.purpose_municipal_social_welfare_and_development)
                    13 -> resources.getStringArray(R.array.purpose_municipal_agriculturist)
                    14 -> resources.getStringArray(R.array.purpose_comelec)

                    else -> arrayOf("Select Purpose")
                }

                // Adapter for purposeInput
                val purposeAdapter = object : ArrayAdapter<String>(
                    this@AppointmentDetails,
                    R.layout.spinner_item,
                    purposes
                ) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getView(position, convertView, parent) as TextView
                        view.setTextColor(
                            if (position == 0)
                                ContextCompat.getColor(this@AppointmentDetails, R.color.lightgrey)
                            else
                                ContextCompat.getColor(this@AppointmentDetails, R.color.blackknight)
                        )
                        return view
                    }

                    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getDropDownView(position, convertView, parent) as TextView
                        view.setTextColor(
                            if (position == 0)
                                ContextCompat.getColor(this@AppointmentDetails, R.color.lightgrey)
                            else
                                ContextCompat.getColor(this@AppointmentDetails, R.color.moonlight)
                        )
                        return view
                    }
                }
                purposeInput.adapter = purposeAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupButtonActions() {
        val timeButton: Button = findViewById(R.id.timebtn)
        val dateButton: Button = findViewById(R.id.datebtn)
        val nextButton: Button = findViewById(R.id.nextbtn)
        val backButton: Button = findViewById(R.id.backbtn)

        timeButton.setOnClickListener { showTimePickerDialog(timeButton) }
        dateButton.setOnClickListener { showDatePickerDialog(dateButton) }

        nextButton.setOnClickListener {
            if (isValidForm()) {
                navigateToVerificationPage()
            }
        }

        backButton.setOnClickListener { finish() }
    }

    private fun showTimePickerDialog(button: Button) {
        val dialogView = layoutInflater.inflate(R.layout.custom_time_picker, null)
        val hourPicker = dialogView.findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minutePicker)
        val ampmSpinner = dialogView.findViewById<Spinner>(R.id.ampmSpinner)

        hourPicker.minValue = 1
        hourPicker.maxValue = 12
        minutePicker.minValue = 0
        minutePicker.maxValue = 59

        AlertDialog.Builder(this)
            .setTitle("Select Time")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val selectedHour = hourPicker.value
                val selectedMinute = minutePicker.value
                val selectedAMPM = ampmSpinner.selectedItem.toString()

                if (isValidTime(selectedHour, selectedAMPM)) {
                    button.text = String.format(Locale.getDefault(), "%d:%02d %s", selectedHour, selectedMinute, selectedAMPM)
                } else {
                    Toast.makeText(this, "Please select a valid time.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDatePickerDialog(button: Button) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedCalendar = Calendar.getInstance().apply { set(year, month, day) }
                if (selectedCalendar.get(Calendar.DAY_OF_WEEK) in listOf(Calendar.SATURDAY, Calendar.SUNDAY)) {
                    Toast.makeText(this, "Weekends are not allowed.", Toast.LENGTH_SHORT).show()
                } else {
                    button.text = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day)
                }
            },
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = calendar.timeInMillis
            show()
        }
    }

    private fun isValidTime(hour: Int, ampm: String): Boolean {
        return (ampm == "AM" && hour in 8..11) || (ampm == "PM" && (hour == 12 || hour in 1..4))
    }

    private fun isValidForm(): Boolean {
        val dateButton: Button = findViewById(R.id.datebtn)
        val timeButton: Button = findViewById(R.id.timebtn)
        val officeInput: Spinner = findViewById(R.id.officeInput)
        val purposeInput: Spinner = findViewById(R.id.purposeInput)

        return when {
            dateButton.text.isEmpty() -> {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                false
            }
            timeButton.text.isEmpty() -> {
                Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show()
                false
            }
            officeInput.selectedItemPosition == 0 -> {
                Toast.makeText(this, "Please select an office", Toast.LENGTH_SHORT).show()
                false
            }
            purposeInput.selectedItemPosition == 0 -> {
                Toast.makeText(this, "Please select a purpose", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun navigateToVerificationPage() {
        val dateButton: Button = findViewById(R.id.datebtn)
        val timeButton: Button = findViewById(R.id.timebtn)
        val officeInput: Spinner = findViewById(R.id.officeInput)
        val purposeInput: Spinner = findViewById(R.id.purposeInput)
        val otherInput: EditText = findViewById(R.id.otherInput)

        // Retrieve data from Resident
        val rName = intent.getStringExtra("name")
        val rAddress = intent.getStringExtra("address")
        val rBarangay = intent.getStringExtra("barangay")
        val rContact = intent.getStringExtra("contact")
        val rEmail = intent.getStringExtra("email")
        val occupant = intent.getStringExtra("occupant")

        // Retrieve data from Visitor
        val vName = intent.getStringExtra("name")
        val vAddress = intent.getStringExtra("address")
        val vZip = intent.getStringExtra("zip")
        val vProvince = intent.getStringExtra("province")
        val vContact = intent.getStringExtra("contact")
        val vEmail = intent.getStringExtra("email")

        val intent = Intent(this, VerificationPage::class.java).apply {
            putExtra("date", dateButton.text.toString())
            putExtra("time", timeButton.text.toString())
            putExtra("office", officeInput.selectedItem.toString())
            putExtra("purpose", purposeInput.selectedItem.toString())
            putExtra("other", otherInput.text.toString())

            // Pass additional resident and visitor data if needed
            putExtra("rName", rName)
            putExtra("rAddress", rAddress)
            putExtra("rBarangay", rBarangay)
            putExtra("rContact", rContact)
            putExtra("rEmail", rEmail)
            putExtra("occupant", occupant)

            putExtra("vName", vName)
            putExtra("vAddress", vAddress)
            putExtra("vZip", vZip)
            putExtra("vProvince", vProvince)
            putExtra("vContact", vContact)
            putExtra("vEmail", vEmail)
        }
        startActivity(intent)
    }

    private fun initializeAnimations() {
        val viewsToAnimate = arrayOf(
            findViewById<TextView>(R.id.datetxt),
            findViewById<Button>(R.id.datebtn),
            findViewById<TextView>(R.id.timetxt),
            findViewById<Button>(R.id.timebtn),
            findViewById<TextView>(R.id.officetxt),
            findViewById<Spinner>(R.id.officeInput),
            findViewById<TextView>(R.id.purposetxt),
            findViewById<Spinner>(R.id.purposeInput),
            findViewById<TextView>(R.id.othertxt),
            findViewById<EditText>(R.id.otherInput),
            findViewById<Button>(R.id.nextbtn),
            findViewById<Button>(R.id.backbtn)
        )

        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        findViewById<FrameLayout>(R.id.sheet2).post {
            animateViewsSequentially(*viewsToAnimate)
        }
    }

    private fun animateViewsSequentially(vararg views: View) {
        views.forEachIndexed { index, view ->
            view.postDelayed({
                view.visibility = View.VISIBLE
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
            }, index * 100L)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
