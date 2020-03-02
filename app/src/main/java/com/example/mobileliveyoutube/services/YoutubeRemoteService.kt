package com.example.mobileliveyoutube.services
import com.example.mobileliveyoutube.BuildConfig
import com.example.mobileliveyoutube.executeApiRequest
import com.example.mobileliveyoutube.modules.youtubemainscreen.YoutubeModel

object YoutubeRemoteService : YoutubeService {

    override suspend fun getYoutubeData(): YoutubeModel {
        var result = executeApiRequest(
            //TODO Later the Key for Query Param needs to be store in BuildConfig file
            { YoutubeRetrofitServices.getInstance().getYoutubeVideo("snippet","PLFs4vir_WsTwEd-nJgVJCZPNL3HALHHpF","AIzaSyBbldvvDLHo9wgku368QDSkgqkG4D6c2dU") },
            { it }
        )
        return result.data!!
    }
}