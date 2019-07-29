package infinum.academy2019.shows_danijel_pecek.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.EpisodeModel
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository

class EpisodesViewModel:ViewModel(), Observer<List<EpisodeModel>> {

    override fun onChanged(episodes: List<EpisodeModel>?) {
        _episodesLiveData.value = episodes?: listOf()
    }


    private val _episodesLiveData = MutableLiveData<List<EpisodeModel>>()
    val episodesLiveData: LiveData<List<EpisodeModel>>
        get() = _episodesLiveData

    fun getEpisodes(showId: String){
        Repository.getEpisodes(showId)
    }

    init {
        Repository.liveDateEpisode().observeForever(this)
    }
}