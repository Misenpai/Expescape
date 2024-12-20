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
class Signup : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var nameField: EditText
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
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        nameField = view.findViewById(R.id.signup_name_textfield)
        emailField = view.findViewById(R.id.signup_email_textfield)
        passwordField = view.findViewById(R.id.signup_password_textfield)
        val signupButton: Button = view.findViewById(R.id.signup_btn)
        val googleSignInButton: ImageButton = view.findViewById(R.id.signupGoogle)

        // Set click listeners
        signupButton.setOnClickListener {
            performSignup()
        }

        googleSignInButton.setOnClickListener {
            viewModel.initiateGoogleSignIn()
        }

        setupObservers()
        setupGoogleSignIn()
    }

    private fun performSignup() {
        val name = nameField.text.toString().trim()
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.performSignup(email, password)
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