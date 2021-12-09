package com.example.map_app

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MainApplication : Application() {
    private val MAPKIT_API_KEY = "675cc8ac-9f7d-4c56-82c0-e4c4eddb80f4"

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }
}