package com.example.salah_app

import Location
import SalatTimes
import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.salah_app.ui.theme.SalahappTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import compose.icons.WeatherIcons
import compose.icons.weathericons.Sunrise
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    protected  val REQUEST_CHECK_SETTINGS = 0x1
    val athanViewModel by viewModels<AthanViewModel>()

    private fun Context.checkSinglePermission(permission: String) : Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!checkSinglePermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1);

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
            .addOnSuccessListener { location : android.location.Location? ->
                Log.i("Loc Succeed", "Got Location: ${location?.latitude} ${location?.longitude} ${location?.altitude} \n\n")
                val latitude = (location?.latitude ?: 0.0)
                val longitude = (location?.longitude ?: 0.0)
                val altitude =  (location?.altitude ?: 0.0)
                athanViewModel.refresh_location(Triple(latitude,longitude,altitude))
                athanViewModel.calculate_athan_local_times()

            }

        task.addOnFailureListener { exception ->
            Log.i("Loc Failed", "Got Location: $exception")
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(this@MainActivity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }


        setContent {
            SalahappTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column() {
                        FindLocation(athanViewModel)
                        AthanCardRow(athanViewModel)
                    }

                }
            }
        }
    }
}

@Composable
fun CardDemo(SalahName: String, ArbitraryTime: String) {
    Card(
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .padding(25.dp)
                .fillMaxWidth()
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){


                // TODO: Figure out how to enhance color, sizing, and content description!
                Icon(
                    imageVector = WeatherIcons.Sunrise,
                    contentDescription = null,
                    tint= Color(0xFFff6f00),
                    modifier = Modifier.
                    then(Modifier.size(48.dp))

                )

            }

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Light,fontSize = 20.sp, color = Color(0xFF607d8b) )
                    ) {
                        append("Upcoming Prayer")
                    }
                }
            )

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight =FontWeight.Bold, fontSize = 40.sp)
                    ) {
                        append("$SalahName at $ArbitraryTime")
                    }
                }
            )

        }
    }
}

@Composable
fun FindLocation(athanViewModel: AthanViewModel) {
    // TODO: code is messy as of now, def needs improvement if possible....
    // Altidude seems borked on my device, no idea why. Need to investigate, zero altidude calculations are used on islamicFinder, so worst case we have same accuracy.

    val location : Triple<Double,Double,Double>? by athanViewModel.currentLocation.observeAsState()
    val (latidude, longitude, altitude) = location ?: Triple(0.0,0.0,0.0)
    val date = Date().time
    val tz = TimeZone.getDefault().getOffset(date) / (3600.0 * 1000.0)

    Log.i("time", tz.toString())
    Log.i("Salat Times: ",
        SalatTimes(location = Location("TBD", latidude, longitude, altitude, tz), calculationMethod = CalculationMethod.ISLAMIC_SOCIETY_OF_NORTH_AMERICA).salatDateTimes.toString()
    )
    Text("$latidude $longitude $altitude")
}



@OptIn(ExperimentalPagerApi::class)
@Composable
fun AthanCardRow(athanViewModel: AthanViewModel) {
    val athans = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
    val locationAthans : HashMap<String,LocalDateTime>? by athanViewModel.localAthanTimes.observeAsState()
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val pagerState = rememberPagerState(pageCount = 5)

    HorizontalPager(state = pagerState) { page ->
        // IDE GENERATED CODE -> Might need to get optimized / double checked.
        locationAthans?.get(athans[page])?.let { CardDemo(ArbitraryTime = it.format(formatter), SalahName=athans[page]) }
    }

}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    SalahappTheme {
//        AthanCardRow()
//    }
//}