package infinum.academy2019.shows_danijel_pecek.data

import infinum.academy2019.shows_danijel_pecek.data.model.*
import infinum.academy2019.shows_danijel_pecek.data.model.user.User
import infinum.academy2019.shows_danijel_pecek.data.model.user.UserLoginResponse
import infinum.academy2019.shows_danijel_pecek.data.model.user.UserRegisterResponse
import retrofit2.Call
import retrofit2.http.*
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST



interface Api {

    @GET("shows")
    fun getShows(): Call<ShowResponse>

    @GET("shows/{id}")
    fun getShowDetails(@Path("id") showId: String): Call<ShowDetailsResponse>

    @GET("shows/{id}/episodes")
    fun getEpisodes(@Path("id") showId: String): Call<EpisodesResponse>

    @POST("users")
    fun userRegister(@Body user: User): Call<UserRegisterResponse>

    @POST("users/sessions")
    fun userLogin(@Body user: User): Call<UserLoginResponse>

    @GET("episodes/{id}")
    fun getEpisode(@Path("id") episodeId: String): Call<SingleEpisodeResponse>

    @GET("episodes/{id}/comments")
    fun getComments(@Path("id") episodeId: String): Call<CommentsResponse>

    @POST("comments")
    fun postComment(@Header ("Authorization") token: String, @Body comment: MessageModel) : Call<MessageResponse>

    @POST("media")
    @Multipart
    fun uploadMedia(@Header ("Authorization") token: String, @Part("file\"; filename=\"image.jpg\"") request: RequestBody): Call<MediaResponse>

    @POST("episodes")
    fun postEpisode(@Header ("Authorization") token: String, @Body episode: EpisodePost) : Call<EpisodePostResponse>

    @POST("shows/{id}/dislike")
    fun dislikeShow(@Header ("Authorization") token: String, @Path("id") showId: String) : Call<LikeResponse>

    @POST("shows/{id}/like")
    fun likeShow(@Header ("Authorization") token: String, @Path("id") showId: String) : Call<LikeResponse>

}