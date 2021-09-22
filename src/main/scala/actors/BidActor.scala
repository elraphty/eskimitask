package actors

import akka.actor.Actor
import models.Models.{Banner, BidRequest, Campaign, Targeting}
import db.Db.activeCampaigns

object BidActor extends Actor {
  override def receive: Receive = {
    case input: BidRequest =>
  }
}
