package com.example.salah_app

import Location
import SalatTimes
import android.annotation.SuppressLint
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.salah_app.ui.theme.SalahappTheme
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.time.LocalDateTime
import java.util.*

class MainActivity : ComponentActivity(), PermissionListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    protected val REQUEST_CHECK_SETTINGS = 0x1

    val athanViewModel by viewModels<AthanViewModel>()

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        getLocation()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Log.i("DENY", "Not working....")
    }

    // TODO: Permissions are already being checked but might need more tweaking.
    @SuppressLint("MissingPermission")
    private fun getLocation() {

        if (!Permissions.checkPermissions(applicationContext)) {
            Permissions.requestPermissions(applicationContext, this)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest.create().apply {
            // interval -> half day,fastest->interval hour, low power
            interval = 43200000L
            fastestInterval = 60
            priority = LocationRequest.PRIORITY_LOW_POWER
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: android.location.Location? ->
                Log.i("Loc Succeed", "Got Location: ${location?.latitude} ${location?.longitude} ${location?.altitude} \n\n")
                val latitude = (location?.latitude ?: 0.0)
                val longitude = (location?.longitude ?: 0.0)
                val altitude = (location?.altitude ?: 0.0)
                athanViewModel.refresh_location(Triple(latitude, longitude, altitude))
                athanViewModel.calculate_athan_local_times()
            }

        task.addOnFailureListener { exception ->
            Log.i("Loc Failed", "Got Location: $exception")
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        this@MainActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLocation()

        setContent {
            SalahappTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(athanViewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val athanTimes = HashMap(SalatTimes(location = Location("TBD", 45.33, -75.33, 0.0, 4.0), calculationMethod = CalculationMethod.ISLAMIC_SOCIETY_OF_NORTH_AMERICA).salatDateTimes)
    val currentTime = LocalDateTime.now()
    val currentAthanIndex = getMostRecentAthan(athanTimes, currentTime)

    SalahappTheme {
        Column() {
            AthanCardRow(athanTimes,currentAthanIndex)
        }
    }
}
