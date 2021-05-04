package com.example.salah_app
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AthanViewModel: ViewModel() {

    private var _currentLocation = MutableLiveData(Triple(0.0, 0.0,0.0))
    val currentLocation: LiveData<Triple<Double,Double,Double>> = _currentLocation

    fun refresh_location(location : Triple<Double,Double,Double>) {
        _currentLocation.value = location

    }

}