package com.example.westtraffic.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import io.ktor.util.date.Month
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun FetchButtonToHOME() {
    val scope = rememberCoroutineScope()
    val estimatedTime = remember { mutableStateOf("Not loaded") }
    val estimatedVehicleNumber = remember { mutableStateOf("Not loaded") }
    val dateState = remember { mutableStateOf<OffsetDateTime?>(null) }

    val fromGid =9021014001760000
    val toGid =9021014007340000


        Column(horizontalAlignment = Alignment.CenterHorizontally)  {
            Button(onClick = {
                scope.launch {
                    try {
                        val token = getToken()

                        val data = fetchData(token.access_token, fromGid, toGid)

                        val timeofDeparture = data.results.firstOrNull()
                            ?.tripLegs?.firstOrNull()?.estimatedDepartureTime ?: "No time found"
                        estimatedTime.value = timeofDeparture

                        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                        dateState.value = OffsetDateTime.parse(estimatedTime.value, formatter)

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
            },
                modifier = Modifier.width(100.dp)
            ) {
                Text("GoHome")
            }

//            Spacer(modifier = Modifier.height(8.dp))
                //Text(text = "DEPARTURE: ${estimatedTime.value}", textAlign = TextAlign.Center,)
            Text(text = "DEPARTURE: ${dateState.value?.hour ?: ""}${":"}${dateState.value?.minute ?: ""}",
                textAlign = TextAlign.Center,)

            Text(text = "VEHICLE: ${estimatedVehicleNumber.value}",
                textAlign = TextAlign.Center,)
        }
    }

@Composable
fun FetchButtonToSCHOOL() {
    val scope = rememberCoroutineScope()
    val estimatedTime = remember { mutableStateOf("Not loaded") }
    val estimatedVehicleNumber = remember { mutableStateOf("Not loaded") }
    val dateState = remember { mutableStateOf<OffsetDateTime?>(null) }
    val fromGid = 9021014007340000
    val toGid = 9021014001760000

        Column(horizontalAlignment = Alignment.CenterHorizontally)  {
            Text(text = "DEPARTURE: ${dateState.value?.hour ?: ""}${":"}${dateState.value?.minute ?: ""}",
                textAlign = TextAlign.Center,)

            Text(text = "VEHICLE: ${estimatedVehicleNumber.value}",
                textAlign = TextAlign.Center,)
            Button(onClick = {
                scope.launch {
                    try {
                        val token = getToken()

                        val data = fetchData(token.access_token, fromGid, toGid)

                        val timeofDeparture = data.results.firstOrNull()
                            ?.tripLegs?.firstOrNull()?.estimatedDepartureTime ?: "No time found"
                        estimatedTime.value = timeofDeparture

                        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                        dateState.value = OffsetDateTime.parse(estimatedTime.value, formatter)

                        val lineNumber = data.results
                            .firstOrNull()
                            ?.tripLegs?.firstOrNull()?.serviceJourney?.line?.name ?: "Unknown"
                        estimatedVehicleNumber.value = lineNumber


                        // Log.d("MyApp", "Got time: $timeofDeparture")
                    } catch (e: Exception) {
                        Log.d("MyApp", "Error fetching data: ${e.message}")
                        estimatedTime.value = "Error"
                        estimatedVehicleNumber.value ="Error"
                    }
                }
            },
                    modifier = Modifier.width(100.dp)
            ) {
                Text("GoSchool")
            }

//            Spacer(modifier = Modifier.height(8.dp))

        }
    }
