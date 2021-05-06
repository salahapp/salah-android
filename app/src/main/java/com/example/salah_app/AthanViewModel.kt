package com.example.salah_app
import Location
import SalatTimes
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

class AthanViewModel: ViewModel() {

    private var _currentLocation = MutableLiveData(Triple(0.0, 0.0,0.0))
    private var _localAthanTimes = MutableLiveData(HashMap<String, LocalDateTime>())

    val currentLocation: LiveData<Triple<Double,Double,Double>> = _currentLocation
    val localAthanTimes: LiveData<HashMap<String,LocalDateTime>> = _localAthanTimes

    fun refresh_location(location : Triple<Double,Double,Double>) {
        _currentLocation.value = location
        calculate_athan_local_times()
    }
    fun calculate_athan_local_times(){

        val (latidude, longitude, altitude) = currentLocation.value ?: Triple(0.0,0.0,0.0)
        val date = Date().time
        val tz = TimeZone.getDefault().getOffset(date) / (3600.0 * 1000.0)


        // TODO: Get actual city name if possible...
        _localAthanTimes.value = HashMap(SalatTimes(location = Location("TBD", latidude, longitude, altitude, tz), calculationMethod = CalculationMethod.ISLAMIC_SOCIETY_OF_NORTH_AMERICA).salatDateTimes)


        Log.i("time", tz.toString())
        Log.i("Salat Times: ",
            _localAthanTimes.value.toString()
        )

    }

}