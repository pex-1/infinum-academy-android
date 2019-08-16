package infinum.academy2019.shows_danijel_pecek.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import infinum.academy2019.shows_danijel_pecek.data.database.dao.LikesDao

@Database(
    version = 1,
    entities = [
        ShowLikes::class
    ]
)
abstract class LikesDatabase : RoomDatabase() {

    abstract fun likesDao(): LikesDao

}