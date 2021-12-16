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

private val TARGET_LOCATION = Point(59.945933, 30.320045)
private val MAPKIT_API_KEY = "675cc8ac-9f7d-4c56-82c0-e4c4eddb80f4"

class MapFragment : Fragment() {

    private lateinit var binding : FragmentMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
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
                TARGET_LOCATION, 13.0f, 0.0f, 0.0f),
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