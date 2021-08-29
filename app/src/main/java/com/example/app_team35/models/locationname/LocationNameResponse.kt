package com.example.app_team35.models.locationname
import com.squareup.moshi.Json

data class LocationNameResponse(
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
    val `data`: List<Data>?,
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
    @Json(name = "feature")
    val feature: String?,
    @Json(name = "geometry")
    val geometry: Geometry?,
    @Json(name = "name")
    val name: String?
)

data class Geometry(
    @Json(name = "coordinates")
    val coordinates: List<Double>?,
    @Json(name = "nearest")
    val nearest: Boolean?,
    @Json(name = "@type")
    val type: String?
)