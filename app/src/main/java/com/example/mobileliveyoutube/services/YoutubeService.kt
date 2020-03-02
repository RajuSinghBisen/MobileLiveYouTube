package com.example.mobileliveyoutube.services

import com.example.mobileliveyoutube.modules.youtubemainscreen.YoutubeModel

interface YoutubeService {

    suspend fun getYoutubeData(): YoutubeModel

}