package com.example.expescape.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.expescape.Fragments.authentication.loginsignup
import com.example.expescape.MainActivity
import com.example.expescape.R

class Onboarding : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigateToLoginSignupButton: Button = view.findViewById(R.id.get_started)
        navigateToLoginSignupButton.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(loginsignup())
        }
    }
}
