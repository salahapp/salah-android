package com.example.salah_app
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AthanViewModel: ViewModel() {

    private var _currentLocation = MutableLiveData(Pair(0.0, 0.0))
    val currentLocation: LiveData<Pair<Double,Double>> = _currentLocation

    fun refresh_location(location : Pair<Double,Double>) {
        _currentLocation.value = location

    }

}