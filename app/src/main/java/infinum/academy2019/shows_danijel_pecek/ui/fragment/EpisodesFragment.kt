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
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import infinum.academy2019.shows_danijel_pecek.ui.episodes.EpisodesAdapter
import kotlinx.android.synthetic.main.fragment_episodes.*

class EpisodesFragment : Fragment(), EpisodesAdapter.OnEpisodeClicked {


    private lateinit var viewModel: SharedDataViewModel
    private lateinit var adapter: EpisodesAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_episodes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setSupportActionBar(toolbarEpisodes)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setDisplayShowTitleEnabled(false)


        adapter = EpisodesAdapter(this)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(SharedDataViewModel::class.java)


            viewModel.showsLiveData.observe(this, Observer {shows ->
                if (shows != null) {
                    val selectedShow = viewModel.currentShow ?: shows[0]


                    adapter.setData(selectedShow.episodeList)

                    showTitleTextViewFragment.text = selectedShow.name

                    showDescriptionTextView.text = selectedShow.description

                    if (selectedShow.episodeList.isNotEmpty()) {

                        noShowsLinearLayout.visibility = View.GONE
                        episodesRecyclerView.visibility = View.VISIBLE

                        episodesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        episodesRecyclerView.adapter = adapter
                    } else {

                    }
                }
            })
        }




        addEpisodesClickableTextView.setOnClickListener {
            //startActivity(AddEpisodeActivity.newInstance(this, id))
            openAddEpisodeFragment()
        }


        episodesFab.setOnClickListener {
            //startActivity(AddEpisodeActivity.newInstance(this, id))
            openAddEpisodeFragment()
        }
    }

    private fun openAddEpisodeFragment() {

        val fragment = if (resources.getBoolean(R.bool.tablet)) {
            R.id.episodesFrameLayout
        } else {
            R.id.fragmentContainer
        }
        fragmentManager?.beginTransaction()?.apply {
            add(fragment, AddEpisodeFragment())
            addToBackStack("add episodes")
            commit()
        }
    }

    override fun onClick(episode: Episode) {
        Toast.makeText(requireContext(),"episode clicked!",Toast.LENGTH_SHORT).show()
    }

}