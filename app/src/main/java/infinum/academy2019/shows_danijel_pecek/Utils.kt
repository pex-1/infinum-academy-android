package infinum.academy2019.shows_danijel_pecek

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity

object Utils {

    fun setSeasonString(season: Int, episode: Int): String {
        return "S ${season.toString().padStart(2, '0')}, E ${episode.toString().padStart(2, '0')}"
    }

    fun setSeasonString(season: String, episode: String): String {
        return if (season.length > 2 || episode.length > 2) {
            "S 01, E 01"
        } else {
            "S ${season.padStart(2, '0')}, E ${episode.padStart(2, '0')}"
        }
    }

    fun getUserName(email: String): String = email.split("@")[0].capitalize()

    fun networkAvailable(activity: AppCompatActivity): Boolean {
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}