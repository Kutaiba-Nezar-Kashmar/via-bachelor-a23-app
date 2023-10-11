package io.github.viabachelora23michaelkutaibakasper.bprapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import io.github.viabachelora23michaelkutaibakasper.bprapp.data.ApolloCountryClient
import io.github.viabachelora23michaelkutaibakasper.bprapp.domain.CountryClient
import io.github.viabachelora23michaelkutaibakasper.bprapp.domain.SimpleCountry
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.theme.BPRAppTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            }
        }

    private fun askPermissions() = when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {

        }

        else -> {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        askPermissions()
        setContent {
            BPRAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    loadEventList()

                    MainScreen()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var presses by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("VibeVerse")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                contentColor = MaterialTheme.colorScheme.secondary, content = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.Check, contentDescription = "Localized description")
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Filled.Build,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Localized description",
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                presses++
            }) {
                Column {
                    Icon(Icons.Default.Build, contentDescription = "Add")
                    Text(text = presses.toString())
                }

            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Map()
            CountryList()
            // CountrySpecific(code ="DK")
        }
    }
}

@Composable
fun Map() {
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
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        eventList.forEach {
            Marker(
                state = MarkerState(position = it.location),
                title = it.name,
                snippet = "description"

            )
        }

    }
}

fun loadEventList() {
    eventList.add(event1)
    eventList.add(event2)
    eventList.add(event3)
}

class Event(val name: String, val location: LatLng)

val eventList = mutableListOf<Event>()
var event1 = Event("Run event", LatLng(55.862207, 9.844651))
var event2 = Event("Dance event", LatLng(55.872207, 9.744651))
var event3 = Event("Drunk event", LatLng(55.882207, 9.644651))

@Composable
fun CountryList() {
    var response by remember { mutableStateOf<List<SimpleCountry>>(emptyList()) }
    LaunchedEffect(Unit) {
        val countryClient: CountryClient = ApolloCountryClient()
        try {
            val countries = countryClient.getCountries("https://countries.trevorblades.com/")
            response = countries

        } catch (e: Exception) {
            Log.d("countriesList", "Failure", e)
        }
    }
    LazyColumn {
        items(response) { country ->
            Column {
                Text("Country: ${country.name ?: "No name"}")
                Text(text = "Country code: ${country.code ?: "No code"}")
                Text(text = "Capital: ${country.capital ?: "No capital"}")
                HorizontalDivider()
            }

        }
    }
}

@Composable
fun CountrySpecific(code: String) {
    var response by remember { mutableStateOf<List<SimpleCountry>>(mutableListOf()) }
    LaunchedEffect(Unit) {
        val countryClient: CountryClient = ApolloCountryClient()
        try {
            val countries = countryClient.getCountries(url="https://countries.trevorblades.com/")
            if (countries != null) {
                response = countries
            }

        } catch (e: Exception) {
            Log.d("countriesList", "Failure", e)
        }
    }
    Column {

        Text(text = "Country: ${response[0].name}")
        Text(text = "Country code: ${response[0].code ?: "No code"}")
        Text(text = "Capital: ${response[0].capital ?: "No capital"}")
        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BPRAppTheme {
        MainScreen()
    }
}