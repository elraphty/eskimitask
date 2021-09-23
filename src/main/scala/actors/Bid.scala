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
          val bidImpression = input.imp.get
          sender() ! Some(BidResponse("2333", input.id, bidImpression.head.bidFloor.get, Some("333"),Some(data(0).banner)))
        }
      case _ => sender() ! "Wrong Input"
    }
  }
}
