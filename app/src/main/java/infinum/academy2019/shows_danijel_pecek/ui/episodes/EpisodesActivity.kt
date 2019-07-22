package infinum.academy2019.shows_danijel_pecek.ui.episodes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import infinum.academy2019.shows_danijel_pecek.DetailsActivity
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.ui.add_episode.AddEpisodeActivity
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import kotlinx.android.synthetic.main.activity_episode_scroll.*
import kotlinx.android.synthetic.main.content_episode.*



class EpisodesActivity : AppCompatActivity(), EpisodesAdapter.OnEpisodeClicked {

    private lateinit var viewModel: EpisodeViewModel
    private lateinit var adapter: EpisodesAdapter
    var id = 0

    companion object {
        const val SHOW = "SHOW"

        fun newInstance(context: Context, showId: Int): Intent {
            val intent = Intent(context, EpisodesActivity::class.java)
            intent.putExtra(SHOW, showId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode_scroll)

        setSupportActionBar(toolbarEpisodes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //TODO: zasto je ovo uopce potrebno ako nigdje ne postavljam title?
        supportActionBar?.setDisplayShowTitleEnabled(false)


        adapter = EpisodesAdapter(this)
        id = intent.getIntExtra(SHOW, 0)

        viewModel = ViewModelProviders.of(this).get(EpisodeViewModel::class.java)
        viewModel.setShow(id)
        viewModel.liveData.observe(this, Observer { show ->
            if (show != null) {
                adapter.setData(show.episodeList)
                if(show.episodeList.size >0){
                }


                updateEpisodes()
            }

        })



        viewModel.liveData.value?.image?.let {
            episodesImageView.setImageResource(it)
        }
        episodeTitleTextView.text = viewModel.liveData.value?.name
        showDescriptionTextView.text = viewModel.liveData.value?.description


        addEpisodesClickableTextView.setOnClickListener {
            startActivity(AddEpisodeActivity.newInstance(this, id))
        }


        updateEpisodes()

        episodesFab.setOnClickListener {
            startActivity(AddEpisodeActivity.newInstance(this, id))
        }

        //returnResult(show)
    }


    override fun onClick(episode: Episode) {
        startActivity(DetailsActivity.newInstance(this, episode))
    }

    private fun updateEpisodes() {
        if (viewModel.liveData.value?.episodeList?.isEmpty()!!) {
            noShowsLinearLayout.visibility = View.VISIBLE
        } else {
            noShowsLinearLayout.visibility = View.GONE
            episodesRecyclerView.visibility = View.VISIBLE

            var list = viewModel.liveData.value?.episodeList!![0]

            episodesRecyclerView.layoutManager = LinearLayoutManager(this)
            episodesRecyclerView.adapter = adapter
        }
    }


    /*
    private fun returnResult(show: Show) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SHOW_WITH_EPISODES, show)
        setResult(Activity.RESULT_OK, resultIntent)
    }*/


/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EPISODES_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            show.episodeList.add(data.getParcelableExtra(Constants.EPISODES_LIST))
        }
    }
    */

    override fun onResume() {
        super.onResume()
        updateEpisodes()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        //returnResult(show)
        super.onBackPressed()
    }
}
