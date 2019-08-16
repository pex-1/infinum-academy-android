package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentsResponse (
    @Json(name = "data")
    val comments: List<Comment>
)

@JsonClass(generateAdapter = true)
data class Comment(
    @Json(name = "_id")
    val id: String,

    @Json(name = "episodeId")
    val episodeId: String,

    @Json(name = "text")
    val comment: String,

    @Json(name = "userEmail")
    val userEmail: String
)