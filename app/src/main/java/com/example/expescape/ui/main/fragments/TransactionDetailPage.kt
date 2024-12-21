package com.example.expescape.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.expescape.R

class TransactionDetailPage : Fragment() {
    private var position: Int = -1  // Add this to track item position

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_detail_page, container, false)

        setupTransactionDetails(view)
        setupButtonListeners(view)
        return view
    }

    private fun setupButtonListeners(view: View) {
        // Back button
        view.findViewById<ImageView>(R.id.expense_back_btn)?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Edit button
        view.findViewById<ImageView>(R.id.expense_edit_btn)?.setOnClickListener {
            navigateToEditForm()
        }

        // Delete button
        view.findViewById<ImageView>(R.id.expense_delete_btn)?.setOnClickListener {
            deleteTransaction()
        }
    }

    private fun setupTransactionDetails(view: View) {
        arguments?.let { args ->
            position = args.getInt("position", -1)  // Get position from arguments
            view.apply {
                findViewById<TextView>(R.id.expense_title)?.text = args.getString("expense_id")
                findViewById<TextView>(R.id.expense_date)?.text = args.getString("date")
                findViewById<EditText>(R.id.description_text_expense)?.setText(args.getString("title"))
                findViewById<EditText>(R.id.amount_input_expense)?.setText(
                    args.getString("amount")?.replace("₹", "")?.trim()
                )
                findViewById<TextView>(R.id.rupee_symbol)?.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateToEditForm() {
        val transactionForm = TransactionForm().apply {
            arguments = Bundle().apply {
                putBoolean("isEditing", true)
                putString("expense_id", arguments?.getString("expense_id"))
                putString("date", arguments?.getString("date"))
                putString("title", arguments?.getString("title"))
                putString("amount", arguments?.getString("amount"))
                putInt("position", position)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, transactionForm)
            .addToBackStack(null)
            .commit()
    }

    private fun deleteTransaction() {
        setFragmentResult("delete_transaction", bundleOf("position" to position))
        parentFragmentManager.popBackStack()
    }
}
