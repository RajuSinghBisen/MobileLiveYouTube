package com.example.mobileliveyoutube


import com.example.mobileliveyoutube.modules.youtubemainscreen.YoutubeModel
import com.example.mobileliveyoutube.services.YoutubeRemoteService
import kotlinx.coroutines.delay

/** This repository represents the abstraction for all data processing requirements related
 * to NewsContent:
 */
object YoutubeMainRepository {

    suspend fun getYoutubeCompleteData(): YoutubeModel {
        delay(20)
        return YoutubeRemoteService.getYoutubeData()
    }
}