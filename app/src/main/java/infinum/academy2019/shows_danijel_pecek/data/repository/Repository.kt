package infinum.academy2019.shows_danijel_pecek.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.data.Api
import infinum.academy2019.shows_danijel_pecek.data.RetrofitClient
import infinum.academy2019.shows_danijel_pecek.data.model.*
import infinum.academy2019.shows_danijel_pecek.data.model.user.User
import infinum.academy2019.shows_danijel_pecek.data.model.user.UserLoginResponse
import infinum.academy2019.shows_danijel_pecek.data.model.user.UserRegisterResponse
import retrofit2.Call
import retrofit2.Response

object Repository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)


    private val shows = MutableLiveData<List<ShowModel>>()

    fun liveData() : LiveData<List<ShowModel>> = shows


    private val episodes = MutableLiveData<List<EpisodeModel>>()
    fun liveDateEpisode(): LiveData<List<EpisodeModel>> = episodes

    private val details = MutableLiveData<ShowDetails>()
    fun liveDataDetails(): LiveData<ShowDetails> = details


    private val userRegistrationSuccessful = MutableLiveData<String>()
    fun registrationSuccessful(): LiveData<String> = userRegistrationSuccessful


    fun registerUser(user: User){
        apiService?.userRegister(user)?.enqueue(object : retrofit2.Callback<UserRegisterResponse>{

            override fun onResponse(call: Call<UserRegisterResponse>, response: Response<UserRegisterResponse>) {
                if(response.isSuccessful && response.body() != null){
                    userRegistrationSuccessful.value = Constants.USER_REGISTRATION_SUCCESSFUL
                }else{
                    userRegistrationSuccessful.value = Constants.USER_REGISTRATION_UNSUCCESSFUL
                }
            }
            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                userRegistrationSuccessful.value = t.localizedMessage
            }
        })
    }

    fun loginUser(user: User){
        apiService?.userLogin(user)?.enqueue(object : retrofit2.Callback<UserLoginResponse>{

            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                if(response.isSuccessful && response.body() != null){
                    userRegistrationSuccessful.value = Constants.USER_LOGIN_SUCCESSFUL
                }else{
                    userRegistrationSuccessful.value = Constants.USER_LOGIN_UNSUCCESSFUL
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                userRegistrationSuccessful.value = t.localizedMessage
            }
        })
    }


    fun getShowsFromApi() {
        apiService?.getShows()?.enqueue(object : retrofit2.Callback<ShowResponse> {

            override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    shows.value = response.body()?.showList
                }
            }

            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
            }

        })
    }

    fun getEpisodes(id: String) {
        apiService?.getEpisodes(id)?.enqueue(object : retrofit2.Callback<EpisodeResponse> {

            override fun onResponse(call: Call<EpisodeResponse>, response: Response<EpisodeResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    episodes.value = response.body()?.episodeList
                    shows.value = shows.value
                    Log.e("from repository", "${response.body()?.episodeList?.get(0)?.description}")
                }
            }
            override fun onFailure(call: Call<EpisodeResponse>, t: Throwable) {
            }
        })
    }


    fun getShowDetails(id: String) {
        apiService?.getShowDetails(id)?.enqueue(object : retrofit2.Callback<ShowDetailsResponse> {

            override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    details.value = response.body()?.showDetails
                    shows.value = shows.value
                    Log.e("show details", response.body()?.showDetails.toString())
                }
            }
            override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
            }
        })
    }

}