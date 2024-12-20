package com.example.expescape.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.expescape.R
import com.example.expescape.ui.main.fragments.Dashboard
import com.example.expescape.ui.onboarding.Onboarding
import com.example.expescape.ui.onboarding.loginsignup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            loadFragment(Onboarding())
        }
    }

    fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        if (fragment is loginsignup || fragment is Dashboard) {
            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.popBackStack()
            }
        }

        transaction.replace(R.id.fragment_container, fragment)
        if (fragment !is Onboarding && fragment !is Dashboard) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        when (currentFragment) {
            is loginsignup -> finish()
            is Dashboard -> finish()
            else -> super.onBackPressed()
        }
    }
}