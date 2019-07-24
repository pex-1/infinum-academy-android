package infinum.academy2019.shows_danijel_pecek.ui.episodes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.ui.add_episode.AddEpisodeActivity
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import kotlinx.android.synthetic.main.activity_episode_scroll.*
import kotlinx.android.synthetic.main.content_episode.*



class EpisodesActivity : AppCompatActivity(), EpisodesAdapter.OnEpisodeClicked {

    private lateinit var viewModel: EpisodeViewModel
    private lateinit var adapter: EpisodesAdapter
    //kako inicijaliziram ovo?
    private var id = 0

    companion object {
        const val SHOW_ID = "SHOW_ID"
        fun newInstance(context: Context, showId: Int): Intent {
            val intent = Intent(context, EpisodesActivity::class.java)
            intent.putExtra(SHOW_ID, showId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode_scroll)

        setSupportActionBar(toolbarEpisodes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        adapter = EpisodesAdapter(this)

        viewModel = ViewModelProviders.of(this).get(EpisodeViewModel::class.java)
        id = intent.getIntExtra(SHOW_ID, 0)


        viewModel.showsLiveData.observe(this, Observer {
            if (it != null) {
                adapter.setData(it[id].episodeList)


                if(it[id].episodeList.isNotEmpty()){

                    noShowsLinearLayout.visibility = View.GONE
                    episodesRecyclerView.visibility = View.VISIBLE

                    episodesRecyclerView.layoutManager = LinearLayoutManager(this)
                    episodesRecyclerView.adapter = adapter
                }
                else{

                }
            }
        })


        viewModel.getShow(id)?.image?.let {
            episodesImageView.setImageResource(it)
        }
        episodeTitleTextView.text = viewModel.getShow(id)?.name
        showDescriptionTextView.text = viewModel.getShow(id)?.description


        addEpisodesClickableTextView.setOnClickListener {
            startActivity(AddEpisodeActivity.newInstance(this, id))
        }


        episodesFab.setOnClickListener {
            startActivity(AddEpisodeActivity.newInstance(this, id))
        }

    }

    //možda nešto napravim s ovime kasnije - šteta bi bilo izbrisat
    override fun onClick(episode: Episode) {
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
