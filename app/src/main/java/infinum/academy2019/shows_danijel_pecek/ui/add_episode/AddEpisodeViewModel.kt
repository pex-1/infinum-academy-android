package infinum.academy2019.shows_danijel_pecek.ui.add_episode

import android.net.Uri
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import infinum.academy2019.shows_danijel_pecek.data.repository.ShowsRepository

class AddEpisodeViewModel : ViewModel(){

    var fileUri: Uri? = null
    var titleInput = ""
    var descriptionInput = ""
    var seasonDefault = 1
    var episodeDefault = 1

    fun saveEpisode(episode: Episode, showId: Int){
        ShowsRepository.saveEpisodes(episode, showId)
    }


}