package infinum.academy2019.shows_danijel_pecek.data.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserRegisterResponse(
    @Json(name = "data")
    val userRegister: UserRegister
)

@JsonClass(generateAdapter = true)
data class UserRegister(
    @Json(name = "type")
    val type: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "_id")
    val id: String
)