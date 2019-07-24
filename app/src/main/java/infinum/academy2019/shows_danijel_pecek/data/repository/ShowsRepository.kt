package infinum.academy2019.shows_danijel_pecek.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import infinum.academy2019.shows_danijel_pecek.data.model.Show

object ShowsRepository {

    private val showsList = mutableListOf<Show>()
    private val shows = MutableLiveData<List<Show>>()

    init {
        with(showsList){
            add(
                Show(0, "Alien Nation", R.drawable.aliennation, "(2010 - 2014)", Constants.alienNationDescription)
            )
            add(
                Show(1, "South Park", R.drawable.southpark, "(2010 - 2014)", Constants.southParkDescription)
            )
            add(
                Show(2, "Star Wars: Clone Wars", R.drawable.starwars, "(2010 - 2014)", Constants.starWarsDescription)
            )
            add(
                Show(3, "The Gangster Chronicles", R.drawable.gangsterchronicles, "(2010 - 2014)", Constants.gangsterChroniclesDescription)
            )
            add(
                Show(4, "From Dusk Till Dawn: The Series", R.drawable.duskdawn, "(2010 - 2014)", Constants.duskDawnDescription)
            )
            add(
                Show(5, "Bordertown", R.drawable.bordertown, "(2010 - 2014)", Constants.bordertownDescription)
            )
            add(
                Show(6, "Jack Irish", R.drawable.jackirish, "(2010 - 2014)", Constants.jackDescription)
            )
        }
        shows.value = showsList
    }

    fun getShow(showId: Int) = shows.value?.get(showId)

    fun saveEpisodes(episode: Episode, showId: Int){
        showsList[showId].episodeList.add(episode)
        shows.value = showsList
    }


    fun getShows() : LiveData<List<Show>> = shows

}