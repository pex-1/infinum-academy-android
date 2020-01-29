package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageResponse(
    @Json(name = "data")
    val messages: MessageBody
)

@JsonClass(generateAdapter = true)
data class MessageBody(
    @Json(name = "text")
    val text: String,

    @Json(name = "episodeId")
    val episodeId: String,

    @Json(name = "userId")
    val userId: String,

    @Json(name = "userEmail")
    val userEmail: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "_id")
    val id: String
)

@JsonClass(generateAdapter = true)
data class MessageModel(
    @Json(name = "text")
    val text: String,

    @Json(name = "episodeId")
    val episodeId: String
)