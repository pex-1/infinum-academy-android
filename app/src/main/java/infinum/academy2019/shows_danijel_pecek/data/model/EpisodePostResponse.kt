package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class EpisodePostResponse(
    @Json(name = "data")
    val episode: EpisodePostResponseData

)

@JsonClass(generateAdapter = true)
data class EpisodePostResponseData(
    @Json(name = "showId")
    val showId: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "episodeNumber")
    val episodeNumber: String,

    @Json(name = "season")
    val season: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "_id")
    val _id: String,

    @Json(name = "imageUrl")
    val imageUrl: String
)