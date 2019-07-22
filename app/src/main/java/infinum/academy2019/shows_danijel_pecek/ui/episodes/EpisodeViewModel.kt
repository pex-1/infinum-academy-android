package infinum.academy2019.shows_danijel_pecek.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import infinum.academy2019.shows_danijel_pecek.data.model.Show
import infinum.academy2019.shows_danijel_pecek.data.repository.ShowsRepository

class EpisodeViewModel: ViewModel() {

    private var showsLiveData = MutableLiveData<Show>()


    val liveData: LiveData<Show>
        get(){
            return showsLiveData
        }



    fun setShow(showId: Int){
        showsLiveData.value = (ShowsRepository.getShow(showId))
    }

    fun addEpisode(episode: Episode){
        val show = showsLiveData.value
        show?.episodeList?.add(episode)
        showsLiveData.postValue(show)

    }

}