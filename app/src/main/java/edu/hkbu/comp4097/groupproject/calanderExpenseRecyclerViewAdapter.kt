package edu.hkbu.comp4097.groupproject

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import edu.hkbu.comp4097.groupproject.R
import edu.hkbu.comp4097.groupproject.data.AppDatabase
import edu.hkbu.comp4097.groupproject.data.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class calanderExpenseRecyclerViewAdapter(
    private val values: List<Expense>
) : RecyclerView.Adapter<calanderExpenseRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_expense_item, parent, false)

        Log.d("calanderExpenseRecyclerViewAdapter", values.size.toString())

//        CoroutineScope(Dispatchers.IO).launch {
//            val dao = AppDatabase.getInstance(view.context).expenseDao()
//            val expenses = dao.getExpense()
//
//            Log.d("calendarFragment", expenses.size.toString())
//            expenses.forEach{
//                Log.d("calendarFragment", it.category)
//            }
//
//        }


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.dateTextView.text = item.date.toString() + '-' + item.month.toString() + '-' + item.year.toString()
        holder.totalTextView.text = "$" + item.amount.toString()
        holder.categoryTextView.text = item.category

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val totalTextView: TextView = view.findViewById(R.id.totalTextView)
        val categoryTextView: TextView = view.findViewById(R.id.categoryTextView)

        override fun toString(): String {
            return super.toString() + " '" + dateTextView.text + "'"
        }
    }

}