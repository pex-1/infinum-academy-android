package infinum.academy2019.shows_danijel_pecek.ui.add_episode

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import infinum.academy2019.shows_danijel_pecek.data.repository.ShowsRepository

class AddEpisodeViewModel(application: Application) : AndroidViewModel(application) {

    var fileUri: Uri? = null
    var titleInput = ""
    var descriptionInput = ""
    var seasonDefault = 1
    var episodeDefault = 1

    fun getShowId() = ShowsRepository.getShowId()

    fun saveEpisode(episode: Episode, showId: Int){
        ShowsRepository.saveEpisodes(episode, showId)
    }


}