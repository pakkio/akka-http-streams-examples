import java.util.Date

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Source

final case class Author(handle: String)
final case class Hashtag(name: String)
final case class Tweet(author: Author, timestamp: Long, body: String) {
  def hashtags: Set[Hashtag] =
    body.split(" ").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
}



object main extends App {

  val akka = Hashtag("#akka")

  implicit val system = ActorSystem("reactive-tweets")
  implicit val mat = ActorFlowMaterializer()

  val now=System.currentTimeMillis()
  private val tweets1: Vector[Tweet] = Vector(
    Tweet(Author("pippo"), now, "#akka alle #falde")
      ,Tweet(Author("pippo"), now, "#akka alle #falde 6")
      ,Tweet(Author("pluto"), now, "#akka alle #falde")
      ,Tweet(Author("cane"), now, "#akka2 alle #falde")
  )
  val tweets: Source[Tweet] = Source(tweets1)

  val authors: Source[Author] =
    tweets
      .filter(_.hashtags.contains(akka))
      .map(_.author)

  authors.runForeach(println)

}