package infinum.academy2019.shows_danijel_pecek.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.ui.fragment.EpisodesFragment
import infinum.academy2019.shows_danijel_pecek.ui.fragment.ShowsFragment

class FragmentContainerActivity : AppCompatActivity() {

    companion object {
        const val ORIENTATION_CHANGE_FRAGMENT_CONTAINER = "ORIENTATION_CHANGE_FRAGMENT_CONTAINER"
        fun newInstance(context: Context) = Intent(context, FragmentContainerActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        if (resources.getBoolean(R.bool.tablet)) {
            tablet()
        }else{
            phone()
        }


    }

    private fun tablet() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.showsFrameLayout, ShowsFragment())
            add(R.id.episodesFrameLayout, EpisodesFragment())
            commit()
        }
    }

    private fun phone() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragmentContainer, ShowsFragment())
            commit()
        }
    }
}
