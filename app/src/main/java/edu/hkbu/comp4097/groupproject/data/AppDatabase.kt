package edu.hkbu.comp4097.groupproject.data


import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*

@Database(entities = arrayOf(Expense::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun expenseDao() : ExpenseDao companion object {
        private var instance: AppDatabase? = null
        private var isNetworkError: Boolean = false

        suspend fun getInstance(context: Context, username: String) : AppDatabase {
            if (instance != null)
                return instance!!
            //build an instance
            instance = Room.databaseBuilder(context, AppDatabase::class.java, "GroupProject").build()

//            initDB()

            if (username == "Dennistein") {
                isNetworkError = !initDB()
            } else {
                clearAllTables()
            }

            return instance!!
        }

        suspend fun deleteInstance(){
            instance = null
        }

        suspend fun initDB() : Boolean{

//            val nowExpense = instance?.expenseDao()?.getExpense()
//            nowExpense?.forEach{
//                instance?.expenseDao()?.delete(it)
//            }

            SampleData.EXPENSE.forEach { instance?.expenseDao()?.insert(it) }
            return true
        }

        suspend fun insertExpense(expense : Expense){
            instance?.expenseDao()?.insert(expense)
            val nowExpense = instance?.expenseDao()?.getExpense()
            Log.d("AppDatabase", "added new expense")
        }

        suspend fun clearAllTables(){
            instance?.clearAllTables()
        }

    }

}