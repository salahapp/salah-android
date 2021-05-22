package com.example.salah_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime
import java.util.Timer;
import java.util.TimerTask;

// Inspired from: https://github.com/googlecodelabs/android-lifecycles/blob/master/app/src/main/java/com/example/android/lifecycles/step3_solution/ChronoActivity3.java
class LiveDataTimerViewModel : ViewModel() {
    private var _currentTime = MutableLiveData<LocalDateTime>()
    private val timer: Timer
    val currentTime: LiveData<LocalDateTime>
        get() = _currentTime

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
    // TODO: REVERT THIS BACK TO ONE SECOND 1000L
    companion object {
        private const val ONE_SECOND = 5000L
    }

    init {
        _currentTime.value = LocalDateTime.now()
        timer = Timer()

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val newValue = LocalDateTime.now()
                _currentTime.postValue(newValue)
            }
        }, ONE_SECOND, ONE_SECOND)
    }
}