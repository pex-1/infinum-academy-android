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
        fun newInstance(context: Context)= Intent(context, EpisodesActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode_scroll)

        setSupportActionBar(toolbarEpisodes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //TODO: zasto je ovo uopce potrebno ako nigdje ne postavljam title?
        supportActionBar?.setDisplayShowTitleEnabled(false)


        adapter = EpisodesAdapter(this)

        viewModel = ViewModelProviders.of(this).get(EpisodeViewModel::class.java)
        id = viewModel.getShowId()
        viewModel.setShow(id)
        viewModel.showLiveData.observe(this, Observer {
            if (it != null) {
                adapter.setData(it.episodeList)
            }
        })


        viewModel.showLiveData.value?.image?.let {
            episodesImageView.setImageResource(it)
        }
        episodeTitleTextView.text = viewModel.showLiveData.value?.name
        showDescriptionTextView.text = viewModel.showLiveData.value?.description


        addEpisodesClickableTextView.setOnClickListener {
            startActivity(AddEpisodeActivity.newInstance(this))
        }


        updateEpisodes()

        episodesFab.setOnClickListener {
            startActivity(AddEpisodeActivity.newInstance(this))
        }

    }

    //možda nešto napravim s ovime kasnije - šteta bi bilo izbrisat
    override fun onClick(episode: Episode) {
    }

    private fun updateEpisodes() {
        if (viewModel.showLiveData.value?.episodeList?.isEmpty()!!) {
            noShowsLinearLayout.visibility = View.VISIBLE
        } else {
            noShowsLinearLayout.visibility = View.GONE
            episodesRecyclerView.visibility = View.VISIBLE

            episodesRecyclerView.layoutManager = LinearLayoutManager(this)
            episodesRecyclerView.adapter = adapter
        }
    }



    override fun onResume() {
        super.onResume()
        updateEpisodes()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
