package com.example.mobileliveyoutube.modules.youtubemainscreen


import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
@JsonClass(generateAdapter = true)
@Parcelize
data class YoutubeModel(
    val kind: String? = null,
    val data: NewsChildrenData? = null
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class NewsChildrenData(
    val approved_at_utc: String? = null,
    val subreddit: String? = null,
    val selftext: String? = null,
    val author_fullname: String? = null,
    val title: String? = null,
    val thumbnail: String? = null
): Parcelable