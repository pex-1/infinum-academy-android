package infinum.academy2019.shows_danijel_pecek.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.ShowModel
import infinum.academy2019.shows_danijel_pecek.ui.shared.BaseFragment

import infinum.academy2019.shows_danijel_pecek.ui.shows.ShowsAdapter
import kotlinx.android.synthetic.main.fragment_shows.*


class ShowsFragment : Fragment(), ShowsAdapter.OnShowClicked {


    private lateinit var viewModel: SharedDataViewModel
    private lateinit var adapter: ShowsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shows, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = ShowsAdapter(this)
        showsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showsRecyclerView.adapter = adapter


        activity?.let {
            viewModel = ViewModelProviders.of(it).get(SharedDataViewModel::class.java)
            viewModel.showsLiveData.observe(this, Observer { shows ->
                if (shows != null) {
                    showsProgressBar.visibility = View.GONE
                    adapter.setData(shows)
                }
            })
        }
        viewModel.getShows()
        showsProgressBar.visibility = View.VISIBLE

    }


    override fun onClick(show: ShowModel) {
        viewModel.setProgressBar()
        viewModel.getEpisode(show.showId)
        viewModel.getShowDetails(show.showId)
        viewModel.currentShow = show
        val fragment = fragmentManager?.findFragmentByTag(Constants.ADD_EPISODE_TAG)

        if (resources.getBoolean(R.bool.tablet)) {
            if (fragment != null && (fragment is BaseFragment)) {
                if(fragment.onBackButton()){
                    fragmentManager?.popBackStackImmediate()
                }
            }else{
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.episodesFrameLayoutTablet, EpisodesFragment(), Constants.EPISODE_TAG)
                    commit()
                }

            }
        } else {
            fragmentManager?.beginTransaction()?.apply {
                add(R.id.frameLayoutPhone, EpisodesFragment(), Constants.EPISODE_TAG)
                addToBackStack(null)
                commit()
            }
        }

    }

}