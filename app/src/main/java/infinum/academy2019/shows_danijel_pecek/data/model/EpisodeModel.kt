package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeModel(
    @Json(name = "_id")
    val showId: String,

    val title: String,

    val description: String,

    val imageUrl: String,

    val episodeNumber: String,

    @Json(name = "season")
    val seasonNumber: String

)
