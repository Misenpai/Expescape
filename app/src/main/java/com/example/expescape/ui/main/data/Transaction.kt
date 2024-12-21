package com.example.expescape.ui.main.data

data class Transaction(
    val title: String,
    val type: String,  // "Expense" or "Income"
    val date: String,
    val amount: String
)

