package com.example.map_app.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.map_app.R
import com.example.map_app.databinding.FragmentProfileBinding
import com.example.map_app.service.AuthSharedPreferenceService

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var authSharedPreferencesService : AuthSharedPreferenceService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        authSharedPreferencesService = AuthSharedPreferenceService(this.requireContext())
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!authSharedPreferencesService.isAuthorized){
            this.findNavController().navigate(R.id.on_idle_auth)
        }
        binding.userInfo.text = "Профиль: ${authSharedPreferencesService.loadCurrentUser().login}"
        binding.navigateDestinationButton.setOnClickListener {
            logOut()
        }
    }

    private fun logOut(){
        authSharedPreferencesService.deleteCurrentUserData()
        this.findNavController().navigate(R.id.on_idle_auth)
    }
}