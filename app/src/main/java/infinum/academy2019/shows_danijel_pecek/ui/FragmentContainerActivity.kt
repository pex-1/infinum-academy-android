package infinum.academy2019.shows_danijel_pecek.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.ui.fragment.AddEpisodeFragment
import infinum.academy2019.shows_danijel_pecek.ui.fragment.EpisodesFragment
import infinum.academy2019.shows_danijel_pecek.ui.fragment.ShowsFragment
import infinum.academy2019.shows_danijel_pecek.ui.shared.BaseFragment


class FragmentContainerActivity : AppCompatActivity() {

    override fun onBackPressed() {

        val fragment = supportFragmentManager.findFragmentByTag(Constants.ADD_EPISODE_TAG)

        if (fragment != null && (fragment is BaseFragment)) {
            if (fragment.onBackButton()) {
            }
        } else {
            super.onBackPressed()
        }

    }

    var addScreenActive: Boolean = false
    var episodeScreenActive: Boolean = false

    companion object {
        fun newInstance(context: Context) = Intent(context, FragmentContainerActivity::class.java)

        const val STACK_POSITION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        if (supportFragmentManager.findFragmentByTag(Constants.ADD_EPISODE_TAG) != null) {
            addScreenActive = true
        }

        if (supportFragmentManager.findFragmentByTag(Constants.EPISODE_TAG) != null) {
            episodeScreenActive = true
        }

        if (resources.getBoolean(R.bool.tablet)) {
            tablet()
        } else {
            phone()
        }
    }


    private fun tablet() {
        supportFragmentManager.popBackStackImmediate(STACK_POSITION, POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.showsFrameLayoutTablet, ShowsFragment(), Constants.SHOW_TAG)
            replace(R.id.episodesFrameLayoutTablet, EpisodesFragment(), Constants.EPISODE_TAG)
            commit()

        }
        supportFragmentManager.beginTransaction().apply {
            if (addScreenActive) {
                replace(R.id.episodesFrameLayoutTablet, AddEpisodeFragment(), Constants.ADD_EPISODE_TAG)
                addToBackStack(null)
            }
            commit()
        }
    }

    private fun phone() {
        supportFragmentManager.popBackStackImmediate(STACK_POSITION, POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutPhone, ShowsFragment())
            commit()
        }

        if (addScreenActive) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutPhone, EpisodesFragment(), Constants.EPISODE_TAG)
                addToBackStack(null)
                commit()
            }

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutPhone, AddEpisodeFragment(), Constants.ADD_EPISODE_TAG)
                addToBackStack(null)
                commit()
            }
        } else if (episodeScreenActive) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutPhone, EpisodesFragment(), Constants.EPISODE_TAG)
                addToBackStack(null)
                commit()
            }
        }

    }
}
