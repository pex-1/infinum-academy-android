package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LikeResponse(
    @Json(name ="type")
    val type : String,
    @Json(name ="title")
    val title : String,
    @Json(name ="mediaId")
    val mediaId : String,
    @Json(name ="description")
    val description : String,
    @Json(name ="_id")
    val _id : String,
    @Json(name ="likesCount")
    val likesCount : String
)