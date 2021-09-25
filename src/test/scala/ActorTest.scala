import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import akka.pattern.ask
import models.Models.{BidRequest, BidResponse, Device, Geo, Impression, Site, User}
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import actors.Bid._

class ActorTest extends TestKit(ActorSystem("BasicSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {

  implicit val timeout: Timeout = Timeout(10 seconds);

  // After test Kill the Actor system
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val bidActor: ActorRef = system.actorOf(Props[BidActor])

  "A Bid Actor" should {
    "Send back 'Wrong Input' if a wrong data is sent to it" in {
      val message = "Hi!"
      bidActor ! message

      expectMsg("Wrong Input")
    }

    "Expect a BidResponse if it receives a BidRequest that matches a campaign " in {
      val message = BidRequest(
        "1",
        Some(List(Impression("1", Some(50), Some(200), Some(300), Some(50), Some(100), Some(250),
          Some(3.5)))), Site(1, "me.com"),
        Some(User("1", Some(Geo(Some("LT"))))),
        Some(Device("1", Some(Geo(Some("LT")))))
      )

      val reply = (bidActor ? message)

      reply.foreach(data => {
        assert(data.isInstanceOf[Some[BidResponse]])
      })
    }

    "Expect a None value if it receives a BidRequest that does not match any campaign " in {
      val message = BidRequest(
        "1",
        Some(List(Impression("1", Some(50), Some(200), Some(300), Some(50), Some(100), Some(250),
          Some(3.5)))), Site(200, "me.com"),
        Some(User("1", Some(Geo(Some("LT"))))),
        Some(Device("1", Some(Geo(Some("LT")))))
      )

      val reply = (bidActor ? message)

      reply.foreach(data => {
        assert(data == None)
      })
    }
  }
}
