package actors

import akka.actor.Actor
import models.Models.{BidRequest, BidResponse}
import helpers.CampaignHelper.findCampaign

object Bid {
  class BidActor extends Actor {
    override def receive: Receive = {
      case input: BidRequest =>
        val data = findCampaign(input).toVector

        if(data.isEmpty) sender() ! None
        else {
          // Generate a random number for the string
          val rand = scala.util.Random
          val randomInt = rand.nextInt(9999999)

          // Select a random data from the data size
          val randBid = rand.nextInt(data.size)

          sender() ! Some(BidResponse(randomInt.toString, input.id, data(randBid).bid,
            Some(data(randBid).id.toString), Some(data(randBid).banner)))
        }
      case _ => sender() ! "Wrong Input"
    }
  }
}
