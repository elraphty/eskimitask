package helpers

import db.Db.activeCampaigns
import models.Models.{BidRequest, FindBanner, Banner}

object CampaignHelper {
  def findCampaign(input: BidRequest): Seq[FindBanner] = {
    val data = activeCampaigns.flatMap(cam => {
      for {
        t <- cam.targeting.targetedSiteIds.find(t => t == input.site.id)
        b <- cam.banners.find(b => b.width == 300 && b.height == 250)
      } yield FindBanner(cam.id, t, b)
    })

    data
  }
}
