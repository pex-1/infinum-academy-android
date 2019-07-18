package infinum.academy2019.shows_danijel_pecek.episodes

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.DetailsActivity
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.adapter.EpisodesAdapter
import infinum.academy2019.shows_danijel_pecek.addEpisode.AddEpisodeActivity
import infinum.academy2019.shows_danijel_pecek.model.Episode
import infinum.academy2019.shows_danijel_pecek.model.Show
import kotlinx.android.synthetic.main.activity_episodes.*

const val EPISODES_ACTIVITY_REQUEST_CODE = 2

lateinit var show: Show

class EpisodesActivity : AppCompatActivity(), EpisodesAdapter.OnEpisodeClicked {


    companion object {
        const val SHOW = "SHOW"

        fun newInstance(context: Context, show: Show): Intent {
            val intent = Intent(context, EpisodesActivity::class.java)
            intent.putExtra(SHOW, show)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodes)

        setSupportActionBar(toolbarEpisodes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        show = intent.getParcelableExtra(SHOW)
        (toolbarEpisodes)?.title = show.name

        showDescriptionTextView.text = show.description


        addEpisodesClickableTextView.setOnClickListener {
            startActivityForResult(AddEpisodeActivity.newInstance(this),EPISODES_ACTIVITY_REQUEST_CODE)
        }


        updateEpisodes()

        episodesFab.setOnClickListener {
            startActivityForResult(AddEpisodeActivity.newInstance(this),EPISODES_ACTIVITY_REQUEST_CODE)
        }

        returnResult(show)
    }

    override fun onClick(episode: Episode) {
        //Toast.makeText(this, "Description: ${episode.description}", Toast.LENGTH_SHORT).show()
        startActivity(DetailsActivity.newInstance(this, episode))
    }

    private fun updateEpisodes() {
        if (show.episodeList.isEmpty()) {
            noShowsLinearLayout.visibility = View.VISIBLE
        } else {
            noShowsLinearLayout.visibility = View.GONE
            episodesRecyclerView.visibility = View.VISIBLE


            episodesRecyclerView.layoutManager = LinearLayoutManager(this)
            episodesRecyclerView.adapter = EpisodesAdapter(show.episodeList, this)
        }
    }

    private fun returnResult(show: Show) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SHOW_WITH_EPISODES, show)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    override fun onSupportNavigateUp(): Boolean {
        returnResult(show)
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        returnResult(show)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EPISODES_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            show.episodeList.add(data.getParcelableExtra(Constants.EPISODES_LIST))
        }

    }

    override fun onResume() {
        super.onResume()
        updateEpisodes()
    }
}
