package com.example.salah_app

import Location
import SalatTimes
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import compose.icons.TablerIcons
import compose.icons.tablericons.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Composable
fun CardDemo(SalahCard: AthanCard, ArbitraryTime: String) {
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
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

                // TODO: Figure out how to enhance color, sizing, and content description!
                Icon(
                    imageVector = SalahCard.cardIcon,
                    contentDescription = null,
                    tint = SalahCard.iconColor,
                    modifier = Modifier
                        .then(Modifier.size(80.dp))

                )
            }

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Light, fontSize = 20.sp, color = Color(0xFF607d8b))
                    ) {
                        append("Upcoming Prayer")
                    }
                }
            )

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 34.sp)
                    ) {
                        append("${SalahCard.athanName} at $ArbitraryTime")
                    }
                }
            )
        }
    }
}

@Composable
fun FindLocation(location: Triple<Double, Double, Double>?) {
    // TODO: code is messy as of now, def needs improvement if possible....
    // Altidude seems borked on my device, no idea why. Need to investigate, zero altidude calculations are used on islamicFinder, so worst case we have same accuracy.

    val (latitude, longitude, altitude) = location ?: Triple(0.0, 0.0, 0.0)
    val date = Date().time
    val tz = TimeZone.getDefault().getOffset(date) / (3600.0 * 1000.0)

    Log.i("time", tz.toString())
    Log.i(
        "Salat Times: ",
        SalatTimes(location = Location("TBD", latitude, longitude, altitude, tz), calculationMethod = CalculationMethod.ISLAMIC_SOCIETY_OF_NORTH_AMERICA).salatDateTimes.toString()
    )
    Text("$latitude $longitude $altitude")
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AthanCardRow(locationAthans: HashMap<String, LocalDateTime>?) {
    val athan = listOf(AthanCard.FAJR, AthanCard.DHUHR,AthanCard.ASR, AthanCard.MAGHRIB, AthanCard.ISHA)
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val pagerState = rememberPagerState(pageCount = 5)


    HorizontalPager(state = pagerState) { page ->
        // IDE GENERATED CODE -> Might need to get optimized / double checked.
        locationAthans?.get(athan[page].athanName)?.let { CardDemo(ArbitraryTime = it.format(formatter), SalahCard = athan[page]) }
    }
}

@Composable
fun MainScreen(athanViewModel: AthanViewModel){
    val location: Triple<Double, Double, Double>? by athanViewModel.currentLocation.observeAsState()
    val locationAthans: HashMap<String, LocalDateTime>? by athanViewModel.localAthanTimes.observeAsState()

    Column() {
        FindLocation(location)
        AthanCardRow(locationAthans)
    }
}


enum class AthanCard(val athanName: String,
                             val cardIcon: ImageVector,
                             val iconColor: Color) {

    FAJR("Fajr", TablerIcons.Sunset,Color(0xFFffeb3b)),
    DHUHR("Dhuhr", TablerIcons.Sun,Color(0xFFff6f00)),
    ASR("Asr", TablerIcons.Flare,Color(0xFFe64a19)),
    MAGHRIB("Maghrib",TablerIcons.Sunshine,Color(0xFF1565c0)),
    ISHA("Isha", TablerIcons.MoonStars,Color(0xFF303f9f))

}
