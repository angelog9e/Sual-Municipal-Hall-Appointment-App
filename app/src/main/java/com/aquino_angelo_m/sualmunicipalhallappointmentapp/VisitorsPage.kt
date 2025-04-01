package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.*
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
        setupInputValidation()
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

        // Buttons
        val backButton = findViewById<Button>(R.id.backbtn)
        val nextButton = findViewById<Button>(R.id.nextbtn)

        // Texts
        val text1 = findViewById<TextView>(R.id.text1)
        val text2 = findViewById<TextView>(R.id.text2)
        val text3 = findViewById<TextView>(R.id.text3)
        val text4 = findViewById<TextView>(R.id.text4)
        val text5 = findViewById<TextView>(R.id.text5)
        val zipText = findViewById<TextView>(R.id.zip)

        // Inputs
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val contactInput = findViewById<EditText>(R.id.contactInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val zipInput = findViewById<EditText>(R.id.zipInput)

        // Initial State
        val viewsToAnimate = arrayOf(
            text1, nameInput, text2, addressInput,
            zipText, zipInput, text3, findViewById<Spinner>(R.id.provinceInput),
            text4, contactInput, text5, emailInput,
            backButton, nextButton
        )

        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        frameLayout.visibility = View.INVISIBLE
        frameLayout.post {
            frameLayout.visibility = View.VISIBLE
            frameLayout.startAnimation(
                android.view.animation.TranslateAnimation(
                    0f, 0f, frameLayout.height.toFloat(), 0f
                ).apply {
                    duration = 600
                    interpolator = AccelerateDecelerateInterpolator()
                }
            )

            frameLayout.postDelayed({
                animateViewsSequentiallyWithFadeIn(*viewsToAnimate)
            }, 200)
        }
    }

    private fun animateViewsSequentiallyWithFadeIn(vararg views: View) {
        val delayBetween = 100L
        var delay = 0L

        views.forEach { view ->
            view.postDelayed({
                view.visibility = View.VISIBLE
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
            }, delay)
            delay += delayBetween
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

        viewPager.adapter = ImagePagerAdapter(images)

        handler.postDelayed(object : Runnable {
            override fun run() {
                currentPage = (currentPage + 1) % images.size
                viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(this, 10000)
            }
        }, 5000)

        viewPager.setPageTransformer { page, position ->
            page.alpha = 1 - abs(position)
            page.scaleX = 1 - 0.2f * abs(position)
            page.scaleY = 1 - 0.2f * abs(position)
        }
    }

    private fun setupProvinceSpinner() {
        val provinceInput: Spinner = findViewById(R.id.provinceInput)
        val provinceList = resources.getStringArray(R.array.province_list)

        provinceInput.adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            provinceList
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        provinceInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(
                    ContextCompat.getColor(
                        this@VisitorsPage,
                        if (position == 0) R.color.lightgrey else R.color.blackknight
                    )
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private val allowedCharactersFilter = InputFilter { source, _, _, _, _, _ ->
        val allowedPattern = Regex("[a-zA-Z0-9 .,@]*")
        if (source.matches(allowedPattern)) {
            source
        } else {
            ""
        }
    }

    private fun setupInputValidation() {
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val contactInput = findViewById<EditText>(R.id.contactInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val zipInput = findViewById<EditText>(R.id.zipInput)
        val provinceInput = findViewById<Spinner>(R.id.provinceInput)
        val backButton = findViewById<Button>(R.id.backbtn)
        val nextButton = findViewById<Button>(R.id.nextbtn)
        val occupantText = findViewById<TextView>(R.id.visoccu)

        setupValidationForInput(nameInput, 10, 50, R.drawable.user, R.drawable.check2, R.drawable.wrong)
        setupValidationForInput(addressInput, 10, 100, R.drawable.location, R.drawable.check2, R.drawable.wrong)
        setupValidationForInput(contactInput, 11, 11, R.drawable.phone, R.drawable.check2, R.drawable.wrong, "^09\\d{9}\$")
        setupValidationForInput(emailInput, 10, 50, R.drawable.email, R.drawable.check2, R.drawable.wrong, "^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com|hotmail\\.com)$")
        setupValidationForInput(zipInput, 4, 4, R.drawable.zip, R.drawable.check2, R.drawable.wrong, "\\d{4}")

        nextButton.setOnClickListener {
            if (validateInputs(nameInput, addressInput, contactInput, emailInput, zipInput, provinceInput)) {
                val intent = Intent(this, AppointmentDetails::class.java).apply {
                    putExtras(Bundle().apply {
                        putString("name", nameInput.text.toString())
                        putString("address", addressInput.text.toString())
                        putString("province", provinceInput.selectedItem.toString())
                        putString("contact", contactInput.text.toString())
                        putString("email", emailInput.text.toString())
                        putString("zip", zipInput.text.toString())
                        putString("occupant", occupantText.text.toString())
                    })
                }
                startActivity(intent)
            }
        }

        backButton.setOnClickListener { finish() }
    }

    private fun setupValidationForInput(
        input: EditText,
        minLength: Int,
        maxLength: Int,
        startIcon: Int,
        validIcon: Int,
        invalidIcon: Int,
        pattern: String = ".*"
    ) {

        input.filters = arrayOf(allowedCharactersFilter, InputFilter.LengthFilter(maxLength))

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val isValid = (s?.length ?: 0) in minLength..maxLength && s.toString()
                    .matches(pattern.toRegex())
                input.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(this@VisitorsPage, startIcon), null,
                    ContextCompat.getDrawable(this@VisitorsPage, if (isValid) validIcon else invalidIcon), null
                )
            }
        })
    }

    private fun validateInputs(
        nameInput: EditText,
        addressInput: EditText,
        contactInput: EditText,
        emailInput: EditText,
        zipInput: EditText,
        provinceInput: Spinner
    ): Boolean {
        return when {
            nameInput.text.isEmpty() || nameInput.text.length < 12 -> {
                showToast("Please enter a name with at least 12 characters.")
                false
            }
            addressInput.text.isEmpty() || addressInput.text.length < 10 -> {
                showToast("Please enter an address with at least 10 characters.")
                false
            }
            zipInput.text.length != 4 || !zipInput.text.toString().matches("\\d{4}".toRegex()) -> {
                showToast("Please enter a 4-digit zip code.")
                false
            }
            provinceInput.selectedItemPosition == 0 -> {
                showToast("Please select a province.")
                false
            }
            contactInput.text.isEmpty() || !contactInput.text.toString().matches("^09\\d{9}\$".toRegex()) -> {
                showToast("Please enter a valid 11-digit contact number.")
                false
            }
            !emailInput.text.isNullOrEmpty() && !emailInput.text.toString().matches(
                "^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com|hotmail\\.com)\$".toRegex()
            ) -> {
                showToast("Please enter a valid email address.")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
