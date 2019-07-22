package infinum.academy2019.shows_danijel_pecek.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import infinum.academy2019.shows_danijel_pecek.ui.episodes.EpisodesApplication
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

private const val FILENAME = "episodes"

object EpisodesRepository {

    private val episodesLiveData = MutableLiveData<List<Episode>>()

    fun getEpisodes() : LiveData<List<Episode>> =
        episodesLiveData

    private val episodesList = readEpisodesFromStorage()

    init {
        episodesLiveData.value = episodesList
    }

    /**
     * Čitanje iz datoteke u koju smo spremili zadnje stanje liste kontakata.
     */
    private fun readEpisodesFromStorage() : MutableList<Episode> {
        return try {
            ObjectInputStream(EpisodesApplication.instance.openFileInput(FILENAME)).use {
                it.readObject() as MutableList<Episode>
            }
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    fun addEpisode(episode: Episode) {
        episodesList.add(episode)
        episodesLiveData.value = episodesList
        /**
         * Prilikom svake promjene liste spremamo podataka u datoteku.
         * Pametnije riješenje bi bilo spremati podataka kada aplikacija prestaje s radom.
         * Tako bi smanjili broj pisanja u datoteku.
         * Ako bi u našu aplikaciju dodali i mogućnost brisanja kontakata ili ažurianje podataka,
         * morali bi također brinuti o spremanji zadnje stanja u datoteku.
         */
        ObjectOutputStream(EpisodesApplication.instance.openFileOutput(FILENAME, Context.MODE_PRIVATE)).use {
            it.writeObject(episodesList)
        }
    }
}