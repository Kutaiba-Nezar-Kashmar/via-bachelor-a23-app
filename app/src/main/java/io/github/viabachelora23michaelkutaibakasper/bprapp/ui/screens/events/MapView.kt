package io.github.viabachelora23michaelkutaibakasper.bprapp.ui.screens.events

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import io.github.viabachelora23michaelkutaibakasper.bprapp.data.domain.Event
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.theme.BPRAppTheme

class MapView {

    @Composable
    fun Map(navController: NavController) {
        Column(modifier = Modifier.fillMaxSize())
        {
            val mapView = 0
            val listView = 1
            val tabs = listOf("Map", "List")
            val selectedIndex = remember { mutableIntStateOf(0) }
            PrimaryTabRow(selectedTabIndex = selectedIndex.intValue) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(text = title) },
                        selected = selectedIndex.intValue == index,
                        onClick = { selectedIndex.intValue = index },
                        icon = {
                            when (index) {
                                mapView -> Icon(Icons.Default.Place, contentDescription = "Map")
                                listView -> Icon(Icons.Default.Menu, contentDescription = "List")
                            }
                        }
                    )
                }

            }

            when (selectedIndex.intValue) {
                mapView -> {
                    mapEvents()
                }

                listView -> {
                    EventList()
                }
            }
        }
    }


    @Composable
    fun EventList() {
        var response by remember { mutableStateOf<List<Event>>(emptyList()) }

        val viewModel: MapViewViewModel = viewModel()
        val events by viewModel.eventList.collectAsState(emptyList())
        val isLoading by viewModel.isLoading
        response = events
        Log.d("eventlist", "events: $response")

        if (isLoading) {
            LoadingScreen()
        } else if (response.isEmpty()) {
            LoadingScreen() //should display a No Events message
        } else {
            LazyColumn {
                items(response) { event ->
                    Column {
                        Row(Modifier.padding(4.dp)) {
                            Column {
                                Row {
                                    //text title in bold
                                    Text(text = "Title: ", fontWeight = Bold)
                                    Text(text = event.title ?: "No title")
                                }
                                Text(text = "Description:", fontWeight = Bold)
                                Text(text = event.description ?: "No description")
                                val context = LocalContext.current
                                val intent =
                                    remember { Intent(Intent.ACTION_VIEW, Uri.parse(event.url)) }
                                Button(onClick = { context.startActivity(intent) })
                                {
                                    Text(text = "Link to event")
                                }
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }

    }
}

@Composable
fun mapEvents() {
    val horsens = LatLng(55.862207, 9.844651)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(horsens, 15f)
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false,
                compassEnabled = true,
                mapToolbarEnabled = true,
                rotationGesturesEnabled = true, tiltGesturesEnabled = true
            )
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            properties = MapProperties(isMyLocationEnabled = true)
        ) {
            //create Markers for each event

            var response by remember { mutableStateOf<List<Event>>(emptyList()) }
            val viewModel: MapViewViewModel = viewModel()
            val events by viewModel.eventList.collectAsState(emptyList())
            response = events

            Log.d("events for makers", "getEvents: $response")
            events.forEach { event ->
                Marker(

                    state = MarkerState(
                        position = LatLng(
                            event.location?.geoLocation?.lat ?: 0.0,
                            event.location?.geoLocation?.lng ?: 0.0
                        )
                    ),
                    title = event.title ?: "No title",
                    snippet = event.description ?: "No description",
                )
            }
        }

        FloatingActionButton(
            onClick = {}, content = {
                Column {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }, modifier = Modifier
                .padding(16.dp)
                .size(56.dp)
                .align(Alignment.BottomEnd)
        )
    }

}

@Composable
fun LoadingScreen() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BPRAppTheme {
        // Map()
    }
}

