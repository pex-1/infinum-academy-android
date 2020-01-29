package infinum.academy2019.shows_danijel_pecek.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likes")
data class ShowLikes(
    @PrimaryKey
    @ColumnInfo(name = "show_id")
    val id: String,

    @ColumnInfo(name = "likes_count")
    val likesCount: String,

    @ColumnInfo(name = "dislike")
    var dislike: Boolean = false,

    @ColumnInfo(name = "like")
    var like: Boolean = false
)