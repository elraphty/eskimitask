import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes, StatusCodes}

import concurrent.duration._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server.MethodRejection
import akka.util.Timeout
import models.JsonTraits
import models.Models.{BidRequest, Device, Geo, Impression, Site, User}
import spray.json._

import scala.language.postfixOps

class HttpTest extends AnyWordSpec with Matchers with ScalatestRouteTest with JsonTraits {
  implicit val timeout: Timeout = Timeout(5 seconds)

  import Routes.routes

  "Check if the server is up" should {
    "return 200k of the base get route" in {
      Get("/") ~> routes ~> check {
        // assertions
        status shouldBe StatusCodes.OK
      }
    }
  }

  "A post should return a status of no content if there is no campaign that matches bid" in {
    val bidRequest = BidRequest(
      "1", Some(List(Impression("1", Some(50),
      Some(200), Some(300), Some(50), Some(100), Some(250), Some(3.5)))), Site(200, "me.com"),
      Some(User("1", Some(Geo(Some("LT"))))), Some(Device("1" , Some(Geo(Some("LT")))))
    )
    // convert request to JSON
    val bid = bidRequest.toJson.prettyPrint

    val postRequest = HttpRequest(
      HttpMethods.POST,
      uri = "/bidrequest",
      entity = HttpEntity(MediaTypes.`application/json`, bid))

    postRequest ~> routes ~> check {
      status shouldBe StatusCodes.NoContent
    }
  }
}

