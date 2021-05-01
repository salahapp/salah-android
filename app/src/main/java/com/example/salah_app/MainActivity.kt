package com.example.salah_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


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
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Light,fontSize = 30.sp, color = Color(0xFF607d8b) )
                    ) {
                        append("Upcoming Prayer")
                    }
                }
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight =FontWeight.Bold, fontSize = 60.sp)
                    ) {
                        append("Isha at: $ArbitraryTime")
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
    val list = ((65..65+72).map { it.toChar() })
    val pagerState = rememberPagerState(pageCount = 10)

    HorizontalPager(state = pagerState) { page ->
        // Our page content
        CardDemo(ArbitraryTime = list[page].toString())

    }

//    LazyRow(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
//        items(items = list, itemContent = { item ->
//            Log.d("COMPOSE", "This get rendered $item")
//            CardDemo(ArbitraryTime = item.toString())
//
//        })
//    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SalahappTheme {
        Greeting("Android")
    }
}