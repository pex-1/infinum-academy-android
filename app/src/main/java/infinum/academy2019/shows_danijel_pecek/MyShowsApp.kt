package infinum.academy2019.shows_danijel_pecek

import android.app.Application

class MyShowsApp : Application() {

    companion object {
        lateinit var instance: MyShowsApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}