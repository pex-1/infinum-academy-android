package infinum.academy2019.shows_danijel_pecek.data.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLoginResponse(
    @Json(name = "data")
    val userLogin: UserLogin
)

@JsonClass(generateAdapter = true)
data class UserLogin(
    val token: String
)