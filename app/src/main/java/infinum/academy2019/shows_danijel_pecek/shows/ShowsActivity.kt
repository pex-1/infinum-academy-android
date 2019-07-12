package infinum.academy2019.shows_danijel_pecek.shows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.episodes.EpisodesActivity
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.adapter.ShowsAdapter
import infinum.academy2019.shows_danijel_pecek.model.Show
import kotlinx.android.synthetic.main.activity_shows.*

const val SHOW_ACTIVITY_REQUEST_CODE = 1

class ShowsActivity : AppCompatActivity(), ShowsAdapter.OnShowClicked {

    val listOfShows = arrayListOf<Show>()
    //var listOfEpisodes = arrayListOf<String>()

    init {
        with(listOfShows){
            add(
                Show(
                    0,
                    "Alien Nation",
                    R.drawable.aliennation,
                    "(2010 - 2014)",
                    Constants.alienNationDescription
                )
            )
            add(
                Show(
                    1,
                    "South Park",
                    R.drawable.southpark,
                    "(2010 - 2014)",
                    Constants.southParkDescription
                )
            )
            add(
                Show(
                    2,
                    "Star Wars: Clone Wars",
                    R.drawable.starwars,
                    "(2010 - 2014)",
                    Constants.starWarsDescription
                )
            )
            add(
                Show(
                    3,
                    "The Gangster Chronicles",
                    R.drawable.gangsterchronicles,
                    "(2010 - 2014)",
                    Constants.gangsterChroniclesDescription
                )
            )
            add(
                Show(
                    4,
                    "From Dusk Till Dawn: The Series",
                    R.drawable.duskdawn,
                    "(2010 - 2014)",
                    Constants.duskDawnDescription
                )
            )
            add(
                Show(
                    5,
                    "Bordertown",
                    R.drawable.bordertown,
                    "(2010 - 2014)",
                    Constants.bordertownDescription
                )
            )
            add(
                Show(
                    6,
                    "Jack Irish",
                    R.drawable.jackirish,
                    "(2010 - 2014)",
                    Constants.jackDescription
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        showsRecyclerView.layoutManager = LinearLayoutManager(this)
        showsRecyclerView.adapter = ShowsAdapter(listOfShows, this)
    }

    override fun onClick(show: Show) {
        startActivityForResult(
            EpisodesActivity.newInstance(
                this,
                Utils.serialize(show)
            ), SHOW_ACTIVITY_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SHOW_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            val show =
                Utils.deserialize(data.getStringExtra(Constants.SHOW_WITH_EPISODES)!!)
            listOfShows[show.id].episodeList = show.episodeList
        }

    }
}
