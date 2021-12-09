package com.example.map_app.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.map_app.R
import com.example.map_app.databinding.FragmentPlacesBinding

class PlacesFragment : Fragment() {

    private lateinit var binding : FragmentPlacesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_places, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}