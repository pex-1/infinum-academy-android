package infinum.academy2019.shows_danijel_pecek.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.Show

object ShowsDatabase{

    private val showsList = mutableListOf<Show>()
    private val shows = MutableLiveData<List<Show>>()

    init {
            with(showsList){
                add(
                    Show(0, "Alien Nation", R.drawable.aliennation, "(2010 - 2014)", infinum.academy2019.shows_danijel_pecek.Constants.alienNationDescription)
                )
                add(
                    Show(1, "South Park", R.drawable.southpark, "(2010 - 2014)", infinum.academy2019.shows_danijel_pecek.Constants.southParkDescription)
                )
                add(
                    Show(2, "Star Wars: Clone Wars", R.drawable.starwars, "(2010 - 2014)", infinum.academy2019.shows_danijel_pecek.Constants.starWarsDescription)
                )
                add(
                    Show(3, "The Gangster Chronicles", R.drawable.gangsterchronicles, "(2010 - 2014)", infinum.academy2019.shows_danijel_pecek.Constants.gangsterChroniclesDescription)
                )
                add(
                    Show(4, "From Dusk Till Dawn: The Series", R.drawable.duskdawn, "(2010 - 2014)", infinum.academy2019.shows_danijel_pecek.Constants.duskDawnDescription)
                )
                add(
                    Show(5, "Bordertown", R.drawable.bordertown, "(2010 - 2014)", infinum.academy2019.shows_danijel_pecek.Constants.bordertownDescription)
                )
                add(
                    Show(6, "Jack Irish", R.drawable.jackirish, "(2010 - 2014)", infinum.academy2019.shows_danijel_pecek.Constants.jackDescription)
                )
        }
        shows.value = showsList
    }


    fun getShows() = shows as LiveData<List<Show>>


}