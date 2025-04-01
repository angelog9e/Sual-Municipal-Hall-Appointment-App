package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.Locale

class ReviewAppointment : DialogFragment() {

    private var frontPhoto: Bitmap? = null
    private var backPhoto: Bitmap? = null
    private var selfiePhoto: Bitmap? = null

    private var office: String? = null
    private var purpose: String? = null
    private var date: String? = null
    private var time: String? = null
    private var other: String? = null

    private var officePosition: Int = 0
    private var purposePosition: Int = 0

    private var rName: String? = null
    private var rAddress: String? = null
    private var rBarangay: String? = null
    private var rContact: String? = null
    private var rEmail: String? = null

    private var occupant: String? = null
    private var zip: String? = null

    private var vName: String? = null
    private var vAddress: String? = null
    private var vZip: String? = null
    private var vProvince: String? = null
    private var vContact: String? = null
    private var vEmail: String? = null

    private val allowedCharactersFilter = InputFilter { source, _, _, _, _, _ ->
        val allowedPattern = Regex("[a-zA-Z0-9 .,@]*")
        if (source.matches(allowedPattern)) {
            source
        } else {
            ""
        }
    }

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

            officePosition = bundle.getInt("officePosition")
            purposePosition = bundle.getInt("purposePosition")

            rName = bundle.getString("rName")
            rAddress = bundle.getString("rAddress")
            rBarangay = bundle.getString("rBarangay")
            rContact = bundle.getString("rContact")
            rEmail = bundle.getString("rEmail")

            occupant = bundle.getString("occupant")
            zip = bundle.getString("zip")

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
        view.findViewById<TextView>(R.id.datepreview).text = date
        view.findViewById<TextView>(R.id.timepreview).text = time
        view.findViewById<TextView>(R.id.otherpreview).text = other

        view.findViewById<TextView>(R.id.namepreview).text = rName
        view.findViewById<TextView>(R.id.addresspreview).text = rAddress
        view.findViewById<TextView>(R.id.barangaypreview).text = rBarangay
        view.findViewById<TextView>(R.id.numberpreview).text = rContact
        view.findViewById<TextView>(R.id.emailpreview).text = rEmail
        view.findViewById<TextView>(R.id.occupantview).text = occupant
        view.findViewById<TextView>(R.id.zippreview).text = zip

        view.findViewById<TextView>(R.id.namepreview).text = vName
        view.findViewById<TextView>(R.id.addresspreview).text = vAddress
        view.findViewById<TextView>(R.id.zippreview).text = vZip
        view.findViewById<TextView>(R.id.provincepreview).text = vProvince
        view.findViewById<TextView>(R.id.numberpreview).text = vContact
        view.findViewById<TextView>(R.id.emailpreview).text = vEmail

        view.findViewById<ImageView>(R.id.frontIDpreview).setImageBitmap(frontPhoto)
        view.findViewById<ImageView>(R.id.backIDpreview).setImageBitmap(backPhoto)
        view.findViewById<ImageView>(R.id.selfiepreview).setImageBitmap(selfiePhoto)

        setupInputValidation(view)
        setupButtonActions(view)

        setupOfficeSpinner(view)
        setupButtonActions(view)
    }

    private fun setupInputValidation(view: View) {
        val nameInput = view.findViewById<EditText>(R.id.namepreview)
        val addressInput = view.findViewById<EditText>(R.id.addresspreview)
        val contactInput = view.findViewById<EditText>(R.id.numberpreview)
        val emailInput = view.findViewById<EditText>(R.id.emailpreview)
        val zipInput = view.findViewById<EditText>(R.id.zippreview)
        val otherInput = view.findViewById<EditText>(R.id.otherpreview)

        setupValidationForInput(nameInput, 10, 50, R.drawable.check2, R.drawable.wrong)
        setupValidationForInput(addressInput, 12, 100, R.drawable.check2, R.drawable.wrong)
        setupValidationForInput(contactInput, 11, 11, R.drawable.check2, R.drawable.wrong, "^09\\d{9}\$")
        setupValidationForInput(emailInput, 0, 50, R.drawable.check2, R.drawable.wrong, "^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com|hotmail\\.com)\$", optional = true)
        setupValidationForInput(zipInput, 4, 4, R.drawable.check2, R.drawable.wrong, "\\d{4}", optional = true)

        otherInput.filters = arrayOf(allowedCharactersFilter)

        val submitButton: Button = view.findViewById(R.id.submitbtn)
        submitButton.setOnClickListener {
            if (validateInputs(nameInput, addressInput, contactInput, emailInput, zipInput)) {
                sendDataToServer()
            }
        }
    }

    private fun setupValidationForInput(
        input: EditText,
        minLength: Int,
        maxLength: Int,
        validIcon: Int,
        invalidIcon: Int,
        pattern: String = ".*",
        optional: Boolean = false
    ) {
        input.filters = arrayOf(allowedCharactersFilter, InputFilter.LengthFilter(maxLength))

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val isValid = if (optional && s.isNullOrEmpty()) {
                    true
                } else {
                    (s?.length ?: 0) in minLength..maxLength && s.toString()
                        .matches(pattern.toRegex())
                }
                input.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null,
                    ContextCompat.getDrawable(requireContext(), if (isValid) validIcon else invalidIcon), null
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
        dateButton: Button? = null,
        timeButton: Button? = null
    ): Boolean {
        return when {
            nameInput.text.isEmpty() || nameInput.text.length < 10 -> {
                showToast("Please enter a name with at least 10 characters.")
                false
            }
            addressInput.text.isEmpty() || addressInput.text.length < 12 -> {
                showToast("Please enter an address with at least 12 characters.")
                false
            }
            zipInput.text.length != 4 || !zipInput.text.toString().matches("\\d{4}".toRegex()) -> {
                showToast("Please enter a 4-digit zip code.")
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
            timeButton != null && timeButton.text.isEmpty() -> {
                showToast("Please select a time.")
                false
            }
            dateButton != null && dateButton.text.isEmpty() -> {
                showToast("Please select a date.")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupOfficeSpinner(view: View) {
        val officeInput: Spinner = view.findViewById(R.id.officepreview)
        val purposeInput: Spinner = view.findViewById(R.id.purposepreview)

        val officeList = resources.getStringArray(R.array.office_list).toMutableList()

        val officeAdapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            officeList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(
                    if (position == 0)
                        ContextCompat.getColor(requireContext(), R.color.lightgrey)
                    else
                        ContextCompat.getColor(requireContext(), R.color.blackknight)
                )
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(
                    if (position == 0)
                        ContextCompat.getColor(requireContext(), R.color.lightgrey)
                    else
                        ContextCompat.getColor(requireContext(), R.color.moonlight)
                )
                return view
            }
        }
        officeInput.adapter = officeAdapter

        officeInput.setSelection(officePosition)

        officeInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val purposes = when (position) {
                    1 -> resources.getStringArray(R.array.purpose_municipal_mayor)
                    2 -> resources.getStringArray(R.array.purpose_municipal_vice_mayor)
                    3 -> resources.getStringArray(R.array.purpose_municipal_human_resource_management_officer)
                    4 -> resources.getStringArray(R.array.purpose_municipal_treasurer)
                    5 -> resources.getStringArray(R.array.purpose_municipal_secretary)
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

                val purposeAdapter = object : ArrayAdapter<String>(
                    requireContext(),
                    R.layout.spinner_item,
                    purposes
                ) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getView(position, convertView, parent) as TextView
                        view.setTextColor(
                            if (position == 0)
                                ContextCompat.getColor(requireContext(), R.color.lightgrey)
                            else
                                ContextCompat.getColor(requireContext(), R.color.blackknight)
                        )
                        return view
                    }

                    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getDropDownView(position, convertView, parent) as TextView
                        view.setTextColor(
                            if (position == 0)
                                ContextCompat.getColor(requireContext(), R.color.lightgrey)
                            else
                                ContextCompat.getColor(requireContext(), R.color.moonlight)
                        )
                        return view
                    }
                }
                purposeInput.adapter = purposeAdapter

                purposeInput.setSelection(purposePosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupButtonActions(view: View) {
        val timeButton: Button = view.findViewById(R.id.timepreview)
        val dateButton: Button = view.findViewById(R.id.datepreview)

        timeButton.setOnClickListener { showTimePickerDialog(timeButton) }
        dateButton.setOnClickListener { showDatePickerDialog(dateButton) }
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

        AlertDialog.Builder(requireContext())
            .setTitle("Select Time")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val selectedHour = hourPicker.value
                val selectedMinute = minutePicker.value
                val selectedAMPM = ampmSpinner.selectedItem.toString()

                if (isValidTime(selectedHour, selectedAMPM)) {
                    button.text = String.format(Locale.getDefault(), "%d:%02d %s", selectedHour, selectedMinute, selectedAMPM)
                } else {
                    Toast.makeText(requireContext(), "Please select a valid time.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDatePickerDialog(button: Button) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedCalendar = Calendar.getInstance().apply { set(year, month, day) }
                if (selectedCalendar.get(Calendar.DAY_OF_WEEK) in listOf(Calendar.SATURDAY, Calendar.SUNDAY)) {
                    Toast.makeText(requireContext(), "Weekends are not allowed.", Toast.LENGTH_SHORT).show()
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

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

    private fun sendDataToServer() {
        // Fetch the latest data from the UI
        val updatedAppointment = Appointment(
            occupantTextView = view?.findViewById<TextView>(R.id.occupantview)?.text.toString(),
            officeTextView = view?.findViewById<Spinner>(R.id.officepreview)?.selectedItem.toString(),
            purposeTextView = view?.findViewById<Spinner>(R.id.purposepreview)?.selectedItem.toString(),
            dateTextView = view?.findViewById<TextView>(R.id.datepreview)?.text.toString(),
            timeTextView = view?.findViewById<TextView>(R.id.timepreview)?.text.toString(),
            otherTextView = view?.findViewById<TextView>(R.id.otherpreview)?.text.toString(),
            rNameTextView = view?.findViewById<TextView>(R.id.namepreview)?.text.toString(),
            rAddressTextView = view?.findViewById<TextView>(R.id.addresspreview)?.text.toString(),
            rBarangayTextView = view?.findViewById<TextView>(R.id.barangaypreview)?.text.toString(),
            rContactTextView = view?.findViewById<TextView>(R.id.numberpreview)?.text.toString(),
            rEmailTextView = view?.findViewById<TextView>(R.id.emailpreview)?.text.toString(),
            vZipTextView = view?.findViewById<TextView>(R.id.zippreview)?.text.toString(),
            vProvinceTextView = view?.findViewById<TextView>(R.id.provincepreview)?.text.toString()
        )

        if (frontPhoto == null || backPhoto == null || selfiePhoto == null) {
            Toast.makeText(context, "Please ensure all photos are uploaded.", Toast.LENGTH_SHORT).show()
            return
        }

        // Convert photos
        val frontPhotoPart = MultipartBody.Part.createFormData(
            "frontPhoto",
            "front_photo.jpg",
            bitmapToByteArray(frontPhoto!!).toRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        val backPhotoPart = MultipartBody.Part.createFormData(
            "backPhoto",
            "back_photo.jpg",
            bitmapToByteArray(backPhoto!!).toRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        val selfiePhotoPart = MultipartBody.Part.createFormData(
            "selfiePhoto",
            "selfie_photo.jpg",
            bitmapToByteArray(selfiePhoto!!).toRequestBody("image/jpeg".toMediaTypeOrNull())
        )

        // Convert appointment data to JSON
        val gson = Gson()
        val appointmentJson = gson.toJson(updatedAppointment)
        val appointmentRequestBody = appointmentJson.toRequestBody("application/json".toMediaTypeOrNull())

        val service = RetrofitInstance.appointmentService
        service.submitAppointment(
            appointmentRequestBody,
            frontPhotoPart,
            backPhotoPart,
            selfiePhotoPart
        ).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Appointment submitted successfully", Toast.LENGTH_SHORT).show()
                    goToApprovedPage()
                } else {
                    Toast.makeText(context, "Upload failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToApprovedPage() {
        val intent = Intent(requireContext(), ApprovedPage::class.java)
        startActivity(intent)
        dismiss()
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