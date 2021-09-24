import actors.Bid
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors

import models.Models.{BidRequest, BidResponse, NoResponse}

import scala.concurrent.duration.DurationInt
import scala.concurrent.Future
import scala.language.postfixOps
import models.JsonTraits

object Routes extends SprayJsonSupport with JsonTraits {
  implicit val system: ActorSystem = ActorSystem("Bidding")

  val biddingActor: ActorRef = system.actorOf(Props[Bid.BidActor], "bidactor")

  implicit val timeout: Timeout = Timeout(10 seconds);

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
          bidRequest =>
            // Could have used akka-http validate, but I wanted a JSON Response
            // validate(bidRequest.imp.isEmpty && bidRequest.user.isEmpty, "Enter a width and height"){}
            if (bidRequest.imp.isEmpty || bidRequest.device.isEmpty) {
              complete(400 -> NoResponse(400, "Your request data is not valid"))
            } else {
              // convert the impression list to vector for fast searching
              val impression = bidRequest.imp.get.toVector

              // Return Error if impression array is empty
              if (impression.isEmpty) {
                complete(400 -> NoResponse(400, "Specify Impression values"))
              } else {
                val data: Future[Option[BidResponse]] = (biddingActor ? bidRequest).mapTo[Option[BidResponse]]

                onSuccess(data) {
                  case None => complete(StatusCodes.NoContent)
                  case Some(value) => complete(value)
                }
              }
            }
        }
      }
    }
  }
}
