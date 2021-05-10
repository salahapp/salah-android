package com.example.salah_app

import Location
import SalatTimes
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month
import java.util.*

class AthanTimesTest {
    @Test
    fun BeforeFajrShouldReturnGiveFirstIndex() {
        val athanTimes = HashMap(
            SalatTimes(
                location = Location("TBD", 45.33, -75.33, 0.0, -4.0), calculationMethod = CalculationMethod.ISLAMIC_SOCIETY_OF_NORTH_AMERICA,
                dateTime = LocalDateTime.of(2021, Month.MAY, 8, 1, 30)
            ).salatDateTimes
        )
        val timeBeforeFajr = LocalDateTime.of(2021, Month.MAY, 8, 1, 31)
        val currentAthanIndex = getMostRecentAthan(athanTimes, timeBeforeFajr)
        Assert.assertEquals(0, currentAthanIndex)
    }

    @Test
    fun AfterFajrShouldReturnSecondIndex() {
        val athanTimes = HashMap(
            SalatTimes(
                location = Location("TBD", 45.33, -75.33, 0.0, -4.0), calculationMethod = CalculationMethod.ISLAMIC_SOCIETY_OF_NORTH_AMERICA,
                dateTime = LocalDateTime.of(2021, Month.MAY, 8, 7, 32)
            ).salatDateTimes
        )
        val timeAfterFajr = LocalDateTime.of(2021, Month.MAY, 8, 7, 32)
        val currentAthanIndex = getMostRecentAthan(athanTimes, timeAfterFajr)
        Assert.assertEquals(1, currentAthanIndex)
    }

    @Test
    fun AfterDhuhrShouldReturnThirdIndex() {
        val athanTimes = HashMap(
            SalatTimes(
                location = Location("TBD", 45.33, -75.33, 0.0, -4.0), calculationMethod = CalculationMethod.ISLAMIC_SOCIETY_OF_NORTH_AMERICA,
                dateTime = LocalDateTime.of(2021, Month.MAY, 8, 7, 32)
            ).salatDateTimes
        )
        val timeAfterFajr = LocalDateTime.of(2021, Month.MAY, 8, 13, 32)
        val currentAthanIndex = getMostRecentAthan(athanTimes, timeAfterFajr)
        Assert.assertEquals(2, currentAthanIndex)
    }

    @Test
    fun AfterAsrShouldReturnThirdIndex() {
        val athanTimes = HashMap(
            SalatTimes(
                location = Location("TBD", 45.33, -75.33, 0.0, -4.0), calculationMethod = CalculationMethod.ISLAMIC_SOCIETY_OF_NORTH_AMERICA,
                dateTime = LocalDateTime.of(2021, Month.MAY, 8, 7, 32)
            ).salatDateTimes
        )

        val timeAfterFajr = LocalDateTime.of(2021, Month.MAY, 8, 18, 32)
        val currentAthanIndex = getMostRecentAthan(athanTimes, timeAfterFajr)
        Assert.assertEquals(3, currentAthanIndex)
    }

    @Test
    fun AfterMaghribShouldReturnFourthIndex() {
        val athanTimes = HashMap(
            SalatTimes(
                location = Location("TBD", 45.33, -75.33, 0.0, -4.0), calculationMethod = CalculationMethod.ISLAMIC_SOCIETY_OF_NORTH_AMERICA,
                dateTime = LocalDateTime.of(2021, Month.MAY, 8, 7, 32)
            ).salatDateTimes
        )
        val timeAfterFajr = LocalDateTime.of(2021, Month.MAY, 8, 20, 35)
        val currentAthanIndex = getMostRecentAthan(athanTimes, timeAfterFajr)
        Assert.assertEquals(4, currentAthanIndex)
    }

    @Test
    fun AfterIshaShouldReturnFinalIndex() {
        val athanTimes = HashMap(
            SalatTimes(
                location = Location("TBD", 45.33, -75.33, 0.0, -4.0), calculationMethod = CalculationMethod.ISLAMIC_SOCIETY_OF_NORTH_AMERICA,
                dateTime = LocalDateTime.of(2021, Month.MAY, 8, 7, 32)
            ).salatDateTimes
        )
        val timeAfterFajr = LocalDateTime.of(2021, Month.MAY, 8, 23, 32)
        val currentAthanIndex = getMostRecentAthan(athanTimes, timeAfterFajr)
        Assert.assertEquals(4, currentAthanIndex)
    }
}
