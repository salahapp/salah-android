package com.example.salah_app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime


class CardsDataViewModel : ViewModel() {
    private var _timeOfLastSwipe = MutableLiveData<LocalDateTime>()

    val timeOfLastSwipe: LiveData<LocalDateTime> = _timeOfLastSwipe

    fun onSwipe(currentSwipeTime: LocalDateTime) {
        _timeOfLastSwipe.value = currentSwipeTime
        Log.i("Wow", "Updating actually works! Current Time Now is: ${timeOfLastSwipe.value}")
    }


    init {
        _timeOfLastSwipe.value = LocalDateTime.MIN

    }

}