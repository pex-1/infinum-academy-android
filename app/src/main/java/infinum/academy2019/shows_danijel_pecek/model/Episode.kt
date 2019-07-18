package infinum.academy2019.shows_danijel_pecek.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Episode(val title: String, val description: String, val season: String, val image: Uri?): Parcelable