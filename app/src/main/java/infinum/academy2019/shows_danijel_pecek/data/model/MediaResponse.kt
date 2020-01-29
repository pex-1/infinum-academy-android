package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaResponse(
    @Json(name = "data")
    val media: Media? = null

)

@JsonClass(generateAdapter = true)
data class Media(
    @Json(name = "_id")
    val id: String,
    @Json(name = "path")
    val path: String
)