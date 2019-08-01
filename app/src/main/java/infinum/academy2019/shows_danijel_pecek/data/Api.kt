package infinum.academy2019.shows_danijel_pecek.data

import infinum.academy2019.shows_danijel_pecek.data.model.EpisodeResponse
import infinum.academy2019.shows_danijel_pecek.data.model.ShowDetailsResponse
import infinum.academy2019.shows_danijel_pecek.data.model.ShowResponse
import infinum.academy2019.shows_danijel_pecek.data.model.user.User
import infinum.academy2019.shows_danijel_pecek.data.model.user.UserLoginResponse
import infinum.academy2019.shows_danijel_pecek.data.model.user.UserRegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("shows")
    fun getShows(): Call<ShowResponse>

    @GET("shows/{id}")
    fun getShowDetails(@Path("id") showId: String): Call<ShowDetailsResponse>

    @GET("shows/{id}/episodes")
    fun getEpisodes(@Path("id") showId: String): Call<EpisodeResponse>

    @POST("users")
    fun userRegister(@Body user: User): Call<UserRegisterResponse>

    @POST("users/sessions")
    fun userLogin(@Body user: User): Call<UserLoginResponse>
}