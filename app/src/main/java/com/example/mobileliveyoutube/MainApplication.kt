package com.example.mobileliveyoutube

import android.app.Application
import com.example.mobileliveyoutube.repositories.LocalPreferences
import com.example.mobileliveyoutube.services.YoutubeRetrofitServices

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize singletons
        LocalPreferences.initialize(this)
        YoutubeRetrofitServices.initialize(this)
    }
}