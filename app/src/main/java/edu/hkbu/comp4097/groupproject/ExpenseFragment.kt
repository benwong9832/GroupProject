package edu.hkbu.comp4097.groupproject

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import edu.hkbu.comp4097.groupproject.data.AppDatabase
import edu.hkbu.comp4097.groupproject.data.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class ExpenseFragment : Fragment() {

    private var date: Long? = null
    var categorys = arrayOf("Foot", "Phone And Net", "Home", "Transportation", "Game", "Book")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getLong("date")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.expense_fragment, container, false)

        val dataTextView: TextView = view.findViewById(R.id.dataTextView)
        val amountEditTextNumberSigned: EditText = view.findViewById(R.id.amountEditTextNumberSigned)
        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)

        val submitButton: Button = view.findViewById(R.id.submitBut)


        val dateString: String = SimpleDateFormat("MM/dd/yyyy").format(date?.let { Date(it) })
        dataTextView.text = dateString

        var selectCategory: String = ""
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(view.context, android.R.layout.simple_spinner_item, categorys)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.setAdapter(adapter)
        categorySpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = adapterView.getItemAtPosition(position)
                if (item != null) {
                    selectCategory = item.toString()
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })

        submitButton.setOnClickListener {
            if (amountEditTextNumberSigned.text != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val pref = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
                        val username =  pref?.getString("username", "")

                        val dao = username?.let {
                            AppDatabase.getInstance(view.context,it).expenseDao()
                        }
                        val expenses = dao?.getExpense()
                        var id = (expenses?.size ?: 0) + 1
                        var year = date.let { SimpleDateFormat("yyyy").format(date?.let { Date(it) }).toInt() }
                        var month = date.let { SimpleDateFormat("MM").format(date?.let { Date(it) }).toInt() }
                        var day = date.let { SimpleDateFormat("dd").format(date?.let { Date(it) }).toInt() }
                        var amount = Integer.parseInt(amountEditTextNumberSigned.getText().toString())
                        var newExpense : Expense = Expense(id, year, month, day, amount, selectCategory)

                        AppDatabase.insertExpense(newExpense)

                        Log.d("ExpenseFragment", newExpense.id.toString())
                        Log.d("ExpenseFragment", newExpense.year.toString())
                        Log.d("ExpenseFragment", newExpense.month.toString())
                        Log.d("ExpenseFragment->", newExpense.date.toString())
                        Log.d("ExpenseFragment->", newExpense.amount.toString())
                        Log.d("ExpenseFragment", newExpense.category.toString())



                        CoroutineScope(Dispatchers.Main).launch {
                            activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.action_expenseFragment_to_calendarFragment,
                                bundleOf(
                                    Pair("date", date)
                                )
                            )
                        }
                    }catch (e: Exception) {
                        CoroutineScope(Dispatchers.Main).launch {
                            Log.d("calendarFragment", "no expense")
                        }
                    }
                }
            }
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)


        Log.d("ExpenseFragment", date.toString())

        return view
    }


}