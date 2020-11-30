package edu.hkbu.comp4097.groupproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.hkbu.comp4097.groupproject.ui.signup.SignupFragment

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SignupFragment.newInstance())
                .commitNow()
        }
    }
}