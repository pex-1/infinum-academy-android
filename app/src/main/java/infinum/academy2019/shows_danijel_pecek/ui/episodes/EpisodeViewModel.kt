package infinum.academy2019.shows_danijel_pecek.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import infinum.academy2019.shows_danijel_pecek.data.model.Show
import infinum.academy2019.shows_danijel_pecek.data.repository.ShowsRepository

class EpisodeViewModel: ViewModel(), Observer<List<Show>> {

    private val _showsLiveData = MutableLiveData<List<Show>>()

    val showsLiveData: LiveData<List<Show>>
        get() = _showsLiveData


    private var showsList = listOf<Show>()

    init {
        _showsLiveData.value = showsList
        ShowsRepository.getShows().observeForever(this)
    }

    override fun onChanged(shows: List<Show>?) {
        _showsLiveData.value = shows?: listOf()
    }

    override fun onCleared() {
        ShowsRepository.getShows().removeObserver(this)
    }

    fun getShow(showId: Int) = _showsLiveData.value?.get(showId)

}