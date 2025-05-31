package com.example.westtraffic.presentation


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.wear.compose.material.*





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FetchButtonToHOME() {
    val scope = rememberCoroutineScope()
    val departureState = remember { mutableStateOf("Not loaded") }
    val vehicleState = remember { mutableStateOf("Not loaded") }
    val dateState = remember { mutableStateOf<OffsetDateTime?>(null) }

    var showPopup by remember { mutableStateOf(false) }

    val fromGid = 9021014001760000
    val toGid = 9021014007340000

    Column(horizontalAlignment = Alignment.CenterHorizontally)  {
        Button(onClick = {

            scope.launch {
                showPopup = true

                val dataRES = FetchToHOME(fromGid, toGid)
                departureState.value = dataRES.departure
                vehicleState.value = dataRES.vehicle

                //Make time look normarl
                val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                dateState.value = OffsetDateTime.parse(departureState.value, formatter)

            }
        },
            modifier = Modifier.width(100.dp)
        )

        {
            Text("GoHome")
        }
        Text(text = "DEPARTURE: ${dateState.value?.toLocalTime() ?: "Not loaded"}")
        Text(text = "VEHICLE: ${vehicleState.value }")
    }

    if (showPopup) {
        ShowPopup(onDismiss = { showPopup = false })
    }
}


@Composable
fun FetchButtonToSCHOOL() {
    val scope = rememberCoroutineScope()
    val departureState = remember { mutableStateOf("Not loaded") }
    val vehicleState = remember { mutableStateOf("Not loaded") }
    val dateState = remember { mutableStateOf<OffsetDateTime?>(null) }

    val fromGid = 9021014007340000
    val toGid = 9021014001760000

        Column(horizontalAlignment = Alignment.CenterHorizontally)  {

            Text(text = "DEPARTURE: ${dateState.value?.toLocalTime() ?: "Not loaded"}")
            Text(text = "VEHICLE: ${vehicleState.value }")

            Button(onClick = {
                scope.launch {

                    val dataRES = FetchToHOME(fromGid, toGid)
                    departureState.value = dataRES.departure
                    vehicleState.value = dataRES.vehicle

                    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    dateState.value = OffsetDateTime.parse(departureState.value, formatter)
                }
            },
                    modifier = Modifier.width(100.dp)
            ) {
                Text("GoSchool")
            }
        }
    }

@Composable
fun ShowPopup(onDismiss: () -> Unit) {
    Card(
        onClick = onDismiss,
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp),
        enabled = true
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Duplicate Question")
            Text("This question has already been asked.")
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    }
}