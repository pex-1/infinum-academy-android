package infinum.academy2019.shows_danijel_pecek.data.database.dao

import androidx.room.*
import infinum.academy2019.shows_danijel_pecek.data.database.ShowLikes

@Dao
interface LikesDao {

    @Query("SELECT * FROM likes where show_id = :id")
    fun getShow(id: String): ShowLikes

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(showLike: ShowLikes)

    @Update
    fun update(showLike: ShowLikes)


}