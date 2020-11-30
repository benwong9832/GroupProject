package edu.hkbu.comp4097.groupproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import edu.hkbu.comp4097.groupproject.data.Code
import edu.hkbu.comp4097.groupproject.data.Code.LOGIN_RESULT
import edu.hkbu.comp4097.groupproject.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        val pref = this?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        var user = FirebaseAuth.getInstance().currentUser
        Log.d("MainActivityaaaa", user.toString())
        // zzx@1c2216a
        if (user != null) {
            pref?.edit()?.putString("username", user.displayName.toString())?.commit()
        }

//        pref?.edit()?.putString("username", intent.getStringExtra("username"))?.commit()
//        intent.getStringExtra("username")?.let { Log.d("MainActivityaaaa", it) }
//        val username =  pref?.getString("username", "unkonw")
//        if (username != null) {
//            Log.d("MainActivityaaaa", username)
//        }

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.calendarFragment, R.id.analyzeFragment, R.id.userFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onResume() {
        super.onResume()
        //The SwitchPreferenceCompat will automatically store the preference value in a key-value dictionary.
        if (getSharedPreferences("${packageName}_preferences", 0).getBoolean("dark_mode", false)) {
            //switch to night mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            //switch to day mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        if (FirebaseAuth.getInstance().currentUser == null)
            startActivityForResult(Intent(this, LoginActivity::class.java), Code.LOGIN_RESULT)
    }



    fun logout(view: View) {

        FirebaseAuth.getInstance().signOut()
        if (FirebaseAuth.getInstance().currentUser == null)
            startActivityForResult(Intent(this, LoginActivity::class.java), LOGIN_RESULT)

        val Bye = getString(R.string.Bye)

        val displayName: Int = R.id.editTextDisplayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$Bye $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(this@MainActivity, "There is no back action", Toast.LENGTH_LONG).show()
        return
    }
}