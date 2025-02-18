package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewAppointment : DialogFragment() {

    private var frontPhoto: Bitmap? = null
    private var backPhoto: Bitmap? = null
    private var selfiePhoto: Bitmap? = null

    private var office: String? = null
    private var purpose: String? = null
    private var date: String? = null
    private var time: String? = null
    private var other: String? = null

    private var rName: String? = null
    private var rAddress: String? = null
    private var rBarangay: String? = null
    private var rContact: String? = null
    private var rEmail: String? = null

    private var occupant: String? = null

    private var vName: String? = null
    private var vAddress: String? = null
    private var vZip: String? = null
    private var vProvince: String? = null
    private var vContact: String? = null
    private var vEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            frontPhoto = bundle.getByteArray("frontPhoto")?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
            backPhoto = bundle.getByteArray("backPhoto")?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
            selfiePhoto = bundle.getByteArray("selfiePhoto")?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

            office = bundle.getString("office")
            purpose = bundle.getString("purpose")
            date = bundle.getString("date")
            time = bundle.getString("time")
            other = bundle.getString("other")

            rName = bundle.getString("rName")
            rAddress = bundle.getString("rAddress")
            rBarangay = bundle.getString("rBarangay")
            rContact = bundle.getString("rContact")
            rEmail = bundle.getString("rEmail")

            occupant = bundle.getString("occupant")

            vName = bundle.getString("vName")
            vAddress = bundle.getString("vAddress")
            vZip = bundle.getString("vZip")
            vProvince = bundle.getString("vProvince")
            vContact = bundle.getString("vContact")
            vEmail = bundle.getString("vEmail")
        }
    }

    private fun sendDataToServer() {
        // Fetch the latest data from the UI
        val updatedOffice = view?.findViewById<TextView>(R.id.officepreview)?.text.toString()
        val updatedPurpose = view?.findViewById<TextView>(R.id.purposepreview)?.text.toString()
        val updatedDate = view?.findViewById<TextView>(R.id.datepreview)?.text.toString()
        val updatedTime = view?.findViewById<TextView>(R.id.timepreview)?.text.toString()
        val updatedOther = view?.findViewById<TextView>(R.id.otherpreview)?.text.toString()

        val updatedRName = view?.findViewById<TextView>(R.id.namepreview)?.text.toString()
        val updatedRAddress = view?.findViewById<TextView>(R.id.addresspreview)?.text.toString()
        val updatedRBarangay = view?.findViewById<TextView>(R.id.barangaypreview)?.text.toString()
        val updatedRContact = view?.findViewById<TextView>(R.id.numberpreview)?.text.toString()
        val updatedREmail = view?.findViewById<TextView>(R.id.emailpreview)?.text.toString()

        val updatedOccupant = view?.findViewById<TextView>(R.id.occupantview)?.text.toString()

        val updatedVName = view?.findViewById<TextView>(R.id.namepreview)?.text.toString()
        val updatedVAddress = view?.findViewById<TextView>(R.id.addresspreview)?.text.toString()
        val updatedVZip = view?.findViewById<TextView>(R.id.zippreview)?.text.toString()
        val updatedVProvince = view?.findViewById<TextView>(R.id.provincepreview)?.text.toString()
        val updatedVContact = view?.findViewById<TextView>(R.id.numberpreview)?.text.toString()
        val updatedVEmail = view?.findViewById<TextView>(R.id.emailpreview)?.text.toString()

        // Create the updated appointment object
        val appointment = Appointment(
            occupantTextView = updatedOccupant,
            officeTextView = updatedOffice,
            purposeTextView = updatedPurpose,
            dateTextView = updatedDate,
            timeTextView = updatedTime,
            otherTextView = updatedOther,
            rNameTextView = updatedRName,
            rAddressTextView = updatedRAddress,
            rBarangayTextView = updatedRBarangay,
            rContactTextView = updatedRContact,
            rEmailTextView = updatedREmail,
            vZipTextView = updatedVZip,
            vProvinceTextView = updatedVProvince
        )

        // Send data to the server
        val service = RetrofitInstance.appointmentService
        val call = service.submitAppointment(appointment)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        if (apiResponse.status == "success") {
                            Log.d("Appointment", "Appointment submitted successfully")
                            Toast.makeText(context, "Appointment submitted successfully", Toast.LENGTH_SHORT).show()
                            goToApprovedPage()
                        } else {
                            Log.e("Appointment", "Error: ${apiResponse.message}")
                            Toast.makeText(context, "Error: ${apiResponse.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("Appointment", "HTTP Error: ${response.code()}")
                    Toast.makeText(context, "HTTP Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("Appointment", "Failed to connect to the server", t)
                Toast.makeText(context, "Failed to connect to the server", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToApprovedPage() {
        val intent = Intent(requireContext(), ApprovedPage::class.java)
        startActivity(intent)
        dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val submitButton: Button = view.findViewById(R.id.submitbtn)
        submitButton.setOnClickListener {
            sendDataToServer()
        }

        // Bind UI elements
        view.findViewById<TextView>(R.id.officepreview).text = office
        view.findViewById<TextView>(R.id.purposepreview).text = purpose
        view.findViewById<TextView>(R.id.datepreview).text = date
        view.findViewById<TextView>(R.id.timepreview).text = time
        view.findViewById<TextView>(R.id.otherpreview).text = other

        view.findViewById<TextView>(R.id.namepreview).text = rName
        view.findViewById<TextView>(R.id.addresspreview).text = rAddress
        view.findViewById<TextView>(R.id.barangaypreview).text = rBarangay
        view.findViewById<TextView>(R.id.numberpreview).text = rContact
        view.findViewById<TextView>(R.id.emailpreview).text = rEmail
        view.findViewById<TextView>(R.id.occupantview).text = occupant

        view.findViewById<TextView>(R.id.namepreview).text = vName
        view.findViewById<TextView>(R.id.addresspreview).text = vAddress
        view.findViewById<TextView>(R.id.zippreview).text = vZip
        view.findViewById<TextView>(R.id.provincepreview).text = vProvince
        view.findViewById<TextView>(R.id.numberpreview).text = vContact
        view.findViewById<TextView>(R.id.emailpreview).text = vEmail

        view.findViewById<ImageView>(R.id.frontIDpreview).setImageBitmap(frontPhoto)
        view.findViewById<ImageView>(R.id.backIDpreview).setImageBitmap(backPhoto)
        view.findViewById<ImageView>(R.id.selfiepreview).setImageBitmap(selfiePhoto)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        }
        return inflater.inflate(R.layout.review_appointment, container, false)
    }

    companion object {
        fun newInstance(bundle: Bundle): ReviewAppointment {
            return ReviewAppointment().apply { arguments = bundle }
        }
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

}
