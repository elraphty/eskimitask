package helpers

import db.Db.activeCampaigns
import models.Models.{BidRequest, Campaign, FoundData}

object CampaignHelper {
  def findCampaign(input: BidRequest): Seq[FoundData] = {
    // convert the impress list to vector for fast searching
    val impression = input.imp.get.toVector

    val country = input.device.get.geo.get.country.get

    var foundCampaigns: Seq[FoundData] = Seq();

    impression.foreach(imp => {
      val height: Int = imp.h.getOrElse(0)
      val width: Int = imp.w.getOrElse(0)
      val minWidth: Int = imp.wmin.getOrElse(0)
      val minHeight: Int = imp.hmin.getOrElse(0)
      val maxWidth: Int = imp.hmax.getOrElse(0)
      val maxHeight: Int = imp.hmax.getOrElse(0)

      val campaigns = for {
        active <- activeCampaigns
        if ((active.bid >= imp.bidFloor.get && active.bid <= imp.bidFloor.get + 1) && active.country == country)

        // targeting sites
        t <- active.targeting.targetedSiteIds
        if(t == input.site.id)

        // Banners
        banners <- active.banners
        if(
          if(width == 0 || height == 0) banners.width >= minWidth && banners.width <= maxWidth
          && banners.height >= minHeight && banners.height <= maxHeight
          else banners.width == width && banners.height == height)
      } yield FoundData(active.id, t, banners, active.bid)

      foundCampaigns = foundCampaigns ++ campaigns

    });

    foundCampaigns
  }
}
