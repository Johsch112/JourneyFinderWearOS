package com.example.westtraffic.presentation
import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
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
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

suspend fun fetchData(token: String, fromGid: Long, toGid: Long): JourneyResponse {
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
    val estimatedDepartureTime: String
)

@Serializable
data class ServiceJourney(
    val line: Line)

@Serializable
data class Line(
    val name: String
)

data class HomeDATA(
    val departure: String,
    val vehicle: String
)

suspend fun FetchToHOME(toGid: Long, fromGid: Long):HomeDATA {


//    val fromGid =9021014001760000
//    val toGid =9021014007340000

               return try {
                    val token = getToken()

                    val data = fetchData(token.access_token, fromGid, toGid)

                    val timeofDeparture = data.results.firstOrNull()
                        ?.tripLegs?.firstOrNull()?.estimatedDepartureTime ?: "No time found"


                    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                   val date = OffsetDateTime.parse(timeofDeparture, formatter)

                    val lineNumber = data.results
                        .firstOrNull()
                        ?.tripLegs?.firstOrNull()?.serviceJourney?.line?.name ?: "Unknown"

                   HomeDATA(timeofDeparture, lineNumber)

                } catch (e: Exception) {
                    Log.d("MyApp", "Error fetching data: ${e.message}")
                   HomeDATA(departure = "Error", vehicle = "Error")
                }
}