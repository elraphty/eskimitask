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
      var height: Int = imp.h.getOrElse(0)
      var width: Int = imp.w.getOrElse(0)
      var minWidth: Int = imp.wmin.getOrElse(0)
      var minHeight: Int = imp.hmin.getOrElse(0)
      var maxWidth: Int = imp.hmax.getOrElse(0)
      var maxHeight: Int = imp.hmax.getOrElse(0)

      var loopFilteredCampaign: Seq[Campaign] = Seq();

      // VALIDATE BID FLOOR AND COUNTRY

      // If bid floor and country is defined filter with both values else if either of
      // country or bid floor is defined filter with their values else, don't filter
      if (country.nonEmpty && imp.bidFloor.isDefined) {
        loopFilteredCampaign = activeCampaigns.filter(x => {
          // select a data close to the bidRequest and country code
          (x.bid >= imp.bidFloor.get && x.bid <= imp.bidFloor.get + 1) && x.country == country
        })
      }
      else if (country.nonEmpty && impression.isEmpty) {
        loopFilteredCampaign = activeCampaigns.filter(x => {
          // select a data close to the bidRequest and country code
          x.country == country
        })
      } else if (imp.bidFloor.isDefined && country.isEmpty) {
        loopFilteredCampaign = activeCampaigns.filter(x => {
          // select a data close to the bidRequest and country code
          (x.bid >= imp.bidFloor.get && x.bid <= imp.bidFloor.get + 1)
        })
      } else {
        loopFilteredCampaign = activeCampaigns
      }

      // If Width or Height is not provided OR Any of the Min and Max width or height is not provided
      // Don't run
      if ((width != 0 && height != 0) || (minWidth != 0 && minHeight != 0 && minHeight != 0 && maxHeight != 0)) {
        val campaigns: Seq[FoundData] = loopFilteredCampaign.flatMap(camp2 => {
          for {
            t <- camp2.targeting.targetedSiteIds.find(t => t == input.site.id)
            // If either of width and height value is = 0, search with a range of min and max width/height
            b <- if (width > 0 && height > 0) {
              camp2.banners.find(b => b.width == width && b.height == height)
            } else {
              camp2.banners.find(b => b.width >= minWidth && b.width <= maxWidth
                && b.height >= minHeight && b.height <= maxHeight)
            }
          } yield FoundData(camp2.id, t, b, camp2.bid)
        })

        foundCampaigns = foundCampaigns ++ campaigns
      }
    });

    foundCampaigns
  }
}
