package infinum.academy2019.shows_danijel_pecek.ui.episodes

import android.app.Application

class EpisodesApplication:Application() {

    companion object{
        lateinit var instance: EpisodesApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}