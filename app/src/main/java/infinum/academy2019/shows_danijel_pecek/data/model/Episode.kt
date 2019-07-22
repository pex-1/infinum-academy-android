package infinum.academy2019.shows_danijel_pecek.data.model

import android.net.Uri
import java.io.Serializable

data class Episode(val title: String, val description: String, val season: Int, val episode: Int, val image: Uri?): Serializable