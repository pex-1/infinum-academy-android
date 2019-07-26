package infinum.academy2019.shows_danijel_pecek.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.Show

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
                    adapter.setData(shows)
                }
            })
        }

    }

    override fun onClick(show: Show) {
        viewModel.currentShow = show

        if (resources.getBoolean(R.bool.tablet)){
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.episodesFrameLayout, EpisodesFragment())
                commit()
            }
        }else{
            fragmentManager?.beginTransaction()?.apply {
                add(R.id.fragmentContainer, EpisodesFragment())
                addToBackStack("show fragment")
                commit()
            }
        }

    }

}