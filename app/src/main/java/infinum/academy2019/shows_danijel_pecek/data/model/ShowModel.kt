package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import infinum.academy2019.shows_danijel_pecek.Constants

@JsonClass(generateAdapter = true)
data class ShowModel(
    @Json(name = "_id")
    val showId: String,

    val title: String,

    val imageUrl: String,

    val likesCount: Int,

    @Transient
    val description: String = "",

    @Transient
    var episodeList: ArrayList<EpisodeModel> = arrayListOf()


){
    fun getImage() = Constants.IMAGE_BASE_URL + imageUrl
}