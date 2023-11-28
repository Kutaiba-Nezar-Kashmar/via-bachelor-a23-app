package io.github.viabachelora23michaelkutaibakasper.bprapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.viabachelora23michaelkutaibakasper.bprapp.data.sign_in.AuthenticationClient
import io.github.viabachelora23michaelkutaibakasper.bprapp.data.sign_in.IAuthenticationClient
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.screens.events.CreateEventInviteFriendsScreen
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.screens.events.eventdetails.EventDetailsScreen

import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.screens.events.MapView
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.screens.events.createevent.CreateEventDateAndTimeScreen
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.screens.events.createevent.CreateEventDetailsScreen
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.screens.events.createevent.CreateEventImagesScreen
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.screens.events.createevent.CreateEventLocationScreen
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.screens.events.createevent.CreateEventTitleAndDescriptionScreen
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.screens.profile.ProfileScreen
import io.github.viabachelora23michaelkutaibakasper.bprapp.ui.theme.BPRAppTheme

@ExperimentalLayoutApi
@ExperimentalFoundationApi
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

    @OptIn(ExperimentalMaterial3Api::class)
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

                    val navController: NavHostController = rememberNavController()

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                navigationIcon = {
                                    if (navController.previousBackStackEntry != null) {
                                        IconButton(onClick = { navController.popBackStack() }) {
                                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "backIcon")
                                        }
                                    }
                                },
                                title = {
                                    Text("VibeVerse")
                                }
                            )
                        },
                        bottomBar = {
                            val selectedIndex = remember { mutableIntStateOf(0) }
                            NavigationBar(
                                contentColor = MaterialTheme.colorScheme.secondary,
                                content = {
                                    val isDarkTheme = isSystemInDarkTheme()
                                    NavigationBarItem(
                                        label = { Text("Home") },
                                        selected = (selectedIndex.intValue == 0),
                                        onClick = {
                                            selectedIndex.intValue = 0;
                                            navController.navigate(BottomNavigationScreens.Map.name)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.LocationOn,
                                                contentDescription = "Map",
                                                tint = if (isDarkTheme) Color.White else Color.Black
                                            )
                                        })
                                    NavigationBarItem(
                                        label = { Text("Recomm.") },
                                        selected = (selectedIndex.intValue == 1),
                                        onClick = {
                                            selectedIndex.intValue =
                                                1; navController.navigate(BottomNavigationScreens.Recommendations.name)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.ThumbUp,
                                                contentDescription = "Achievements",
                                                tint = if (isDarkTheme) Color.White else Color.Black
                                            )
                                        })
                                    NavigationBarItem(
                                        label = { Text("Medals") },
                                        selected = (selectedIndex.intValue == 2),
                                        onClick = {
                                            selectedIndex.intValue =
                                                2; navController.navigate(BottomNavigationScreens.Achievements.name)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.Star,
                                                contentDescription = "Achievements",
                                                tint = if (isDarkTheme) Color.White else Color.Black
                                            )
                                        })
                                    NavigationBarItem(
                                        label = { Text("Profile") },
                                        selected = (selectedIndex.intValue == 3),
                                        onClick = {
                                            selectedIndex.intValue = 3;
                                            navController.navigate(BottomNavigationScreens.Profile.name)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.Person,
                                                contentDescription = "Profile",
                                                tint = if (isDarkTheme) Color.White else Color.Black
                                            )
                                        })
                                }
                            )
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = BottomNavigationScreens.Map.name,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            val authentication: IAuthenticationClient = AuthenticationClient()
                            composable(BottomNavigationScreens.Map.name) {
                                MapView().Map(navController = navController)
                            }
                            composable(BottomNavigationScreens.Recommendations.name) {
                                Text(text = authentication.getCurrentUser()?.displayName.toString() + ": Recommendations")
                            }
                            composable(BottomNavigationScreens.Achievements.name) {
                                Text(text = "Achievements")
                            }
                            composable(BottomNavigationScreens.Profile.name) {
                                ProfileScreen()
                            }
                            composable(CreateEventScreens.Title.name) {
                                CreateEventTitleAndDescriptionScreen(navController = navController)
                            }
                            composable(CreateEventScreens.Location.name) {
                                CreateEventLocationScreen(navController = navController)
                            }
                            composable(CreateEventScreens.DateAndTime.name) {
                                CreateEventDateAndTimeScreen(navController = navController)
                            }
                            composable(CreateEventScreens.Details.name) {
                                CreateEventDetailsScreen(navController = navController)
                            }
                            composable(CreateEventScreens.Images.name) {
                                CreateEventImagesScreen(navController = navController)
                            }
                            composable(CreateEventScreens.InviteFriends.name) {
                                CreateEventInviteFriendsScreen(navController = navController)
                            }
                            composable(BottomNavigationScreens.EventDetails.name) {
                              EventDetailsScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class BottomNavigationScreens() {
    Map,
    Recommendations,
    Achievements,
    Profile,
    EventDetails
}

enum class CreateEventScreens() {
    Title,
    Location,
    DateAndTime,
    Details,
    Images,
    InviteFriends
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BPRAppTheme {
    }
}