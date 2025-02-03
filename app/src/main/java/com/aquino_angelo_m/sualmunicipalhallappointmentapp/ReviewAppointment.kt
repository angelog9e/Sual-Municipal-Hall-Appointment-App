package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class ReviewAppointment : DialogFragment() {

    private var frontPhoto: Bitmap? = null
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

        arguments?.let { bundle ->
            val frontPhotoBytes = bundle.getByteArray("frontPhoto")
            frontPhoto = BitmapFactory.decodeByteArray(frontPhotoBytes, 0, frontPhotoBytes?.size ?: 0)

            office = bundle.getString("office")
            date = bundle.getString("date")
            time = bundle.getString("time")

            rName = bundle.getString("rName")
            rAddress = bundle.getString("rAddress")
            rBarangay = bundle.getString("rBarangay")
            rContact = bundle.getString("rContact")
            rEmail = bundle.getString("rEmail")

            vName = bundle.getString("vName")
            vAddress = bundle.getString("vAddress")
            vZip = bundle.getString("vZip")
            vProvince = bundle.getString("vProvince")
            vContact = bundle.getString("vContact")
            vEmail = bundle.getString("vEmail")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind UI elements
        val officeTextView: TextView = view.findViewById(R.id.officepreview)
        val dateTextView: TextView = view.findViewById(R.id.datepreview)
        val timeTextView: TextView = view.findViewById(R.id.timepreview)
        val rNameTextView: TextView = view.findViewById(R.id.namepreview)
        val rAddressTextView: TextView = view.findViewById(R.id.addresspreview)
        val rBarangayTextView: TextView = view.findViewById(R.id.barangaypreview)
        val rContactTextView: TextView = view.findViewById(R.id.numberpreview)
        val rEmailTextView: TextView = view.findViewById(R.id.emailpreview)
        val vNameTextView: TextView = view.findViewById(R.id.namepreview)
        val vAddressTextView: TextView = view.findViewById(R.id.addresspreview)
        val vZipTextView: TextView = view.findViewById(R.id.zippreview)
        val vProvinceTextView: TextView = view.findViewById(R.id.provincepreview)
        val vContactTextView: TextView = view.findViewById(R.id.numberpreview)
        val vEmailTextView: TextView = view.findViewById(R.id.emailpreview)

        officeTextView.text = office
        dateTextView.text = date
        timeTextView.text = time

        rNameTextView.text = rName
        rAddressTextView.text = rAddress
        rBarangayTextView.text = rBarangay
        rContactTextView.text = rContact
        rEmailTextView.text = rEmail

        vNameTextView.text = vName
        vAddressTextView.text = vAddress
        vZipTextView.text = vZip
        vProvinceTextView.text = vProvince
        vContactTextView.text = vContact
        vEmailTextView.text = vEmail
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.review_appointment, container, false)

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        dialog?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        return view
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(400.dpToPx3(requireContext()), 800.dpToPx3(requireContext()))

        dialog?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    private fun Int.dpToPx3(context: Context): Int {
        return (this * context.resources.displayMetrics.density + 0.5f).toInt()
    }

    companion object {
        fun newInstance(bundle: Bundle): ReviewAppointment {
            val fragment = ReviewAppointment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
