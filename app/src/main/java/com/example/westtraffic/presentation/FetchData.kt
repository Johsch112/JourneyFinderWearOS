package com.example.westtraffic.presentation
import android.app.Application
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

suspend fun fetchData(token: String): JourneyResponse {
    val token = getToken()
    val fromGid = 9021014001760000
    val toGid = 9021014007340000
    val url = "https://ext-api.vasttrafik.se/pr/v4/journeys?originGid=$fromGid&destinationGid=$toGid"

    val response: JourneyResponse = client.get(url){
        headers {
            append("Content-Type", "application/json")
            append("Authorization", "Bearer $token")
        }
    }.body()

    Log.d("MyApp", "Parsed response: $response")

    return response
}


suspend fun getToken(): TokenResponse{

    val url = "https://ext-api.vasttrafik.se/token"

    val response: TokenResponse = client.post(url){
        headers {
            append("Content-Type", "application/x-www-form-urlencoded")
            append("Authorization", "Basic UGdOYkhGVEhVVE85UFRjbmZIUHhXUW1nQ1lrYTpkY1I3cU1veU5VS2ZmZllsS3BrNlVZSmxmM3Nh")
        }
            setBody("grant_type=client_credentials")
    }.body()

    Log.d("MyApp", "Parsed response: $response")

    return response
}

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = getToken()
                Log.d("Token", token.access_token)
            } catch (e: Exception) {
                Log.e("MyApp", "Error fetching token", e)
            }
        }
    }
}

@Serializable
data class TokenResponse(
    val access_token: String,
    val scope: String,
    val token_type: String,
    val expires_in: Int
)


//UGdOYkhGVEhVVE85UFRjbmZIUHhXUW1nQ1lrYTpkY1I3cU1veU5VS2ZmZllsS3BrNlVZSmxmM3Nh



//BRIDGES
@Serializable
data class JourneyResponse(
    val results: List<Journey>
)

@Serializable
data class Journey(
    val tripLegs: List<Tripleg>
)

@Serializable
data class Tripleg(
    val serviceJourney: ServiceJourney,
    val estimatedDepartureTime: String // or a proper Date type if you parse it
)

@Serializable
data class ServiceJourney(
    val line: Line)

@Serializable
data class Line(
    val name: String
)