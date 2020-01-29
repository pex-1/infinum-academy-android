package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import infinum.academy2019.shows_danijel_pecek.Constants

@JsonClass(generateAdapter = true)
data class ShowModel(
    @Json(name = "_id")
    val showId: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "imageUrl")
    val imageUrl: String,

    @Json(name = "likesCount")
    val likesCount: Int,

    @Transient
    val description: String = "",

    @Transient
    var episodeList: ArrayList<EpisodeModel> = arrayListOf()


){
    fun getImage() = Constants.IMAGE_BASE_URL + imageUrl
}