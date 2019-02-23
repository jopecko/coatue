import java.time.Year

import scala.annotation.tailrec

case class Movie(id: Int, name: String, yearReleased: Year)

/** API to fetch Movies **/
trait Api {
  /** Given a page number, fetches movies on that page (empty if page is past last page) **/
  def fetchBatch(page: Int): Iterator[Movie]
}

class MovieService {
  /** fetch all distinct movie names by year **/
  def namesByYear(api: Api): Map[Year, Set[String]] = {

    @tailrec
    def fetchPage(page: Int, acc: Map[Year, Set[String]]): Map[Year, Set[String]] = {
      val it = api.fetchBatch(page)
      if (it.isEmpty)
        acc
      else {
        val accumulated = it.foldLeft(acc) { case (m, Movie(_, name, year)) =>
          m + (year -> m.get(year).map(_ + name).getOrElse(Set(name)))
        }
        fetchPage(page + 1, accumulated)
      }
    }

    fetchPage(1, Map.empty[Year, Set[String]])
  }
}