package db

import models.Models.{Banner, Campaign, Targeting}

object Db {
  val activeCampaigns = Seq(
    Campaign(
      id = 1,
      country = "LT",
      targeting = Targeting(
        targetedSiteIds = Vector(1, 2) // Use collection of your choice
      ),
      banners = List(
        Banner(
          id = 1,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 5d
    ),
    Campaign(
      id = 2,
      country = "LT",
      targeting = Targeting(
        targetedSiteIds = Vector(1, 3, 4) // Use collection of your choice
      ),
      banners = List(
        Banner(
          id = 2,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 3.15
    ),
    Campaign(
      id = 3,
      country = "LT",
      targeting = Targeting(
        targetedSiteIds = Vector(1, 3, 4) // Use collection of your choice
      ),
      banners = List(
        Banner(
          id = 3,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 4d
    ),
    Campaign(
      id = 4,
      country = "US",
      targeting = Targeting(
        targetedSiteIds = Vector(5,6) // Use collection of your choice
      ),
      banners = List(
        Banner(
          id = 4,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 3d
    ),
    Campaign(
      id = 5,
      country = "US",
      targeting = Targeting(
        targetedSiteIds = Vector(5,6) // Use collection of your choice
      ),
      banners = List(
        Banner(
          id = 5,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 5d
    ),
    Campaign(
      id = 6,
      country = "LT",
      targeting = Targeting(
        targetedSiteIds = Vector(1,5,6) // Use collection of your choice
      ),
      banners = List(
        Banner(
          id = 6,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 400,
          height = 300
        )
      ),
      bid = 4d
    ),
  )
}


