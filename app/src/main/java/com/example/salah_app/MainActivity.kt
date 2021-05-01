package com.example.salah_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.salah_app.ui.theme.SalahappTheme
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import compose.icons.WeatherIcons

import compose.icons.weathericons.Sunrise


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SalahappTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun CardDemo(ArbitraryTime: String) {
    Card(
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(25.dp).fillMaxWidth()
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
                        append("Isha at $ArbitraryTime PM")
                    }
                }
            )




        }
    }
}

@Composable
fun Greeting(name: String) {
    LazyRowDemo()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LazyRowDemo() {
    val list = ((1..6).map { "${it*2}:${it}0" })
    val pagerState = rememberPagerState(pageCount = 5)

    HorizontalPager(state = pagerState) { page ->
        // Our page content
        CardDemo(ArbitraryTime = list[page].toString())

    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SalahappTheme {
        Greeting("Android")
    }
}