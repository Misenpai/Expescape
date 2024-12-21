package com.example.expescape.ui.main.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expescape.R
import com.example.expescape.ui.main.data.Transaction
import com.example.expescape.ui.main.data.TransactionAdapter
import com.google.android.material.snackbar.Snackbar

class Dashboard : Fragment() {

    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var searchField: androidx.appcompat.widget.AppCompatEditText
    private lateinit var dashboardLayout: LinearLayout
    private lateinit var settingsLayout: LinearLayout
    private lateinit var dashboardIcon: ImageView
    private lateinit var settingsIcon: ImageView
    private lateinit var homeText: TextView
    private lateinit var settingsText: TextView
    private lateinit var btnAddNew: View

    private val transactions = mutableListOf<Transaction>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        initializeViews(view)
        setupRecyclerView()
        setupNavigation()
        setupListeners()
        setupSwipeToDelete()

        return view
    }

    private fun initializeViews(view: View) {
        transactionsRecyclerView = view.findViewById(R.id.transactionsRecyclerView)
        searchField = view.findViewById(R.id.search_field)
        dashboardLayout = view.findViewById(R.id.dashboardLayout)
        settingsLayout = view.findViewById(R.id.settingsLayout)
        dashboardIcon = view.findViewById(R.id.dashboard)
        settingsIcon = view.findViewById(R.id.setting)
        homeText = view.findViewById(R.id.hometext)
        settingsText = view.findViewById(R.id.searchText)
        btnAddNew = view.findViewById(R.id.btnAddNew)

        // Set up Add New button click listener
        btnAddNew.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TransactionForm())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(transactions) { transaction ->
            val bundle = Bundle().apply {
                putString("expense_id", transaction.type)
                putString("date", transaction.date)
                putString("title", transaction.title)
                putString("amount", transaction.amount)
            }

            val detailFragment = TransactionDetailPage().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        transactionsRecyclerView.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupNavigation() {
        selectItem(dashboardLayout)

        dashboardLayout.setOnClickListener {
            selectItem(dashboardLayout)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Dashboard())
                .commit()
        }

        settingsLayout.setOnClickListener {
            selectItem(settingsLayout)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Dashboard())
                .commit()
        }
    }

    private fun setupListeners() {
        setFragmentResultListener("transaction_request_key") { _, bundle ->
            val newTransaction = Transaction(
                title = bundle.getString("title") ?: "Unknown",
                type = bundle.getString("type") ?: "Expense",
                date = bundle.getString("date") ?: "N/A",
                amount = bundle.getString("amount") ?: "0"
            )
            addTransaction(newTransaction)
        }

        searchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                transactionAdapter.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedTransaction = transactionAdapter.deleteItem(position)

                Snackbar.make(transactionsRecyclerView, "Transaction Deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        transactionAdapter.restoreItem(deletedTransaction, position)
                    }.show()
            }
        })
        itemTouchHelper.attachToRecyclerView(transactionsRecyclerView)
    }

    private fun addTransaction(transaction: Transaction) {
        transactions.add(0, transaction)
        transactionAdapter.restoreItem(transaction, 0)
        transactionsRecyclerView.scrollToPosition(0)
    }

    private fun selectItem(selectedLayout: LinearLayout) {
        val primaryColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        val secondaryColor = ContextCompat.getColor(requireContext(), R.color.colorControlNormal)

        dashboardIcon.setColorFilter(secondaryColor)
        settingsIcon.setColorFilter(secondaryColor)
        homeText.setTextColor(secondaryColor)
        settingsText.setTextColor(secondaryColor)

        when (selectedLayout.id) {
            R.id.dashboardLayout -> {
                dashboardIcon.setColorFilter(primaryColor)
                homeText.setTextColor(primaryColor)
            }
            R.id.settingsLayout -> {
                settingsIcon.setColorFilter(primaryColor)
                settingsText.setTextColor(primaryColor)
            }
        }
    }
}
