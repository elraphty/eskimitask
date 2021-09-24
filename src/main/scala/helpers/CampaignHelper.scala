package helpers

import db.Db.activeCampaigns
import models.Models.{BidRequest, Campaign, FoundData}

object CampaignHelper {
  def findCampaign(input: BidRequest): Seq[FoundData] = {
    // convert the impress list to vector for fast searching
    val impression = input.imp.get.toVector
    val imp = impression(0);
    var filteredCampaign: Seq[Campaign] = Seq();

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
    // if there is a device  filter with the device geolocation else filter with bidFloor
    val country = input.device.get.geo.get.country.get

    if(country.nonEmpty) {
      filteredCampaign = activeCampaigns.filter(x => {
        // select a data close to the bidRequest and country code
        (x.bid >= imp.bidFloor.get && x.bid <= imp.bidFloor.get + 1) && x.country == country
      })
    } else {
      filteredCampaign = activeCampaigns.filter(x => {
        // select a data close to the bidRequest
        x.bid >= imp.bidFloor.get && x.bid <= imp.bidFloor.get + 1
      })
    }

    val data = filteredCampaign.flatMap(camp => {
      for {
        t <- camp.targeting.targetedSiteIds.find(t => t == input.site.id)
        // If either of width and height value is = 0, search with a range of min and max width/height
        b <- if (width > 0 && height > 0) {
          camp.banners.find(b => b.width == width && b.height == height)
        } else {
          camp.banners.find(b => b.width >= minWidth && b.width <= maxWidth && b.height >= minHeight && b.height <= maxHeight)
        }
      } yield FoundData(camp.id, t, b, camp.bid)
    })

    data
  }
}
