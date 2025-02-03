package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import java.io.ByteArrayOutputStream
import kotlin.math.abs

class VerificationPage : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private val cameraRequestCode = 1001

    private var frontPhoto: Bitmap? = null
    private var backPhoto: Bitmap? = null
    private var selfiePhoto: Bitmap? = null
    private var currentPhotoTag: String? = null

    private var office: String? = null
    private var date: String? = null
    private var time: String? = null

    private var rName: String? = null
    private var rAddress: String? = null
    private var rBarangay: String? = null
    private var rContact: String? = null
    private var rEmail: String? = null

    private var vName: String? = null
    private var vAddress: String? = null
    private var vZip: String? = null
    private var vProvince: String? = null
    private var vContact: String? = null
    private var vEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.verification_page)

        applyWindowInsets()
        setupAnimations()
        setupViewPager()
        setupButtonListeners()
        retrieveIntentData()
    }

    private fun applyWindowInsets() {
        val rootView = findViewById<View>(R.id.verification)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupAnimations() {
        setupFrameLayoutAnimation()
        setupSequentialAnimations()
    }

    private fun setupFrameLayoutAnimation() {
        val frameLayout = findViewById<FrameLayout>(R.id.sheet3)
        frameLayout.visibility = FrameLayout.INVISIBLE

        val slideTransition = Slide(Gravity.BOTTOM).apply {
            duration = 600L
            interpolator = AccelerateDecelerateInterpolator()
        }

        frameLayout.post {
            frameLayout.visibility = FrameLayout.VISIBLE
            frameLayout.startAnimation(
                TranslateAnimation(0f, 0f, frameLayout.height.toFloat(), 0f).apply {
                    duration = 600L
                    interpolator = AccelerateDecelerateInterpolator()
                }
            )
        }
    }

    private fun setupSequentialAnimations() {
        val viewsToAnimate = listOf(
            findViewById<TextView>(R.id.text1),
            findViewById<Button>(R.id.frontidbtn),
            findViewById<Button>(R.id.backidbtn),
            findViewById<TextView>(R.id.text2),
            findViewById<Button>(R.id.selfiebtn),
            findViewById<Button>(R.id.backbtn),
            findViewById<Button>(R.id.submitbtn)
        )

        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        viewsToAnimate.forEachIndexed { index, view ->
            view.postDelayed({
                view.visibility = View.VISIBLE
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
            }, index * 100L)
        }
    }

    private fun setupViewPager() {
        viewPager = findViewById(R.id.viewPager)
        val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4)

        val adapter = ImagePagerAdapter(images)
        viewPager.adapter = adapter

        val autoSlide = object : Runnable {
            override fun run() {
                currentPage = (currentPage + 1) % images.size
                viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(this, 10000L)
            }
        }
        handler.postDelayed(autoSlide, 5000L)

        viewPager.setPageTransformer { page, position ->
            page.alpha = 1 - abs(position)
            page.scaleX = 1 - 0.2f * abs(position)
            page.scaleY = 1 - 0.2f * abs(position)
        }
    }

    private fun retrieveIntentData() {
        intent?.let {
            office = it.getStringExtra("office")
            date = it.getStringExtra("date")
            time = it.getStringExtra("time")

            rName = it.getStringExtra("rName")
            rAddress = it.getStringExtra("rAddress")
            rBarangay = it.getStringExtra("rBarangay")
            rContact = it.getStringExtra("rContact")
            rEmail = it.getStringExtra("rEmail")

            vName = it.getStringExtra("vName")
            vAddress = it.getStringExtra("vAddress")
            vZip = it.getStringExtra("vZip")
            vProvince = it.getStringExtra("vProvince")
            vContact = it.getStringExtra("vContact")
            vEmail = it.getStringExtra("vEmail")
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun setupButtonListeners() {
        val backButton = findViewById<Button>(R.id.backbtn)
        val frontIDButton = findViewById<Button>(R.id.frontidbtn)
        val backIDButton = findViewById<Button>(R.id.backidbtn)
        val selfieButton = findViewById<Button>(R.id.selfiebtn)
        val submitButton = findViewById<Button>(R.id.submitbtn)

        val viewFrontIDButton = findViewById<Button>(R.id.viewfrontIDbtn).apply { visibility = View.INVISIBLE }
        val viewBackIDButton = findViewById<Button>(R.id.viewbackIDbtn).apply { visibility = View.INVISIBLE }
        val viewSelfieButton = findViewById<Button>(R.id.viewselfieIDbtn).apply { visibility = View.INVISIBLE }

        val cameraButtonListener = View.OnClickListener { view ->
            val tag = view.tag.toString()
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), cameraRequestCode)
            } else {
                openCamera(tag)
            }
        }

        frontIDButton.tag = "frontID"
        backIDButton.tag = "backID"
        selfieButton.tag = "selfie"

        frontIDButton.setOnClickListener(cameraButtonListener)
        backIDButton.setOnClickListener(cameraButtonListener)
        selfieButton.setOnClickListener(cameraButtonListener)

        viewFrontIDButton.setOnClickListener { frontPhoto?.let { showPhotoPreview(it, "frontID") } }
        viewBackIDButton.setOnClickListener { backPhoto?.let { showPhotoPreview(it, "backID") } }
        viewSelfieButton.setOnClickListener { selfiePhoto?.let { showPhotoPreview(it, "selfie") } }

        backButton.setOnClickListener { finish() }

        submitButton.setOnClickListener {

            when {
                frontPhoto == null -> Toast.makeText(this, "Please take a front ID photo", Toast.LENGTH_SHORT).show()
                backPhoto == null -> Toast.makeText(this, "Please take a back ID photo", Toast.LENGTH_SHORT).show()
                selfiePhoto == null -> Toast.makeText(this, "Please take a selfie photo", Toast.LENGTH_SHORT).show()
                else -> {

                    // Prepare the data bundle
                    val bundle = Bundle().apply {
                        putByteArray("frontPhoto", frontPhoto?.let { bitmapToByteArray(it) })
                        putByteArray("backPhoto", backPhoto?.let { bitmapToByteArray(it) })
                        putByteArray("selfiePhoto", selfiePhoto?.let { bitmapToByteArray(it) })

                        putString("office", office)
                        putString("date", date)
                        putString("time", time)

                        putString("rName", rName)
                        putString("rAddress", rAddress)
                        putString("rBarangay", rBarangay)
                        putString("rContact", rContact)
                        putString("rEmail", rEmail)

                        putString("vName", vName)
                        putString("vAddress", vAddress)
                        putString("vZip", vZip)
                        putString("vProvince", vProvince)
                        putString("vContact", vContact)
                        putString("vEmail", vEmail)
                    }

                    // Create and show the dialog fragment
                    val reviewAppointment = ReviewAppointment.newInstance(bundle)
                    reviewAppointment.show(supportFragmentManager, "ReviewAppointment")
                }
            }
        }
    }

    private fun openCamera(tag: String) {
        currentPhotoTag = tag // Store the tag in a field
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(cameraIntent, cameraRequestCode)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestCode && resultCode == RESULT_OK) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            currentPhotoTag?.let { tag ->
                showPhotoPreview(photo, tag)
            }
        }
    }

    private fun showPhotoPreview(photo: Bitmap, photoTag: String) {
        val dialog = Dialog(this).apply {
            setContentView(R.layout.dialog_photo_preview)
        }
        val imageView = dialog.findViewById<ImageView>(R.id.preview_image)
        val retakeButton = dialog.findViewById<Button>(R.id.retake_button)
        val usePhotoButton = dialog.findViewById<Button>(R.id.use_photo_button)

        imageView.setImageBitmap(photo)

        retakeButton.setOnClickListener {
            dialog.dismiss()
            openCamera(photoTag)
        }

        usePhotoButton.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, "Photo accepted", Toast.LENGTH_SHORT).show()

            val checkIcon = resources.getDrawable(R.drawable.check, theme)
            val check2Icon = resources.getDrawable(R.drawable.check2, theme)

            when (photoTag) {
                "frontID" -> {
                    frontPhoto = photo
                    val frontIDButton = findViewById<Button>(R.id.frontidbtn)
                    frontIDButton.setCompoundDrawablesWithIntrinsicBounds(null, checkIcon, null, null)
                    fadeInButton(findViewById(R.id.viewfrontIDbtn))
                }
                "backID" -> {
                    backPhoto = photo
                    val backIDButton = findViewById<Button>(R.id.backidbtn)
                    backIDButton.setCompoundDrawablesWithIntrinsicBounds(null, checkIcon, null, null)
                    fadeInButton(findViewById(R.id.viewbackIDbtn))
                }
                "selfie" -> {
                    selfiePhoto = photo
                    val selfieButton = findViewById<Button>(R.id.selfiebtn)
                    selfieButton.setCompoundDrawablesWithIntrinsicBounds(null, check2Icon, null, null)
                    fadeInButton(findViewById(R.id.viewselfieIDbtn))
                }
            }
        }

        dialog.show()
    }

    private fun fadeInButton(button: Button) {
        button.visibility = View.VISIBLE
        button.alpha = 0f
        button.animate().alpha(1f).setDuration(300L).start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentPhotoTag?.let { openCamera(it) } // Retry opening the camera
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
