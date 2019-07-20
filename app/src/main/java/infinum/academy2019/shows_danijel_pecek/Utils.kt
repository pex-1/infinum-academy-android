package infinum.academy2019.shows_danijel_pecek

object Utils{

    fun setSeasonString(season: Int, episode: Int):String{
        return "S ${season.toString().padStart(2, '0')}, E ${episode.toString().padStart(2, '0')}"
    }
}