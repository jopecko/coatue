import java.time.Year

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class MovieSpec extends Specification with Mockito {

  "Movies specification" >> {
    val systemUnderTest = new MovieService

    val page1 = List(
      Movie(1, "The Matrix", Year.parse("1999")),
      Movie(2, "No Country for Old Men", Year.parse("2007")),
      Movie(3, "There Will Be Blood", Year.parse("2007"))
    )
    val page2 = List(
      Movie(4, "Fight Club", Year.parse("1999")),
      Movie(5, "Inception", Year.parse("2010")),
      Movie(6, "The Martian", Year.parse("2015"))
    )
    val page3 = List(
      Movie(7, "The Sixth Sense", Year.parse("1999")),
      Movie(8, "Mad Max: Fury Road", Year.parse("2015")),
      Movie(9, "Dunkirk", Year.parse("2017"))
    )

    "correctly accumulates single page values by year" >> {
      val mockApi = mock[Api]
      mockApi.fetchBatch(anyInt) returns page1.iterator
      val map = systemUnderTest.namesByYear(mockApi)
      map.size must_=== 2
      map must have haveValue Set("The Matrix")
    }

    "accumulates movies by year across pages" >> {
      val mockApi = mock[Api]
      mockApi.fetchBatch(anyInt) returns page1.iterator thenReturns page2.iterator thenReturns page3.iterator
      val map = systemUnderTest.namesByYear(mockApi)
      map.size must_=== 5
      map must have haveValue Set("The Matrix", "Fight Club", "The Sixth Sense")
    }

    "empty Iterator must return an empty Map" >> {
      val mockApi = mock[Api]
      mockApi.fetchBatch(anyInt) returns Iterator.empty
      systemUnderTest.namesByYear(mockApi) must beEmpty
    }
  }
}