import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, MediaTypes, StatusCodes}

import concurrent.duration._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.Timeout
import models.Models.{BidRequest, BidResponse, Device, Geo, Impression, NoResponse, Site, User}
import spray.json._

import scala.language.postfixOps
import Routes._

class HttpTest extends AnyWordSpec with Matchers with ScalatestRouteTest {
  implicit val timeout: Timeout = Timeout(10 seconds)

  import Routes.routes

  "Check if the server is up" should {
    "return 200k of the base get route" in {
      Get("/") ~> routes ~> check {
        // assertions
        status shouldBe StatusCodes.OK
      }
    }
  }

  // Input Validations

  "A post request should return a 400 status when impression is empty" in {
    val bidRequest = BidRequest(
      "1",
      Some(List()), Site(1, "me.com"),
      Some(User("1", Some(Geo(Some("LT"))))),
      Some(Device("1", Some(Geo(Some("LT")))))
    )

    Post("/bidrequest", bidRequest) ~> routes ~> check {
      status shouldBe StatusCodes.BadRequest
      response.entity.contentType shouldBe ContentTypes.`application/json`

      val noResponse = entityAs[NoResponse]

      noResponse.errorCode shouldBe(400)
      noResponse.message shouldBe("Specify Impression values")
    }
  }

  "A post request should return a 400 status when impression or device data is not provided" in {
    val bidRequest = BidRequest(
      "1", None, Site(1, "me.com"),
      Some(User("1", Some(Geo(Some("LT"))))), None)

    Post("/bidrequest", bidRequest) ~> routes ~> check {
      status shouldBe StatusCodes.BadRequest
      response.entity.contentType shouldBe ContentTypes.`application/json`

      val noResponse = entityAs[NoResponse]

      noResponse.errorCode shouldBe(400)
      noResponse.message shouldBe("Your request data is not valid")
    }
  }

  // Data Validations

  "A post request should return a status of 204 if there is no campaign that matches bid" in {
    val bidRequest = BidRequest(
      "1",
      Some(List(Impression("1", Some(50), Some(200), Some(300), Some(50), Some(100), Some(250),
        Some(3.5)))), Site(200, "me.com"),
      Some(User("1", Some(Geo(Some("LT"))))),
      Some(Device("1", Some(Geo(Some("LT")))))
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

  "A post request should return a status of 200 and a value if there is a matching bid" in {
    val bidRequest = BidRequest(
      "1",
      Some(List(Impression("1", Some(50), Some(300), Some(300),
        Some(50), Some(100), Some(250), Some(3.5)))), Site(1, "me.com"),
      Some(User("1", Some(Geo(Some("LT"))))),
      Some(Device("1", Some(Geo(Some("LT")))))
    )
    // convert request to JSON
    val bid = bidRequest.toJson.prettyPrint

    val postRequest = HttpRequest(
      HttpMethods.POST,
      uri = "/bidrequest",
      entity = HttpEntity(MediaTypes.`application/json`, bid))

    postRequest ~> routes ~> check {
      status shouldBe StatusCodes.OK
      response.entity.contentType shouldBe ContentTypes.`application/json`

      val newResponse = entityAs[BidResponse]

      // A Banner should be returned
      newResponse.banner.size shouldBe 1
    }
  }

  "A post request should return a random campaign when multiple campaigns match request" in {
    val bidRequest = BidRequest(
        "1",
        Some(List(Impression("1", Some(50),
        Some(300), Some(300), Some(50), Some(100), Some(250), Some(3.5)),
        Impression("1", Some(50),
        Some(300), Some(400), Some(50), Some(100), Some(300), Some(3.5)))),
        Site(1, "me.com"),
        Some(User("1", Some(Geo(Some("LT"))))), Some(Device("1", Some(Geo(Some("LT")))))
    )

    Post("/bidrequest", bidRequest) ~> routes ~> check {
      status shouldBe StatusCodes.OK
      response.entity.contentType shouldBe ContentTypes.`application/json`

      val newResponse = entityAs[BidResponse]

      // One Banner should be returned
      newResponse.banner.size shouldBe 1
    }
  }
}

