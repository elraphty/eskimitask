import actors.Bid
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, MediaTypes, Multipart, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import db.Db.activeCampaigns
import models.JsonTraits
import models.Models.{BidRequest, BidResponse, FindBanner, NoResponse}
import spray.json._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object Routes extends SprayJsonSupport with JsonTraits {
  implicit val system: ActorSystem = ActorSystem("Bidding")

  implicit val timeout: Timeout = Timeout(10 seconds);
  val biddingActor = system.actorOf(Props[Bid.BidActor], "bidactor")

  val routes: Route = cors() {
    path("") {
      get {
        complete(
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            """
              |<html>
              | <body>
              |   Hello! welcome to eskimi scala home.
              | </body>
              |</html>
              |""".stripMargin
          )
        )
      }
    } ~ path("bidrequest") {
      post {
        entity(as[BidRequest]) {
          bidRequest  =>
            val data = (biddingActor ? bidRequest).mapTo[List[FindBanner]]
            println(data.map(x => println(x)))
            val hello = "hell";
            if(hello == "hello") {
              complete( StatusCodes.OK -> hello)
            }
            complete(StatusCodes.NoContent)
        }
      }
    }
  }
}
