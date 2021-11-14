package com.example.map_app.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.map_app.R
import com.example.map_app.databinding.FragmentHomeBinding
import com.example.map_app.service.AuthSharedPreferenceService

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var authSharedPreferencesService : AuthSharedPreferenceService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        authSharedPreferencesService = AuthSharedPreferenceService(this.requireContext())
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!authSharedPreferencesService.isAuthorized){
            this.findNavController().navigate(R.id.on_idle_auth)
        }
        binding.navigateDestinationButton.setOnClickListener {
            logOut()
        }
    }
    private fun logOut(){
        authSharedPreferencesService.deleteCurrentUserData()
        this.findNavController().navigate(R.id.on_idle_auth)
    }
}