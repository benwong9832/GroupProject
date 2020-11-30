package edu.hkbu.comp4097.groupproject

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.common.io.Files.append
import edu.hkbu.comp4097.groupproject.data.AppDatabase
import edu.hkbu.comp4097.groupproject.data.Category
import edu.hkbu.comp4097.groupproject.data.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class analyzeFragment : Fragment() {

    private val DATA_COUNT = 5
    private val titlesList = mutableListOf<String>()
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
        viewManager = LinearLayoutManager(activity)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.analyze_fragment, container, false)

        var preMonthbutton : Button = view.findViewById(R.id.preMonthbutton)
        var monthTextView : TextView = view.findViewById(R.id.monthTextView)
        var nextMonthbutton : Button = view.findViewById(R.id.nextMonthbutton)
        var categoryRecycler : RecyclerView = view.findViewById(R.id.categoryRecycler)
        var yearTotalTextView : TextView = view.findViewById(R.id.yearTotalTextView)
        var monthTotalTextView : TextView = view.findViewById(R.id.monthTotalTextView)
        val piechart: PieChart = view.findViewById(R.id.bar_pie)


        val current = LocalDateTime.now()
        val month_formatter = DateTimeFormatter.ofPattern("MM")
        val year_formatter = DateTimeFormatter.ofPattern("YYYY")
        var month = current.format(month_formatter).toInt()
        var year = current.format(year_formatter).toInt()

        updateMonthTextView(monthTextView,year, month)
        updateValue(view, year, month, categoryRecycler, piechart, yearTotalTextView, monthTotalTextView )

        preMonthbutton.setOnClickListener{
            if (month == 1) {
                month = 12
                year = year - 1
            }else {
                month = month - 1
            }
            updateMonthTextView(monthTextView,year, month)
            updateValue(view, year, month, categoryRecycler, piechart, yearTotalTextView, monthTotalTextView )

        }
        nextMonthbutton.setOnClickListener{
            if (month == 12) {
                month = 1
                year = year + 1
            }else {
                month = month + 1
            }
            updateMonthTextView(monthTextView,year, month)
            updateValue(view, year, month, categoryRecycler, piechart, yearTotalTextView, monthTotalTextView )
        }



        /*
        val linechart : LineChart = view.findViewById(R.id.bar_line)

        val entries = ArrayList<Entry>()
        entries.add(Entry(0F,4F))
        entries.add(Entry(1f,1f))
        entries.add(Entry(2f,2f))
        entries.add(Entry(3f,4f))
        entries.add(Entry(5f,10f))

        val dataset = LineDataSet(entries,"Customized values")
        dataset.color = ContextCompat.getColor(view.context,R.color.colorPrimary)
        dataset.valueTextColor = ContextCompat.getColor(view.context,R.color.colorPrimaryDark)

        val xAxis = linechart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        val months = arrayOf("Jan", "Feb", "Mar", "Apr","May","June")
        val formatter = IAxisValueFormatter{
                value, axis ->   months[value.toInt()]
        }

        xAxis.granularity = 1f
        xAxis.valueFormatter = formatter

        val yAxisRight = linechart.axisRight
        yAxisRight.setEnabled(false)

        val yAxisLeft = linechart.axisLeft
        yAxisLeft.setGranularity(1f)

        // Setting Data
        val data = LineData(dataset)
        linechart.setData(data)
        linechart.animateX(2500)
        //refresh
        linechart.invalidate()




        postToList()

        var view_page2 :ViewPager2 = view.findViewById(R.id.view_page2)
        view_page2.adapter = ViewPagerAdapter(titlesList)
        view_page2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        var indicator :CircleIndicator3 = view.findViewById(R.id.indicator)
        indicator.setViewPager(view_page2)
        */
        return view
    }

    private fun addToList(title: String){
        titlesList.add(title)
    }

    private fun postToList(){
        for (i in 1..5){
            addToList("Title $i")
        }
    }

    fun updatePieChart(piechart :PieChart, expense: List<Expense>){
        var types: MutableList<String> = ArrayList()
        var persentage: MutableList<Float> = ArrayList()
        var totalAmount: Float = 0f

        expense.forEach{
            if (!types.contains(it.category)){
                types.add(it.category)
            }
            totalAmount += it.amount.toFloat()
        }

        types.forEach{ type: String ->
            var tAmount : Float = 0f
            expense.forEach {
                if (it.category == type){
                    tAmount += it.amount
                }
            }
            var per : Float = (tAmount / totalAmount)
            Log.d("analyzeFragment", per.toString())
            String.format("%.2f", per)
            persentage.add((tAmount / totalAmount).toFloat())
        }

        val entries = ArrayList<PieEntry>()
        types.forEachIndexed { index, type: String ->
            var v =  "%.2f".format(persentage[index]).substring(2, 4).toFloat()
            entries.add(PieEntry(v, type))
        }

        val colors = ArrayList<Int>()
        colors.add(resources.getColor(R.color.chartColor1))
        colors.add(resources.getColor(R.color.chartColor2))
        colors.add(resources.getColor(R.color.chartColor3))
        colors.add(resources.getColor(R.color.chartColor4))
        colors.add(resources.getColor(R.color.chartColor5))
        colors.add(resources.getColor(R.color.chartColor6))
        colors.add(resources.getColor(R.color.chartColor7))
        colors.add(resources.getColor(R.color.chartColor8))
        colors.add(resources.getColor(R.color.chartColor9))

        val dataSet = PieDataSet(entries,"label")
        dataSet.setColors(colors)
        dataSet.setValueTextSize(15f)

        val pieData = PieData(dataSet)
        pieData.setDrawValues(true)

        piechart.setData(pieData)
        piechart.setEntryLabelColor(Color.BLACK)
        piechart.setEntryLabelTextSize(15f)
        piechart.setCenterTextSize(15f)
        piechart.spin( 1000, 0F,90f, Easing.EasingOption.EaseInOutQuad);


        piechart.invalidate()
    }

    fun updateValue(view: View, year: Int, month: Int, categoryRecycler :RecyclerView, piechart : PieChart, yearTotalTextView : TextView, monthTotalTextView : TextView){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
                val username =  pref?.getString("username", "")
                val dao = username?.let {
                    AppDatabase.getInstance(view.context,it).expenseDao()
                }

                val expenses = dao?.getExpenseByYearAndMonth(year, month)
                val calExpenses = dao?.getExpense()
                var yearTotalForView = 0
                var monthTotalForView = 0

                var category : ArrayList<Category> = ArrayList()

                var types: MutableList<String> = ArrayList()

                if (calExpenses != null) {
                    calExpenses.forEach{
                        if (!types.contains(it.category)){
                            types.add(it.category)
                        }
                    }
                }

                types.forEachIndexed{ index, type: String ->
                    var yeartotalPrice : Int = 0
                    var monthTotalPrice : Int = 0

                    if (calExpenses != null) {
                        calExpenses.forEach {
                            if (it.year == year && it.category == type){
                                yeartotalPrice += it.amount
                            }

                            if (it.year == year && it.month == month && it.category == type){
                                monthTotalPrice += it.amount
                            }

                            if (index == 0 && it.year == year && it.month == month){
                                monthTotalForView += it.amount
                            }

                            if (index == 0 && it.year == year){
                                yearTotalForView += it.amount
                            }
                        }
                    }

                    var cat : Category = Category(type, yeartotalPrice, monthTotalPrice)
                    category.add(cat)
                }

                categoryRecycler.apply {
                    CoroutineScope(Dispatchers.Main).launch {
                        // setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = analyzeCategoryRecyclerViewAdapter(category)
                        //Log.d("calendarFragment", expenses.size.toString())
                        if (expenses != null) {
                            updatePieChart(piechart, expenses)
                        }
                        yearTotalTextView.text = "$ " + yearTotalForView
                        monthTotalTextView.text = "Month Expense $ " + monthTotalForView
                    }
                }

//                CoroutineScope(Dispatchers.Main).launch {
//                    Log.d("analyzeFragment", expenses.size.toString())
//                    Log.d("analyzeFragment category", category.size.toString())
//                    updatePieChart(piechart, expenses)
//                    analyzeCategoryRecyclerViewAdapter(category)
//                }
            }catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("analyzeFragment", "no expense")
                }
            }
        }
    }

    fun updateMonthTextView(monthTextView :TextView, year: Int, month :Int){
        when (month) {
            1 -> monthTextView.text = year.toString() + " Jan"
            2 -> monthTextView.text = year.toString() + " Feb"
            3 -> monthTextView.text = year.toString() + " Mar"
            4 -> monthTextView.text = year.toString() + " Apr"
            5 -> monthTextView.text = year.toString() + " May"
            6 -> monthTextView.text = year.toString() + " Jun"
            7 -> monthTextView.text = year.toString() + " Jul"
            8 -> monthTextView.text = year.toString() + " Aug"
            9 -> monthTextView.text = year.toString() + " Sep"
            10 -> monthTextView.text = year.toString() + " Oct"
            11 -> monthTextView.text = year.toString() + " Nov"
            12 -> monthTextView.text = year.toString() + " Dec"
            else -> { // Note the block
                print("x is neither 1 nor 2")
            }
        }
    }

}
