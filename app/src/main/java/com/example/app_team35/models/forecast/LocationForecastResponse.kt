package com.example.app_team35.models.forecast

// NOTE: The JSON->Class mappings in this file were generated using the
// JsonToKotlinClass plugin, using a sample met.no response as the input.
// After generation, the Data class had to be changed so that the
// nextNHours fields are optional (not all responses include them)

import com.squareup.moshi.Json

data class LocationForecastResponse(
    @Json(name = "geometry")
    val geometry: Geometry,
    @Json(name = "properties")
    val properties: Properties,
    @Json(name = "type")
    val type: String
)

data class Geometry(
    @Json(name = "coordinates")
    val coordinates: List<Double>,
    @Json(name = "type")
    val type: String
)

data class Properties(
    @Json(name = "meta")
    val meta: Meta,
    @Json(name = "timeseries")
    val timeseries: List<Timesery>
)

data class Meta(
    @Json(name = "units")
    val units: Units,
    @Json(name = "updated_at")
    val updatedAt: String
)

data class Timesery(
    @Json(name = "data")
    val `data`: Data,
    @Json(name = "time")
    val time: String
)

data class Units(
    @Json(name = "air_pressure_at_sea_level")
    val airPressureAtSeaLevel: String?,
    @Json(name = "air_temperature")
    val airTemperature: String?,
    @Json(name = "air_temperature_max")
    val airTemperatureMax: String?,
    @Json(name = "air_temperature_min")
    val airTemperatureMin: String?,
    @Json(name = "cloud_area_fraction")
    val cloudAreaFraction: String?,
    @Json(name = "cloud_area_fraction_high")
    val cloudAreaFractionHigh: String?,
    @Json(name = "cloud_area_fraction_low")
    val cloudAreaFractionLow: String?,
    @Json(name = "cloud_area_fraction_medium")
    val cloudAreaFractionMedium: String?,
    @Json(name = "dew_point_temperature")
    val dewPointTemperature: String?,
    @Json(name = "fog_area_fraction")
    val fogAreaFraction: String?,
    @Json(name = "precipitation_amount")
    val precipitationAmount: String?,
    @Json(name = "precipitation_amount_max")
    val precipitationAmountMax: String?,
    @Json(name = "precipitation_amount_min")
    val precipitationAmountMin: String?,
    @Json(name = "probability_of_precipitation")
    val probabilityOfPrecipitation: String?,
    @Json(name = "probability_of_thunder")
    val probabilityOfThunder: String?,
    @Json(name = "relative_humidity")
    val relativeHumidity: String?,
    @Json(name = "ultraviolet_index_clear_sky")
    val ultravioletIndexClearSky: String?,
    @Json(name = "wind_from_direction")
    val windFromDirection: String?,
    @Json(name = "wind_speed")
    val windSpeed: String?,
    @Json(name = "wind_speed_of_gust")
    val windSpeedOfGust: String?
)

data class Data(
    @Json(name = "instant")
    val instant: Instant,
    @Json(name = "next_12_hours")
    val next12Hours: Next12Hours?,
    @Json(name = "next_1_hours")
    val next1Hours: Next1Hours?,
    @Json(name = "next_6_hours")
    val next6Hours: Next6Hours?
)

data class Instant(
    @Json(name = "details")
    val details: Details
)

data class Next12Hours(
    @Json(name = "details")
    val details: DetailsX?,
    @Json(name = "summary")
    val summary: Summary?
)

data class Next1Hours(
    @Json(name = "details")
    val details: DetailsXX?,
    @Json(name = "summary")
    val summary: SummaryX?
)

data class Next6Hours(
    @Json(name = "details")
    val details: DetailsXXX?,
    @Json(name = "summary")
    val summary: SummaryXX?
)

data class Details(
    @Json(name = "air_pressure_at_sea_level")
    val airPressureAtSeaLevel: Double?,
    @Json(name = "air_temperature")
    val airTemperature: Double?,
    @Json(name = "cloud_area_fraction")
    val cloudAreaFraction: Double?,
    @Json(name = "cloud_area_fraction_high")
    val cloudAreaFractionHigh: Double?,
    @Json(name = "cloud_area_fraction_low")
    val cloudAreaFractionLow: Double?,
    @Json(name = "cloud_area_fraction_medium")
    val cloudAreaFractionMedium: Double?,
    @Json(name = "dew_point_temperature")
    val dewPointTemperature: Double?,
    @Json(name = "fog_area_fraction")
    val fogAreaFraction: Double?,
    @Json(name = "relative_humidity")
    val relativeHumidity: Double?,
    @Json(name = "ultraviolet_index_clear_sky")
    val ultravioletIndexClearSky: Double?,
    @Json(name = "wind_from_direction")
    val windFromDirection: Double?,
    @Json(name = "wind_speed")
    val windSpeed: Double?,
    @Json(name = "wind_speed_of_gust")
    val windSpeedOfGust: Double?
)

data class DetailsX(
    @Json(name = "probability_of_precipitation")
    val probabilityOfPrecipitation: Double?
)

data class Summary(
    @Json(name = "symbol_code")
    val symbolCode: String?,
    @Json(name = "symbol_confidence")
    val symbolConfidence: String?
)

data class DetailsXX(
    @Json(name = "precipitation_amount")
    val precipitationAmount: Double?,
    @Json(name = "precipitation_amount_max")
    val precipitationAmountMax: Double?,
    @Json(name = "precipitation_amount_min")
    val precipitationAmountMin: Double?,
    @Json(name = "probability_of_precipitation")
    val probabilityOfPrecipitation: Double?,
    @Json(name = "probability_of_thunder")
    val probabilityOfThunder: Double?
)

data class SummaryX(
    @Json(name = "symbol_code")
    val symbolCode: String
)

data class DetailsXXX(
    @Json(name = "air_temperature_max")
    val airTemperatureMax: Double?,
    @Json(name = "air_temperature_min")
    val airTemperatureMin: Double?,
    @Json(name = "precipitation_amount")
    val precipitationAmount: Double?,
    @Json(name = "precipitation_amount_max")
    val precipitationAmountMax: Double?,
    @Json(name = "precipitation_amount_min")
    val precipitationAmountMin: Double?,
    @Json(name = "probability_of_precipitation")
    val probabilityOfPrecipitation: Double?
)

data class SummaryXX(
    @Json(name = "symbol_code")
    val symbolCode: String?
)