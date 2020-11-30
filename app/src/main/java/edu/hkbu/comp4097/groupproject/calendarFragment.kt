package edu.hkbu.comp4097.groupproject

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.hkbu.comp4097.groupproject.data.AppDatabase
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class calendarFragment : Fragment() {

    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
        viewManager = LinearLayoutManager(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.calendar_fragment, container, false)

        var calendarView: CalendarView = view.findViewById(R.id.calendarView)
        var addExpenseBut: Button= view.findViewById(R.id.addExpenSeButton)
        val expensRecycler: RecyclerView = view.findViewById(R.id.expensRecycler)


        var date: Long  = calendarView.date

        expensRecycler.apply {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var year = date.let { SimpleDateFormat("yyyy").format(date?.let { Date(it) }).toInt() }
                    var month = date.let { SimpleDateFormat("MM").format(date?.let { Date(it) }).toInt() }

                    val pref = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
                    val username =  pref?.getString("username", "unkonw")
                    Log.d("calendarFragmentaaaa", "sdsd$username")
                    val dao = username?.let {
                        AppDatabase.getInstance(view.context,it).expenseDao()
                    }
                    if (username != null) {
                        Log.d("calendarFragmentaaaa", username)
                    }

                    val expenses = dao?.getExpenseByYearAndMonth(year, month)
                    // Log.d("calendarFragment", expenses.size.toString())

                    CoroutineScope(Dispatchers.Main).launch {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = expenses?.let { calanderExpenseRecyclerViewAdapter(it) }
                        // Log.d("calendarFragment", expenses.size.toString())
                    }
                }catch (e: Exception) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Log.d("calendarFragment", "no expense")
                    }
                }
            }
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            date = calendarView.getDate();

            // expensRecycler
            expensRecycler.apply {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val pref = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
                        val username =  pref?.getString("username", "")
                        val dao = username?.let {
                            AppDatabase.getInstance(view.context,it).expenseDao()
                        }
//                        val expenses = dao.getExpenseByYearAndMonth(year, month + 1)
                        val expenses = dao?.getExpenseByDate(year, month + 1, dayOfMonth)
                        // Log.d("calendarFragment", expenses.size.toString())

                        CoroutineScope(Dispatchers.Main).launch {
                            setHasFixedSize(true)
                            layoutManager = viewManager
                            adapter = expenses?.let { calanderExpenseRecyclerViewAdapter(it) }
                            // Log.d("calendarFragment", expenses.size.toString())
                        }
                    }catch (e: Exception) {
                        CoroutineScope(Dispatchers.Main).launch {
                            Log.d("calendarFragment", "no expense")
                        }
                    }
                }
            }

        }

        addExpenseBut.setOnClickListener {
            activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.action_calendarFragment_to_expenseFragment,
                bundleOf(
                    Pair("date", date)
                )
            )
        }
        return view
    }
}