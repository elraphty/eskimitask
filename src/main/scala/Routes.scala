import actors.Bid
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import models.JsonTraits
import models.Models.{BidRequest, BidResponse, FindBanner, NoResponse}

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
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
          bidRequest =>
            // convert the impression list to vector for fast searching
            val impression = bidRequest.imp.get.toVector

            // Return Error if impression array is empty
            if (impression.isEmpty) {
              complete(400 -> NoResponse(400, "Enter Impression values"))
            } else {
              val imp = impression(0)

              // Could have used akka-http validate, but I wanted a JSON Response
              // validate(imp.h.isDefined && imp.w.isDefined, "Enter a width and height"){}

              // if  width or height is Empty and all of min and max are None values return an error.
              // there is not need to check for a bid
              if (imp.w.isEmpty || imp.h.isEmpty && imp.hmin.isEmpty && imp.wmin.isEmpty && imp.hmax.isEmpty && imp.wmax.isEmpty) {
                complete(400 -> NoResponse(400, "Enter a width and height"))
              } else {

                val data = (biddingActor ? bidRequest).mapTo[List[FindBanner]]
                println(data.map(x => println(x)))

                val hello = "hell";
                if (hello == "hello") {
                  complete(StatusCodes.OK -> hello)
                }
                complete(StatusCodes.NoContent)
              }
            }
        }
      }
    }
  }
}
