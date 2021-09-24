import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import Routes.routes
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.{ExceptionHandler, MethodRejection,
  MissingQueryParamRejection, Rejection, RejectionHandler}

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("ScalaLike");

  // Handle rejections
  implicit val customRejectionHandler: RejectionHandler = RejectionHandler.newBuilder()
    .handle {
      case m: MissingQueryParamRejection =>
        println(s"I got a query param rejection: $m")
        complete("Rejected query param!")
    }
    .handle {
      case m: Rejection =>
        println(s"I got a query param rejection: $m")
        complete("Rejected query param!")
    }
    .handle {
      case m: MethodRejection =>
        println(s"I got a method rejection: $m")
        complete("Rejected method!")
    }
    .handleNotFound {
      complete(StatusCodes.NotFound, "Not Found!")
    }
    .result()

  // Handle Exceptions
  implicit val customExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: IllegalArgumentException =>
      println(s"Illegal Argument exception ${e.getMessage}")
      complete(StatusCodes.BadRequest, e.getMessage)
    case e: RuntimeException =>
      println(s"Runtime exception ${e.getMessage}")
      complete(StatusCodes.NotFound, e.getMessage)
  }

  Http().newServerAt("localhost", 8083).bind(routes)
}
