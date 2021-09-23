import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import spray.json._

import concurrent.duration._
import concurrent.{Await, Future}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.MethodRejection
import akka.util.Timeout

import scala.language.postfixOps

class HttpTest extends AnyWordSpec with Matchers with ScalatestRouteTest {
  implicit val timeout: Timeout = Timeout(5 seconds)

  import Routes.routes;
  "Check if the server is up" should {
    "return 200k of the base get route" in {
      // send an HTTP request through an endpoint that you want to test
      // inspect the response
      Get("/") ~> routes ~> check {

        // assertions
        status shouldBe StatusCodes.OK
      }
    }
  }
}

