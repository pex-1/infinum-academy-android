package infinum.academy2019.shows_danijel_pecek.ui.show_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.database.LikesDatabase
import infinum.academy2019.shows_danijel_pecek.data.database.ShowLikes
import infinum.academy2019.shows_danijel_pecek.data.model.EpisodeModel
import infinum.academy2019.shows_danijel_pecek.data.model.LikeResponse
import infinum.academy2019.shows_danijel_pecek.data.model.ShowDetails
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository

class ShowDetailsViewModel : ViewModel() {

    var showLikesLiveData: LiveData<ShowLikes>? = null

    var apiErrorLiveData: LiveData<String>? = null

    fun apiError(){
        apiErrorLiveData = Repository.apiErrorLiveData()
    }

    fun resetApiError(){
        Repository.resetErrorLiveData()
    }

    fun insertLike(showLikes: ShowLikes) {
        Repository.insert(showLikes)
    }

    fun updateLike(showLikes: ShowLikes) {
        Repository.update(showLikes)
    }

    fun getLikes(id: String) {
        Repository.getShow(id)
        showLikesLiveData = Repository.liveDataLikes()

    }

    var detailsLiveData: LiveData<ShowDetails>? = null

    fun getShowDetails(showId: String) {
        Repository.getShowDetails(showId)
        detailsLiveData = Repository.liveDataDetails()
    }

    var episodesLiveData: LiveData<List<EpisodeModel>>? = null

    fun getEpisode(showId: String) {
        Repository.getEpisodes(showId)
        episodesLiveData = Repository.liveDateEpisodes()
    }

    var likeShowLiveData: LiveData<LikeResponse>? = null
    var dislikeShowLiveData: LiveData<LikeResponse>? = null

    fun likeShow(showId: String) {
        Repository.likeShow(showId)
        likeShowLiveData = Repository.showLikedResponse()
    }

    fun dislikeShow(showId: String) {
        Repository.dislikeShow(showId)
        likeShowLiveData = Repository.showDislikedResponse()
    }


    var progressBarLiveData: LiveData<Boolean>? = null

    fun setProgressBar() {
        progressBarLiveData = Repository.liveDataEpisodesProgressBar()
    }

    fun resetLiveData() {
        Repository.resetLiveData()
    }
}