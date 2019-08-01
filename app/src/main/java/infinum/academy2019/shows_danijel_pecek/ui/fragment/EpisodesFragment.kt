package infinum.academy2019.shows_danijel_pecek.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import infinum.academy2019.shows_danijel_pecek.data.model.EpisodeModel
import infinum.academy2019.shows_danijel_pecek.ui.episodes.EpisodesAdapter
import kotlinx.android.synthetic.main.fragment_episodes.*

class EpisodesFragment : Fragment(), EpisodesAdapter.OnEpisodeClicked {


    private lateinit var viewModel: SharedDataViewModel
    private lateinit var adapter: EpisodesAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_episodes, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EpisodesAdapter(this)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(SharedDataViewModel::class.java)

            viewModel.progressBarLiveData?.observe(this, Observer { progressBar ->
                if(progressBar){
                    episodesProgressBar.visibility = View.GONE
                }else{
                    episodesProgressBar.visibility = View.VISIBLE
                }
            })

            viewModel.episodesLiveData?.observe(this, Observer {episodes ->
                if (episodes != null) {

                    adapter.setData(episodes)


                    if (episodes.isNotEmpty()) {
                        noShowsLinearLayout.visibility = View.GONE
                        episodesRecyclerView.visibility = View.VISIBLE

                        episodesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        episodesRecyclerView.adapter = adapter
                    }
                }

            })

            viewModel.detailsLiveData?.observe(this, Observer {details ->
                if (details != null) {
                    showTitleTextViewFragment.text = details.title
                    showDescriptionTextView.text = details.description

                }
            })
        }


        addEpisodesClickableTextView.setOnClickListener {
            openAddEpisodeFragment()
        }


        episodesFab.setOnClickListener {
            openAddEpisodeFragment()
        }
    }


    private fun openAddEpisodeFragment() {

        val fragment = if (resources.getBoolean(R.bool.tablet)) {
            R.id.episodesFrameLayoutTablet
        } else {
            R.id.frameLayoutPhone
        }
        fragmentManager?.beginTransaction()?.apply {
            replace(fragment, AddEpisodeFragment(), Constants.ADD_EPISODE_TAG)
            addToBackStack(null)
            commit()
        }
    }

    override fun onClick(episode: EpisodeModel) {
        Toast.makeText(requireContext(),"episode clicked!",Toast.LENGTH_SHORT).show()
    }

}