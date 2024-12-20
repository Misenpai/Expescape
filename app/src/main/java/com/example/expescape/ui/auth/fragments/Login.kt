package com.example.expescape.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.expescape.ui.main.MainActivity
import com.example.expescape.R
import com.example.expescape.ui.main.fragments.Dashboard
import com.example.expescape.ui.auth.AuthViewModel
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import com.example.expescape.util.Result
import com.google.android.gms.auth.api.signin.GoogleSignIn

@AndroidEntryPoint
class Login : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        emailField = view.findViewById(R.id.login_email_textfield)
        passwordField = view.findViewById(R.id.login_password_textfield)
        val loginButton: Button = view.findViewById(R.id.login_btn)
        val googleSignInButton: ImageButton = view.findViewById(R.id.loginGoogle)

        // Set click listeners
        loginButton.setOnClickListener {
            performLogin()
        }

        googleSignInButton.setOnClickListener {
            viewModel.initiateGoogleSignIn()
        }

        setupObservers()
        setupGoogleSignIn()
    }

    private fun performLogin() {
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.performLogin(email, password)
    }

    private fun setupObservers() {
        viewModel.authState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading indicator if needed
                }
                is Result.Success -> {
                    // Handle success if needed
                }
                is Result.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is AuthViewModel.NavigationEvent.NavigateToDashboard -> {
                    (activity as? MainActivity)?.loadFragment(Dashboard())
                }
                else -> {}
            }
        }
    }

    private fun setupGoogleSignIn() {
        viewModel.googleSignInIntent.observe(viewLifecycleOwner) { intent ->
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            viewModel.handleGoogleSignInResult(task)
        }
    }
}