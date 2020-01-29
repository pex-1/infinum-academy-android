package infinum.academy2019.shows_danijel_pecek.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.ShowModel
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository

class ShowsViewModel : ViewModel(), Observer<List<ShowModel>> {

    var apiErrorLiveData: LiveData<String>? = null

    fun apiError(){
        apiErrorLiveData = Repository.apiErrorLiveData()
    }

    fun resetApiError(){
        Repository.resetErrorLiveData()
    }

    var gridLayout = true

    private val _showsLiveData = MutableLiveData<List<ShowModel>>()
    val showsLiveData: LiveData<List<ShowModel>>
        get() = _showsLiveData

    override fun onChanged(shows: List<ShowModel>?) {
        _showsLiveData.value = shows ?: listOf()
    }

    fun setToken(token: String){
        Repository.setToken(token)
    }

    fun getShows() {
        Repository.getShowsFromApi()
    }

    init {
        Repository.liveData().observeForever(this)
    }

    override fun onCleared() {
        Repository.liveData().removeObserver(this)
    }

}