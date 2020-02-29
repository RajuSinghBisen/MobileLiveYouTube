package com.example.mobileliveyoutube

import android.app.Application
import com.example.mobileliveyoutube.repositories.LocalPreferences

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize singletons
        LocalPreferences.initialize(this)
    }
}