package com.example.mobileliveyoutube.services
import android.content.Context
import com.example.mobileliveyoutube.BuildConfig
import com.example.mobileliveyoutube.modules.youtubemainscreen.YoutubeModel
import com.example.mobileliveyoutube.utils.SingletonHolder
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface YoutubeRetrofitServices {
    companion object: SingletonHolder<YoutubeRetrofitServices, Context>(
        {
            val timeout = 20.00
            Retrofit.Builder()
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder()
                            // .add(..)  // Add specific adapters if required
                            .build()
                    )
                )
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .client(
                    OkHttpClient.Builder().apply {
                        if (BuildConfig.DEBUG ) {
                            val interceptor = HttpLoggingInterceptor()
                            interceptor.level = HttpLoggingInterceptor.Level.BODY
                            addInterceptor(interceptor)
                        }
                    }
                        //.addInterceptor(createOkHttpCacheInterceptor(it)) // Cache?
                        .callTimeout(timeout.toLong(), TimeUnit.SECONDS)
                        .build()
                )
                .build()
                .create(YoutubeRetrofitServices::class.java)
        }
    ) {
        private const val QUERY_PARAM_PART : String = "part"
        const val QUERY_PARAM_PLAYLISTID                  : String = "playlistId" // aka `_id`
        const val QUERY_PARAM_KEY                   : String = "key" // aka `username`

        // Path-Params definition
        private const val ENDPOINT_NEWS_GET                 : String = "playlistItems"
    }

    @GET(ENDPOINT_NEWS_GET)
    suspend fun getYoutubeVideo(@Query(QUERY_PARAM_PART) part: String,
                                @Query(QUERY_PARAM_PLAYLISTID) playlistId: String,
                                @Query(QUERY_PARAM_KEY) key: String

    ): Response<YoutubeModel>
}