package infinum.academy2019.shows_danijel_pecek.data.model


data class Show (val id: Int, val title: String, val image: Int, val likesCount: String, val description: String, var episodeList: ArrayList<Episode> = arrayListOf())
