package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri

class ContactUs : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.contact_us, container, false)

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(android.graphics.Color.TRANSPARENT.toDrawable())

        dialog?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        val facebookTextView = view.findViewById<TextView>(R.id.facebook_text)

        facebookTextView.setOnClickListener {
            val url = "https://www.facebook.com/LGUSual"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = url.toUri()
            startActivity(intent)
        }

        val numberTextView = view.findViewById<TextView>(R.id.number_text)

        numberTextView.setOnClickListener {
            val phoneNumber = "tel: +639563629014"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = phoneNumber.toUri()
            startActivity(intent)
        }

        val emailTextView = view.findViewById<TextView>(R.id.email_text)

        emailTextView.setOnClickListener {
            val url = "lgusual@yahoo.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = url.toUri()
            startActivity(intent)
        }

        val closeButton = view.findViewById<TextView>(R.id.closebtn)

        closeButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(300.dpToPx4(requireContext()), 460.dpToPx4(requireContext()))
    }
}

fun Int.dpToPx4(context: Context): Int {
    return (this * context.resources.displayMetrics.density + 0.5f).toInt()
}

