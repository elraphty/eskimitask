package actors

import akka.actor.Actor
import models.Models.{Banner, BidRequest}
import helpers.CampaignHelper.findCampaign

object Bid {
  class BidActor extends Actor {
    override def receive: Receive = {
      case input: BidRequest =>
        val data = findCampaign(input)
        sender() ! data
    }
  }
}
