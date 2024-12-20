package com.example.expescape.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.expescape.ui.auth.AuthViewModel
import com.example.expescape.ui.main.MainActivity
import com.example.expescape.R
import com.example.expescape.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Onboarding : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_onboarding,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is AuthViewModel.NavigationEvent.NavigateToLoginSignup -> {
                    (activity as? MainActivity)?.loadFragment(loginsignup())
                }

                AuthViewModel.NavigationEvent.NavigateToDashboard -> TODO()
                AuthViewModel.NavigationEvent.NavigateToLogin -> TODO()
                AuthViewModel.NavigationEvent.NavigateToSignup -> TODO()
            }
        }
    }
}