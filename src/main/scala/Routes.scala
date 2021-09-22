import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, MediaTypes, Multipart, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import db.Db.activeCampaigns
import models.JsonTraits
import models.Models.{BidRequest, BidResponse, NoResponse}
import spray.json._

import scala.concurrent.Future

object Routes extends SprayJsonSupport with JsonTraits {
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
