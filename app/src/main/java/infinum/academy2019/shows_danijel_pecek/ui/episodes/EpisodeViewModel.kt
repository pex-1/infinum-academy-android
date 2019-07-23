package infinum.academy2019.shows_danijel_pecek.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import infinum.academy2019.shows_danijel_pecek.data.model.Show
import infinum.academy2019.shows_danijel_pecek.data.repository.ShowsRepository

class EpisodeViewModel: ViewModel() {

    private var _showLiveData = MutableLiveData<Show>()

    val showLiveData: LiveData<Show>
        get()= _showLiveData



    fun setShow(showId: Int){
        _showLiveData.value = (ShowsRepository.getShow(showId))
    }

    fun getShowId() = ShowsRepository.getShowId()

}