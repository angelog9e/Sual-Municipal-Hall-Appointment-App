package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class VerificationPage : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    private val cameraRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.verification_page)

        applyWindowInsets()
        setupFrameLayoutAnimation()
        setupViewPager2()
        setupButtonListeners()
        setupSequentialAnimations()

        // Retrieve data from Intent
        val office = intent.getStringExtra("office")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")

        val rName = intent.getStringExtra("rName")
        val rAddress = intent.getStringExtra("rAddress")
        val rBarangay = intent.getStringExtra("rBarangay")
        val rContact = intent.getStringExtra("rContact")
        val rEmail = intent.getStringExtra("rEmail")

        val vName = intent.getStringExtra("vName")
        val vAddress = intent.getStringExtra("vAddress")
        val vZip = intent.getStringExtra("vZip")
        val vProvince = intent.getStringExtra("vProvince")
        val vContact = intent.getStringExtra("vContact")
        val vEmail = intent.getStringExtra("vEmail")
    }

    private fun applyWindowInsets() {
        val rootView = findViewById<View>(R.id.verification)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupFrameLayoutAnimation() {
        val frameLayout = findViewById<FrameLayout>(R.id.sheet3)

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

    private fun setupSequentialAnimations() {
        val text1 = findViewById<TextView>(R.id.text1)
        val text2 = findViewById<TextView>(R.id.text2)
        val frontIDButton = findViewById<Button>(R.id.frontidbtn)
        val backIDButton = findViewById<Button>(R.id.backidbtn)
        val selfieButton = findViewById<Button>(R.id.selfiebtn)
        val backButton = findViewById<Button>(R.id.backbtn)
        val submitButton = findViewById<Button>(R.id.submitbtn)

        val viewsToAnimate = listOf(
            text1, frontIDButton, backIDButton,
            text2, selfieButton, backButton,
            submitButton
        )

        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val delay = 100L
        viewsToAnimate.forEachIndexed { index, view ->
            view.postDelayed({
                view.visibility = View.VISIBLE
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
            }, index * delay)
        }
    }

    private fun setupButtonListeners() {
        val backButton = findViewById<Button>(R.id.backbtn)
        val frontIDButton = findViewById<Button>(R.id.frontidbtn)
        val backIDButton = findViewById<Button>(R.id.backidbtn)
        val selfieButton = findViewById<Button>(R.id.selfiebtn)
        val viewFrontIDButton = findViewById<Button>(R.id.viewfrontIDbtn)
        val viewBackIDButton = findViewById<Button>(R.id.viewbackIDbtn)
        val viewSelfieButton = findViewById<Button>(R.id.viewselfieIDbtn)
        val submitButton = findViewById<Button>(R.id.submitbtn)

        backButton.setOnClickListener { finish() }

        val cameraButtonListener = View.OnClickListener {
            // Check for camera permission
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), cameraRequestCode)
            } else {
                openCamera()
            }
        }

        frontIDButton.setOnClickListener(cameraButtonListener)
        backIDButton.setOnClickListener(cameraButtonListener)

        selfieButton.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), cameraRequestCode)
            } else {
                openFrontCamera()
            }
        }
    }

    private fun openFrontCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
        cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
        cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
        try {
            startActivity(cameraIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to open front camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivity(cameraIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
