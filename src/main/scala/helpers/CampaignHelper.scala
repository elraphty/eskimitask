package helpers

import db.Db.activeCampaigns
import models.Models.{BidRequest, FindBanner, Banner}

object CampaignHelper {
  def findCampaign(input: BidRequest): Seq[FindBanner] = {
    // convert the impress list to vector for fast searching
    val impression = input.imp.get.toVector

    val data = activeCampaigns.flatMap(camp => {
      for {
        t <- camp.targeting.targetedSiteIds.find(t => t == input.site.id)
        b <- camp.banners.find(b => b.width == impression(0).w.get && b.height == impression(0).h.get)
      } yield FindBanner(camp.id, t, b)
    })

    data
  }
}
