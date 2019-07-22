package infinum.academy2019.shows_danijel_pecek.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.Show
import infinum.academy2019.shows_danijel_pecek.data.repository.ShowsRepository

class ShowsViewModel: ViewModel(), Observer<List<Show>>{

    private val showsLiveData = MutableLiveData<List<Show>>()

    val liveData: LiveData<List<Show>>
        get(){
            return showsLiveData
        }

    private var showsList = listOf<Show>()

    init {
        showsLiveData.value = showsList
        ShowsRepository.getShows().observeForever(this)
    }

    override fun onChanged(shows: List<Show>?) {
        showsList = shows?: listOf()        //nepotrebno?
        showsLiveData.value = showsList
    }

    fun addShow(show: Show){
        ShowsRepository.addShow(show)
    }

    override fun onCleared() {
        ShowsRepository.getShows().removeObserver(this)
    }

}