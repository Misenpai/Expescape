package com.example.expescape.ui.main.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expescape.R

class TransactionAdapter(
    private val transactions: MutableList<Transaction>,
    private val onItemClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private val allTransactions: MutableList<Transaction> = ArrayList(transactions)
    private val filteredTransactions: MutableList<Transaction> = ArrayList(transactions)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_cardview, parent, false)
        return TransactionViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(filteredTransactions[position])
    }

    override fun getItemCount(): Int = filteredTransactions.size

    class TransactionViewHolder(
        itemView: View,
        private val onItemClick: (Transaction) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvExpenseId: TextView = itemView.findViewById(R.id.tvExpenseId)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)

        fun bind(transaction: Transaction) {
            tvTitle.text = transaction.title
            tvExpenseId.text = transaction.type
            tvDate.text = transaction.date
            tvAmount.text = "₹${transaction.amount}"

            itemView.setOnClickListener {
                onItemClick(transaction)
            }
        }
    }

    fun filter(query: String) {
        filteredTransactions.clear()
        if (query.isEmpty()) {
            filteredTransactions.addAll(allTransactions)
        } else {
            filteredTransactions.addAll(
                allTransactions.filter {
                    it.title.contains(query, ignoreCase = true)
                }
            )
        }
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int): Transaction {
        val deletedTransaction = filteredTransactions.removeAt(position)
        allTransactions.remove(deletedTransaction)
        notifyItemRemoved(position)
        return deletedTransaction
    }

    fun restoreItem(transaction: Transaction, position: Int) {
        allTransactions.add(position, transaction)
        filter("")
    }
}