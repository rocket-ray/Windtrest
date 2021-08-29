package com.example.app_team35.repositories

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.app_team35.models.forecast.LocationForecastResponse
import com.example.app_team35.models.locationname.LocationNameResponse
import com.example.app_team35.models.sources.SourcesResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://in2000-apiproxy.ifi.uio.no/"
private const val BASE_URL_FROST = "https://in2000-frostproxy.ifi.uio.no/"

// Moshi is used to map between raw JSON and Kotlin data objects
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Log all HTTP calls
val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

// Set up interceptors
val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .addInterceptor(BasicAuthInterceptor("341e6e54-489a-4870-8a41-eb762e696718", ""))
    .build()

// Initialize Retrofit. This library allows us to call the met.no endpoint through a simple service.
// Retrofit is able to utilize Moshi to do the JSON -> object mapping.
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

// The "frost" api is unfortunately on a different subdomain, requiring a separate retrofit instance
private val retrofitFrost = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_FROST)
    .client(client)
    .build()

/**
 * The interface to the api.met.no forecast api
 */
interface ForecastService {
    /**
     * Get weather information for a location
     * When invoked, this will make a call equivalent to:
     *      curl -X GET -v --user 341e6e54-489a-4870-8a41-eb762e696718: 'https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=60.179&lon=11.044'
     */
    @GET("weatherapi/locationforecast/2.0/complete")
    suspend fun getForecast(@Query("lat") latitude: Double, @Query("lon") longitude: Double): LocationForecastResponse
}

/**
 * The interface to the frost.met.no api
 */
interface FrostService {
    /**
     * Get a list of sensor locations given a place name. Add * to the query for wildcard searches.
     * When invoked, this will make a call equivalent to:
     *     curl -X GET -v --user 341e6e54-489a-4870-8a41-eb762e696718: 'https://frost.met.no/sources/v0.jsonld?types=SensorSystem&name=OSLO*'
     */
    @GET("sources/v0.jsonld")
    suspend fun getSources(@Query("types") types: String, @Query("name") name: String): SourcesResponse

    /**
     * Try to find nearest location name(s) given a geometry expression like "nearest(POINT(10.994392%2059.9171086))" where 10.99... is longitude and 59.91... is latitude
     * The geometry specification is here: https://frost.met.no/concepts2.html#geometryspecification
     */
    @GET("locations/v0.jsonld")
    suspend fun getLocationNames(@Query("geometry") geometry: String): LocationNameResponse
}

/**
 * lazy-initialized Retrofit service
 */
object WeatherApi {
    val forecastService : ForecastService by lazy { retrofit.create(ForecastService::class.java) }
    val frostService : FrostService by lazy { retrofitFrost.create(FrostService::class.java) }
}
