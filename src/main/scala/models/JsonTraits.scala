package models

import models.Models.{Banner, BidRequest, BidResponse, Device, Geo, Impression, Site, User}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonTraits extends DefaultJsonProtocol {
  implicit val impressionJson: RootJsonFormat[Impression] = jsonFormat8(Impression)
  implicit val geoJson: RootJsonFormat[Geo] = jsonFormat1(Geo)
  implicit val deviceJson: RootJsonFormat[Device] = jsonFormat2(Device)
  implicit val userJson: RootJsonFormat[User] = jsonFormat2(User)
  implicit val siteJson: RootJsonFormat[Site] = jsonFormat2(Site)
  implicit val bannerJson: RootJsonFormat[Banner] = jsonFormat4(Banner)

  implicit val bidRequestJson: RootJsonFormat[BidRequest] = jsonFormat5(BidRequest)
  implicit val bidResponseJson: RootJsonFormat[BidResponse] = jsonFormat5(BidResponse)
}

