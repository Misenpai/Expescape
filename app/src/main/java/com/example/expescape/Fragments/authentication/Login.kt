package com.example.expescape.Fragments.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.expescape.Fragments.mainapp.Dashboard
import com.example.expescape.MainActivity
import com.example.expescape.R

class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val loginButton: Button = view.findViewById(R.id.login_btn)
        loginButton.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(Dashboard())
        }

        return view
    }
}
