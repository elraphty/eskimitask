package helpers

import db.Db.activeCampaigns
import models.Models.{BidRequest, FindBanner}

object CampaignHelper {
  def findCampaign(input: BidRequest): Seq[FindBanner] = {
    // convert the impress list to vector for fast searching
    val impression = input.imp.get.toVector
    val imp = impression(0);

    var height: Int = 0;
    var width: Int = 0;
    var minWidth: Int = 0
    var minHeight: Int = 0
    var maxWidth: Int = 0
    var maxHeight: Int = 0

    // check if there are values for height and width else fall back to minimum width, and height
    if(impression(0).w.isDefined) {
      width = imp.w.get
    }

    if(impression(0).h.isDefined) {
      height = imp.h.get
    }

    if(impression(0).hmin.isDefined) {
      minHeight = imp.hmin.get
    }

    if(impression(0).hmax.isDefined) {
      maxHeight = imp.hmax.get
    }

    if(impression(0).wmin.isDefined) {
      minWidth = imp.wmin.get
    }

    if(impression(0).wmax.isDefined) {
      maxWidth = imp.wmax.get
    }

    // Would have preferred doing a query to a Database
    val data = activeCampaigns.flatMap(camp => {
      for {
        t <- camp.targeting.targetedSiteIds.find(t => t == input.site.id)
        // If either of width and height value is = 0, search with a range of min and max width/height
        b <- if (width > 0 && height > 0) {
          camp.banners.find(b => b.width == width && b.height == height)
        } else {
          camp.banners.find(b => b.width >= minWidth && b.width <= maxWidth && b.height >= minHeight && b.height <= maxHeight)
        }
      } yield FindBanner(camp.id, t, b)
    })

    data
  }
}
