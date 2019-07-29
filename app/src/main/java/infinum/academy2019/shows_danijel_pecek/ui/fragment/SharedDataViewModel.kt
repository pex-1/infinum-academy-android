package infinum.academy2019.shows_danijel_pecek.ui.fragment

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import infinum.academy2019.shows_danijel_pecek.data.model.Show
import infinum.academy2019.shows_danijel_pecek.data.repository.ShowsRepository

class SharedDataViewModel : ViewModel(), Observer<List<Show>> {

    var fileUri: Uri? = null
    var titleInput = ""
    var descriptionInput = ""
    var seasonDefault = 1
    var episodeDefault = 1

    var currentShow: Show? = null


    private val _showsLiveData = MutableLiveData<List<Show>>()

    val showsLiveData: LiveData<List<Show>>
        get() = _showsLiveData


    private var showsList = listOf<Show>()

    init {
        _showsLiveData.value = showsList
        ShowsRepository.getShows().observeForever(this)
    }

    override fun onChanged(shows: List<Show>?) {
        _showsLiveData.value = shows?: listOf()     //nepotrebno?
    }

    override fun onCleared() {
        ShowsRepository.getShows().removeObserver(this)
    }

    fun saveEpisode(showId: Int){
        ShowsRepository.saveEpisodes(Episode(titleInput, descriptionInput, seasonDefault, episodeDefault, fileUri), showId)
    }


}