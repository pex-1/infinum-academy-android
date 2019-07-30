package infinum.academy2019.shows_danijel_pecek.ui.fragment

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.EpisodeModel
import infinum.academy2019.shows_danijel_pecek.data.model.ShowDetails
import infinum.academy2019.shows_danijel_pecek.data.model.ShowModel
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository

class SharedDataViewModel : ViewModel(), Observer<List<ShowModel>> {

    override fun onChanged(shows: List<ShowModel>?) {
        _showsLiveData.value = shows?: listOf()
    }

    var fileUri: Uri? = null
    var titleInput = ""
    var descriptionInput = ""
    var seasonDefault = 1
    var episodeDefault = 1

    var currentShow: ShowModel? = null

    fun isLoading():Boolean = Repository.isLoading()

    fun loadingReset(){
        Repository.loadingReset()
    }

    private val _showsLiveData = MutableLiveData<List<ShowModel>>()
    val showsLiveData: LiveData<List<ShowModel>>
        get() = _showsLiveData

    var detailsLiveData: LiveData<ShowDetails>? = null

    fun getShowDetails(showId: String){
        Repository.getShowDetails(showId)
        detailsLiveData = Repository.liveDataDetails()
    }


    var episodesLiveData: LiveData<List<EpisodeModel>>? = null

    fun getEpisode(showId: String){
        Repository.getEpisodes(showId)
        episodesLiveData = Repository.liveDateEpisode()
    }


    init {
        Repository.liveData().observeForever(this)
    }


    fun getShows(){
        Repository.getShowsFromApi()
    }

    override fun onCleared() {
        Repository.liveData().removeObserver(this)
    }

    //fun saveEpisode(showId: Int){
        //Repository.saveEpisodes(Episode(titleInput, descriptionInput, seasonDefault, episodeDefault, fileUri), showId)
    //}


}