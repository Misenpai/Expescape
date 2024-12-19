package com.example.expescape.Fragments.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.expescape.MainActivity
import com.example.expescape.R
import com.example.expescape.Fragments.mainapp.Dashboard

class Signup : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        val signupButton: Button = view.findViewById(R.id.signup_btn)
        signupButton.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(Dashboard())
        }

        return view
    }
}
