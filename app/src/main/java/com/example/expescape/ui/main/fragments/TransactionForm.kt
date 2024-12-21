package com.example.expescape.ui.main.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.expescape.R
import java.text.SimpleDateFormat
import java.util.*

class TransactionForm : Fragment() {

    private lateinit var dateText: TextView
    private lateinit var datePickerIcon: ImageView
    private lateinit var amountInput: EditText
    private lateinit var rupeeSymbol: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var expenseRadio: RadioButton
    private lateinit var incomeRadio: RadioButton
    private lateinit var descriptionText: EditText
    private lateinit var saveButton: Button

    private var isEditing = false
    private var position = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_form, container, false)

        // Initialize views
        dateText = view.findViewById(R.id.date_text)
        datePickerIcon = view.findViewById(R.id.date_picker_icon)
        amountInput = view.findViewById(R.id.amount_input)
        rupeeSymbol = view.findViewById(R.id.rupee_symbol)
        radioGroup = view.findViewById(R.id.radioGroup)
        expenseRadio = view.findViewById(R.id.expense_radio)
        incomeRadio = view.findViewById(R.id.income_radio)
        descriptionText = view.findViewById(R.id.description_text)
        saveButton = view.findViewById(R.id.save_btn)

        // Set default date to current date
        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        dateText.text = currentDate

        // DatePicker setup
        datePickerIcon.setOnClickListener {
            showDatePickerDialog()
        }

        // Rupee symbol logic
        amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                rupeeSymbol.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        // Set default radio button selection and background
        setRadioButtonBackground()

        // Check if editing and populate fields
        arguments?.let { args ->
            isEditing = args.getBoolean("isEditing", false)
            position = args.getInt("position", -1)

            if (isEditing) {
                populateFields(args)
            }
        }

        // Save button click listener
        saveButton.setOnClickListener {
            if (isFormValid()) {
                sendTransactionToDashboard()
                parentFragmentManager.popBackStack()  // Return to previous screen (Dashboard)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    // Populate fields for editing
    private fun populateFields(args: Bundle) {
        dateText.text = args.getString("date")
        descriptionText.setText(args.getString("title"))
        amountInput.setText(args.getString("amount")?.replace("\u20B9", "")?.trim())

        val transactionType = args.getString("expense_id")
        if (transactionType == "Income") {
            incomeRadio.isChecked = true
        } else {
            expenseRadio.isChecked = true
        }
    }

    // Show date picker dialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate.time)
            dateText.text = formattedDate
        }, year, month, day).show()
    }

    // Validate if all form fields are filled
    private fun isFormValid(): Boolean {
        val date = dateText.text.toString()
        val description = descriptionText.text.toString()
        val amount = amountInput.text.toString()

        return date.isNotEmpty() && description.isNotEmpty() && amount.isNotEmpty()
    }

    // Set default and dynamic radio button selection backgrounds
    private fun setRadioButtonBackground() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.expense_radio -> {
                    expenseRadio.setBackgroundResource(R.drawable.radio_btn_onselect)
                    incomeRadio.setBackgroundResource(R.drawable.radio_btn_disselect)
                }
                R.id.income_radio -> {
                    expenseRadio.setBackgroundResource(R.drawable.radio_btn_disselect)
                    incomeRadio.setBackgroundResource(R.drawable.radio_btn_onselect)
                }
            }
        }
    }

    // Pass form data to Dashboard using FragmentResult API
    private fun sendTransactionToDashboard() {
        val transactionType = if (expenseRadio.isChecked) "Expense" else "Income"
        val transactionData = bundleOf(
            "title" to descriptionText.text.toString(),
            "amount" to amountInput.text.toString().replace("\u20B9", ""),
            "date" to dateText.text.toString(),
            "type" to transactionType,
            "position" to position,
            "isEditing" to isEditing
        )

        // Send result back to Dashboard fragment
        setFragmentResult("transaction_request_key", transactionData)
    }
}
