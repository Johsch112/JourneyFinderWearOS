package com.example.westtraffic.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch



@Composable
fun FetchButton() {
    val scope = rememberCoroutineScope()
    val estimatedTime = remember { mutableStateOf("Not loaded yet") }
    val estimatedVehicleNumber = remember { mutableStateOf("Vehicle not loaded yet") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(myBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally)  {
            Button(onClick = {
                scope.launch {
                    try {
                        val data = fetchData(token.access_token)

                        val timeofDeparture = data.results.firstOrNull()
                            ?.tripLegs?.firstOrNull()?.estimatedDepartureTime ?: "No time found"
                        estimatedTime.value = timeofDeparture

                        val lineNumber = data.results
                            .firstOrNull()
                            ?.tripLegs?.firstOrNull()?.serviceJourney?.line?.name ?: "Unknown"
                        estimatedVehicleNumber.value = lineNumber


                        // Log.d("MyApp", "Got time: $timeofDeparture")
                    } catch (e: Exception) {
                        Log.d("MyApp", "Error fetching data: ${e.message}")
                        estimatedTime.value = "Error"
                    }
                }
            }
            ) {
                Text("DATA")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "DEPARTURE: ${estimatedTime.value}", textAlign = TextAlign.Center,)
                Text(text = "VEHICLE: ${estimatedVehicleNumber.value}", textAlign = TextAlign.Center,)
            }


        }
    }
}