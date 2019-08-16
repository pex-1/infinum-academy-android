package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowDetails(
    @Json(name = "type")
    val type: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "_id")
    val id: String,

    @Json(name = "likesCount")
    val likesCount: Int,

    @Json(name = "imageUrl")
    val imageUrl: String
)
