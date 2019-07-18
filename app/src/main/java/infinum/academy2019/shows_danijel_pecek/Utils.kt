package infinum.academy2019.shows_danijel_pecek

import com.google.gson.Gson
import infinum.academy2019.shows_danijel_pecek.model.Episode
import infinum.academy2019.shows_danijel_pecek.model.Show

object Utils {

    fun serialize(show: Show): String{
        val gson = Gson()
        return gson.toJson(show)
    }

    fun serialize(episode: Episode): String{
        val gson = Gson()
        return gson.toJson(episode)
    }

    fun deserialize(string: String): Show {
        val gson = Gson()
        return gson.fromJson(string, Show::class.java)
    }

    fun deserializeEpisode(string: String): Episode {
        val gson = Gson()
        return gson.fromJson(string, Episode::class.java)
    }


}