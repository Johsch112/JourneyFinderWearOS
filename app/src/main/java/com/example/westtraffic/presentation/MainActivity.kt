package com.example.westtraffic.presentation

import android.graphics.Color.rgb
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.*
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.ktor.client.statement.HttpResponse
import io.ktor.client.engine.cio.*
import io.ktor.client.statement.bodyAsText

val token = "eyJ4NXQiOiJaV05sTURNNE56SmpZelZrT1dFNU16RTFNalF5TTJaaE5XSm1ORE0zWkRVMk9HRXdOVGxqWVRjNE1tWTNPRGcwWW1JeFlqSTFPVGMzTjJWallqZzRNdyIsImtpZCI6IlpXTmxNRE00TnpKall6VmtPV0U1TXpFMU1qUXlNMlpoTldKbU5ETTNaRFUyT0dFd05UbGpZVGM0TW1ZM09EZzBZbUl4WWpJMU9UYzNOMlZqWWpnNE13X1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJQZ05iSEZUSFVUTzlQVGNuZkhQeFdRbWdDWWthIiwiYXV0IjoiQVBQTElDQVRJT04iLCJiaW5kaW5nX3R5cGUiOiJyZXF1ZXN0IiwiaXNzIjoiaHR0cHM6XC9cL2V4dC1hcGkudmFzdHRyYWZpay5zZVwvdG9rZW4iLCJ0aWVySW5mbyI6eyJVbmxpbWl0ZWQiOnsidGllclF1b3RhVHlwZSI6InJlcXVlc3RDb3VudCIsImdyYXBoUUxNYXhDb21wbGV4aXR5IjowLCJncmFwaFFMTWF4RGVwdGgiOjAsInN0b3BPblF1b3RhUmVhY2giOnRydWUsInNwaWtlQXJyZXN0TGltaXQiOjAsInNwaWtlQXJyZXN0VW5pdCI6bnVsbH19LCJrZXl0eXBlIjoiUFJPRFVDVElPTiIsInN1YnNjcmliZWRBUElzIjpbeyJzdWJzY3JpYmVyVGVuYW50RG9tYWluIjoiY2FyYm9uLnN1cGVyIiwibmFtZSI6ImFwaTAwMTMtcHIiLCJjb250ZXh0IjoiXC9wclwvdjQiLCJwdWJsaXNoZXIiOiJhZG1pbiIsInZlcnNpb24iOiJ2NCIsInN1YnNjcmlwdGlvblRpZXIiOiJVbmxpbWl0ZWQifV0sImF1ZCI6Imh0dHBzOlwvXC9leHQtYXBpLnZhc3R0cmFmaWsuc2UiLCJuYmYiOjE3NDc4NTQ1OTMsImFwcGxpY2F0aW9uIjp7Im93bmVyIjoiUGdOYkhGVEhVVE85UFRjbmZIUHhXUW1nQ1lrYSIsInRpZXJRdW90YVR5cGUiOm51bGwsInRpZXIiOiJVbmxpbWl0ZWQiLCJuYW1lIjoiRGluUmVzYVdhZ3dhbiIsImlkIjozMTc1LCJ1dWlkIjoiMTBkNTg2NmEtZDYzYy00N2Y0LTg1Y2EtNTRlMDQwNWJmOGQ0In0sImF6cCI6IlBnTmJIRlRIVVRPOVBUY25mSFB4V1FtZ0NZa2EiLCJzY29wZSI6ImFwaW06c3Vic2NyaWJlIiwiZXhwIjoxNzQ3OTQwOTkzLCJpYXQiOjE3NDc4NTQ1OTMsImJpbmRpbmdfcmVmIjoiMDIyZmEwZDBlMGI3NTVhZWNmMjk3MmJmZjQ0OTliYzYiLCJqdGkiOiI3NmUzNGFhNC1mNGMwLTQ0ZmUtYTg4Ni02OGZkNjhkOTllZTkifQ.G5lS_pgzAMYh7VVNP11L1sVsmkFjKD7DKehIMkKEL83hKydqVTT-IQ9s92k3Q1GkxmD8bVBn8Ygbtn404egcedDvdQExV9Qy3EvF0HK0cn1olZ4yEW2y1FudVnR5W663V92jMgqFA-RbJzoHqslyS2H30Q66oH3Z1AXii6ssJDWri3H7ZCNaa14sBaPubGBUzhTjd__vjYI38XFI9C-CZiXux1ryKnZ6Syoi-tW6j6Mca3XcsxcHRBOgDoxRzTKoar5gA9tJkuQSZb8SjEB2hL5mvSauusgoCIoulSEPmFBJNRwXYL2NTAjPZ_Lmdn4HvWdU2pTY7Lsrb3dK4z80UQ"
val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}
val myBlue = Color(rgb(90,95,187)) // A blue color with full opacity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp()

        }

    }
}

@Composable
fun WearApp() {
    MaterialTheme {
        FetchButton()
    }
}


@Composable
fun FetchButton() {
    val scope = rememberCoroutineScope()
    val estimatedTime = remember { mutableStateOf("Not loaded yet") }
    val estimatedVehicleNumber = remember { mutableStateOf("Not loaded yet") }

    Box(
        modifier = Modifier
            .fillMaxSize()
        .background(myBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                scope.launch {
                    try {
                        val data = fetchData(token)

                        val timeofDeparture = data.results.firstOrNull()
                            ?.tripLegs?.firstOrNull()?.estimatedDepartureTime ?: "No time found"
                        estimatedTime.value = timeofDeparture

                        val lineNumber = data.results
                            .firstOrNull()
                            ?.tripLegs?.firstOrNull()
                            ?.serviceJourney
                            ?.line?.name ?: "Unknown"
                        estimatedVehicleNumber.value = lineNumber


                       // Log.d("MyApp", "Got time: $timeofDeparture")
                    } catch (e: Exception) {
                        Log.d("MyApp", "Error fetching data: ${e.message}")
                        estimatedTime.value = "Error"
                    }
                }
            }
            ) {
                Text("Fetch Data")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "NEXT DEPARTURE AT: ${estimatedTime.value}")
            Text(text = "${estimatedVehicleNumber.value}")
        }
    }
}



@Composable
fun TimeDisplay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(myBlue), // set background color here
        contentAlignment = Alignment.TopCenter
    ) {
        Text(text = "Current Time Display Here")
    }
}

//results[0].tripLegs[0].serviceJourney.line.name

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
    val serviceJourney: ServiceJourney,  // NOT a List<ServiceJourney>
    val estimatedDepartureTime: String // or a proper Date type if you parse it
)

@Serializable
data class ServiceJourney(
    val line: Line)

@Serializable
data class Line(
    val name: String
)



suspend fun fetchData(token: String): JourneyResponse {
    val fromGid = 9021014004490000
    val toGid = 9021014006242000
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
