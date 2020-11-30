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
import edu.hkbu.comp4097.groupproject.data.Category
import edu.hkbu.comp4097.groupproject.data.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class analyzeCategoryRecyclerViewAdapter(
    private val values: List<Category>
) : RecyclerView.Adapter<analyzeCategoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_analyze_item, parent, false)

        Log.d("analyzeCategoryRecyclerViewAdapter", values.size.toString())

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.typeTextView.text = item.category
        holder.yearTotalTextView.text = "$ " + item.yeartotalPrice.toString()
        holder.totalTextView.text = "$ " + item.monthTotalPrice.toString()

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeTextView: TextView = view.findViewById(R.id.typeTextView)
        val yearTotalTextView: TextView = view.findViewById(R.id.yearTotalTextView)
        val totalTextView: TextView = view.findViewById(R.id.totalTextView)

        override fun toString(): String {
            return super.toString() + " '" + typeTextView.text + "'"
        }
    }

}