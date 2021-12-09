package com.example.map_app.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.map_app.R
import com.example.map_app.databinding.FragmentMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

class MapFragment : Fragment() {

    private lateinit var binding : FragmentMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(this.context)
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mapview.map.move(
            CameraPosition(
                Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),null)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }
}