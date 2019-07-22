package infinum.academy2019.shows_danijel_pecek.data.model

import java.io.Serializable

data class Show (val id: Int, val name: String, val image: Int, val date: String, val description: String, var episodeList: ArrayList<Episode> = arrayListOf()) : Serializable
