package database.car_ad_photos

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import database.ads.AdsTable

object CarAdsPhotosTable: Table("car_ads_photos") {
    val id = varchar("id", 36).autoIncrement()
    val carAdId = varchar("car_ad_id", 36).references(AdsTable.id, onDelete = ReferenceOption.CASCADE)
    val uri = varchar("uri", 255)

    override val primaryKey = PrimaryKey(id)
}