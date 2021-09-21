import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Directives.{post, _}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, Multipart, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors

import scala.concurrent.Future

object Routes {
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
    }
  }
}
