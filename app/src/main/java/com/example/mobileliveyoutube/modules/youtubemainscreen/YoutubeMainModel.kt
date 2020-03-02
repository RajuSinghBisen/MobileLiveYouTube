package com.example.mobileliveyoutube.modules.youtubemainscreen


import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
@JsonClass(generateAdapter = true)
@Parcelize
data class YoutubeModel(
    val pageInfo: YouTubePageInfo? = null,
    val items : List<YoutubeSnippet>? = null
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class YoutubeSnippet(
    val snippet: YoutubeChild?
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class YoutubeChild(
    val title: String? = null,
    val thumbnails: YouTubeThumbnailInfo? = null
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class YouTubePageInfo(
    val totalResults: Int? = null,
    val resultsPerPage: Int? = null
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class YouTubeThumbnailInfo(
    val medium: YouTubeThumbnailMediumInfo? = null
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class YouTubeThumbnailMediumInfo(
    val url: String? = null,
    val width: Int? = null,
    val height: Int? = null
): Parcelable