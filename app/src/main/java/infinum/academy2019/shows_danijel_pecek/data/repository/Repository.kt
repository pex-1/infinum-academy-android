package infinum.academy2019.shows_danijel_pecek.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import infinum.academy2019.shows_danijel_pecek.MyShowsApp
import infinum.academy2019.shows_danijel_pecek.data.Api
import infinum.academy2019.shows_danijel_pecek.data.RetrofitClient
import infinum.academy2019.shows_danijel_pecek.data.database.LikesDatabase
import infinum.academy2019.shows_danijel_pecek.data.database.ShowLikes
import infinum.academy2019.shows_danijel_pecek.data.model.*
import infinum.academy2019.shows_danijel_pecek.data.model.user.User
import infinum.academy2019.shows_danijel_pecek.data.model.user.UserLoginResponse
import infinum.academy2019.shows_danijel_pecek.data.model.user.UserRegisterResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import java.io.File
import java.util.concurrent.Executors


object Repository {

    private val database: LikesDatabase = Room.databaseBuilder(
        MyShowsApp.instance,
        LikesDatabase::class.java, "likes-database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val executor = Executors.newSingleThreadExecutor()

    fun update(showLikes: ShowLikes) {
        executor.execute {
            database.likesDao().update(showLikes)
        }
    }

    fun insert(showLikes: ShowLikes) {
        executor.execute {
            database.likesDao().insert(showLikes)
        }
    }


    private val showLike = MutableLiveData<ShowLikes>()
    fun liveDataLikes(): LiveData<ShowLikes> = showLike


    fun getShow(id: String) {
        executor.execute {
            showLike.postValue(database.likesDao().getShow(id))
        }
    }


    private var apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)


    private var token = ""

    private var loadingShowDetails: Boolean = false

    private var loadingEpisodes: Boolean = false

    fun setToken(token: String) {
        this.token = token
    }

    fun isLoading() = loadingShowDetails && loadingEpisodes


    private val apiError = MutableLiveData<String>()
    fun apiErrorLiveData(): LiveData<String> = apiError

    fun resetErrorLiveData(){
        apiError.value = null
    }

    private val shows = MutableLiveData<List<ShowModel>>()
    fun liveData(): LiveData<List<ShowModel>> = shows

    private val episodesProgressBar = MutableLiveData<Boolean>()
    fun liveDataEpisodesProgressBar(): LiveData<Boolean> = episodesProgressBar


    private val episodes = MutableLiveData<List<EpisodeModel>>()
    fun liveDateEpisodes(): LiveData<List<EpisodeModel>> = episodes

    private val details = MutableLiveData<ShowDetails>()
    fun liveDataDetails(): LiveData<ShowDetails> = details

    private val episode = MutableLiveData<SingleEpisode>()
    fun liveDataSingleEpisode(): LiveData<SingleEpisode> = episode

    private val comments = MutableLiveData<List<Comment>>()
    fun liveDataComments(): LiveData<List<Comment>> = comments

    private val media = MutableLiveData<MediaResponse>()
    fun liveDataMedia(): LiveData<MediaResponse> = media

    private val postEpisodeResponse = MutableLiveData<EpisodePostResponse>()
    fun liveDataPostEpisode(): LiveData<EpisodePostResponse> = postEpisodeResponse


    private val userRegistrationSuccessful = MutableLiveData<Boolean>()
    fun registrationResponse(): LiveData<Boolean> = userRegistrationSuccessful

    private val userLoginResponse = MutableLiveData<UserLoginResponse>()
    fun loginResponse(): LiveData<UserLoginResponse> = userLoginResponse

    private val commentPosted = MutableLiveData<MessageResponse>()
    fun commentPosted(): LiveData<MessageResponse> = commentPosted

    private val showLiked = MutableLiveData<LikeResponse>()
    fun showLikedResponse(): LiveData<LikeResponse> = showLiked

    private val showDisliked = MutableLiveData<LikeResponse>()
    fun showDislikedResponse(): LiveData<LikeResponse> = showDisliked

    fun likeShow(showId: String) {
        apiService?.likeShow(token, showId)?.enqueue(object : Callback<LikeResponse> {
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    showLiked.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                apiError.postValue(t.message)
            }
        })
    }

    fun dislikeShow(showId: String) {
        apiService?.dislikeShow(token, showId)?.enqueue(object : Callback<LikeResponse> {
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    showDisliked.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                apiError.postValue(t.message)
            }
        })
    }

    fun postComment(comment: MessageModel) {
        apiService?.postComment(token, comment)?.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    commentPosted.value = response.body()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                apiError.postValue(t.message)
            }
        })
    }

    fun postEpisode(episode: EpisodePost) {
        apiService?.postEpisode(token, episode)?.enqueue(object : Callback<EpisodePostResponse> {

            override fun onResponse(call: Call<EpisodePostResponse>, response: Response<EpisodePostResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    postEpisodeResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<EpisodePostResponse>, t: Throwable) {
                apiError.postValue(t.message)
            }

        })
    }

    fun uploadMedia(imageFile: File) {
        apiService?.uploadMedia(token, RequestBody.create(MediaType.parse("image/jpg"), imageFile))
            ?.enqueue(object : Callback<MediaResponse> {
                override fun onResponse(call: Call<MediaResponse>, response: Response<MediaResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        media.value = response.body()
                    }
                }

                override fun onFailure(call: Call<MediaResponse>, t: Throwable) {
                    apiError.postValue(t.message)
                }
            })
    }

    fun registerUser(user: User) {
        apiService?.userRegister(user)?.enqueue(object : Callback<UserRegisterResponse> {

            override fun onResponse(call: Call<UserRegisterResponse>, response: Response<UserRegisterResponse>) {
                userRegistrationSuccessful.value = response.isSuccessful && response.body() != null
            }

            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                apiError.postValue(t.message)
            }
        })
    }


    fun loginUser(user: User) {
        apiService?.userLogin(user)?.enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    userLoginResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                apiError.postValue(t.message)
            }
        })
    }

    fun getShowsFromApi() {
        apiService?.getShows()?.enqueue(object : Callback<ShowResponse> {

            override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    shows.value = response.body()?.showList
                }
            }

            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                apiError.postValue(t.message)
            }

        })
    }

    fun getEpisodes(id: String) {
        apiService?.getEpisodes(id)?.enqueue(object : Callback<EpisodesResponse> {

            override fun onResponse(call: Call<EpisodesResponse>, response: Response<EpisodesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    loadingEpisodes = true
                    if (isLoading()) {
                        episodesProgressBar.postValue(true)
                    }
                    episodes.postValue(response.body()?.episodeList)
                }
            }

            override fun onFailure(call: Call<EpisodesResponse>, t: Throwable) {
                loadingEpisodes = true
                apiError.postValue(t.message)
            }
        })
    }

    fun getComments(id: String) {
        apiService?.getComments(id)?.enqueue(object : Callback<CommentsResponse> {
            override fun onResponse(call: Call<CommentsResponse>, response: Response<CommentsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    comments.value = response.body()?.comments
                }
            }

            override fun onFailure(call: Call<CommentsResponse>, t: Throwable) {
                apiError.postValue(t.message)
            }

        })
    }

    fun getEpisode(id: String) {
        apiService?.getEpisode(id)?.enqueue(object : Callback<SingleEpisodeResponse> {
            override fun onResponse(call: Call<SingleEpisodeResponse>, response: Response<SingleEpisodeResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    episode.value = response.body()?.episodeData
                }
            }

            override fun onFailure(call: Call<SingleEpisodeResponse>, t: Throwable) {
                apiError.postValue(t.message)
            }
        })
    }


    fun getShowDetails(id: String) {
        apiService?.getShowDetails(id)?.enqueue(object : Callback<ShowDetailsResponse> {
            override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    loadingShowDetails = true
                    if (isLoading()) {
                        episodesProgressBar.postValue(true)
                    }
                    details.value = response.body()?.showDetails
                }
            }

            override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
                loadingEpisodes = true
                apiError.postValue(t.message)
            }
        })
    }

    fun resetLiveData() {
        episodes.value = null
        details.value = null
        episodesProgressBar.value = false
        loadingEpisodes = false
        loadingShowDetails = false
    }


}