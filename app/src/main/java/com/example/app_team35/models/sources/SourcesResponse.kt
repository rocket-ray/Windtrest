package com.example.app_team35.models.sources
import com.example.app_team35.models.locationname.Data
import com.example.app_team35.models.locationname.Geometry
import com.squareup.moshi.Json

// NOTE: The JSON->Class mappings in this file were generated using the
// JsonToKotlinClass plugin, using a sample met.no response as the input.
// After generation, the Data class had to be changed so that the
// fields are optional (not all responses include all fields)

data class SourcesResponse(
    @Json(name = "apiVersion")
    val apiVersion: String?,
    @Json(name = "@context")
    val context: String?,
    @Json(name = "createdAt")
    val createdAt: String?,
    @Json(name = "currentItemCount")
    val currentItemCount: Int?,
    @Json(name = "currentLink")
    val currentLink: String?,
    @Json(name = "data")
    val `data`: List<Data>,
    @Json(name = "itemsPerPage")
    val itemsPerPage: Int?,
    @Json(name = "license")
    val license: String?,
    @Json(name = "offset")
    val offset: Int?,
    @Json(name = "queryTime")
    val queryTime: Double?,
    @Json(name = "totalItemCount")
    val totalItemCount: Int?,
    @Json(name = "@type")
    val type: String?
)

data class Data(
    @Json(name = "country")
    val country: String?,
    @Json(name = "countryCode")
    val countryCode: String?,
    @Json(name = "county")
    val county: String?,
    @Json(name = "countyId")
    val countyId: Int?,
    @Json(name = "externalIds")
    val externalIds: List<String>??,
    @Json(name = "geometry")
    val geometry: Geometry?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "masl")
    val masl: Int?,
    @Json(name = "municipality")
    val municipality: String?,
    @Json(name = "municipalityId")
    val municipalityId: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "shortName")
    val shortName: String?,
    @Json(name = "stationHolders")
    val stationHolders: List<String>?,
    @Json(name = "@type")
    val type: String?,
    @Json(name = "validFrom")
    val validFrom: String?,
    @Json(name = "wigosId")
    val wigosId: String?,
    @Json(name = "wmoId")
    val wmoId: Int?
)

data class Geometry(
    @Json(name = "coordinates")
    val coordinates: List<Double>,
    @Json(name = "nearest")
    val nearest: Boolean?,
    @Json(name = "@type")
    val type: String?
)