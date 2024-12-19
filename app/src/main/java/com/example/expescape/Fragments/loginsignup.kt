package com.example.expescape.Fragments.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.expescape.MainActivity
import com.example.expescape.R

class loginsignup : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loginsignup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton: Button = view.findViewById(R.id.login_onboard_btn)
        loginButton.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(Login())
        }

        val signupButton: Button = view.findViewById(R.id.signup_onboard_btn)
        signupButton.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(Signup())
        }
    }
}
