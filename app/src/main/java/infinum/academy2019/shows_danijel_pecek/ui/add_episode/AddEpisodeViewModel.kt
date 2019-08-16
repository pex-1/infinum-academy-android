package infinum.academy2019.shows_danijel_pecek.ui.add_episode

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.EpisodePost
import infinum.academy2019.shows_danijel_pecek.data.model.EpisodePostResponse
import infinum.academy2019.shows_danijel_pecek.data.model.MediaResponse
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository
import java.io.File

class AddEpisodeViewModel : ViewModel(),Observer<MediaResponse> {

    var apiErrorLiveData: LiveData<String>? = null

    fun apiError(){
        apiErrorLiveData = Repository.apiErrorLiveData()
    }

    override fun onChanged(t: MediaResponse?) {
        mediaUpload.value = t
    }

    override fun onCleared() {
        Repository.liveDataMedia().removeObserver(this)
    }

    init {
        Repository.liveDataMedia().observeForever(this)
    }
    var postEpisodeLiveData: LiveData<EpisodePostResponse>? = null


    private val mediaUpload = MutableLiveData<MediaResponse>()

    val liveDataMedia: LiveData<MediaResponse>
        get() {
            return mediaUpload
        }

    fun uploadMedia(imageFile: File){
        Repository.uploadMedia(imageFile)
    }

    fun postEpisode(episode: EpisodePost){
        Repository.postEpisode(episode)
        postEpisodeLiveData = Repository.liveDataPostEpisode()
    }

    var fileUri: Uri? = null
    var titleInput = ""
    var descriptionInput = ""
    var seasonDefault = 1
    var episodeDefault = 1


}