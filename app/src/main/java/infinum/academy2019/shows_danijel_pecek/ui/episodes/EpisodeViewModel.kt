package infinum.academy2019.shows_danijel_pecek.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.SingleEpisode
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository

class EpisodeViewModel:ViewModel(){

    var apiErrorLiveData: LiveData<String>? = null

    fun apiError(){
        apiErrorLiveData = Repository.apiErrorLiveData()
    }

    fun resetApiError(){
        Repository.resetErrorLiveData()
    }

    var episodeLiveData: LiveData<SingleEpisode>? = null

    fun getEpisode(episodeId: String){
        Repository.getEpisode(episodeId)
        episodeLiveData = Repository.liveDataSingleEpisode()
    }
}