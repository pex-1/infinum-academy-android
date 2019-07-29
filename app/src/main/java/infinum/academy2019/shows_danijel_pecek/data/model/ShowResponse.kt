package infinum.academy2019.shows_danijel_pecek.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowResponse(
    @Json(name = "data")
    val showList: List<ShowModel>? = arrayListOf()
)
