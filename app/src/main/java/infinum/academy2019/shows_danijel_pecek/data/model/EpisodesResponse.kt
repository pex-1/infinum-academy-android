package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodesResponse(
    @Json(name = "data")
    val episodeList: List<EpisodeModel>? = arrayListOf()
)