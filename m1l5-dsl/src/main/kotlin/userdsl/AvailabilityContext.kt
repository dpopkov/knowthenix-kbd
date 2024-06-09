package io.dpopkov.knowthenixkbd.m1l5.userdsl

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

class AvailabilityContext {
    private val availabilities: MutableList<LocalDateTime> = mutableListOf()

    fun build(): List<LocalDateTime> = availabilities.toList()

    fun sun(time: String) = dayTimeOfWeek(DayOfWeek.SUNDAY, time)
    fun mon(time: String) = dayTimeOfWeek(DayOfWeek.MONDAY, time)
    fun tue(time: String) = dayTimeOfWeek(DayOfWeek.TUESDAY, time)
    fun wed(time: String) = dayTimeOfWeek(DayOfWeek.WEDNESDAY, time)
    fun thu(time: String) = dayTimeOfWeek(DayOfWeek.THURSDAY, time)
    fun fri(time: String) = dayTimeOfWeek(DayOfWeek.FRIDAY, time)
    fun sat(time: String) = dayTimeOfWeek(DayOfWeek.SATURDAY, time)

    fun tomorrow(time: String) = addDayTime(
        LocalDate.now().plusDays(1),
        LocalTime.parse(time)
    )

    private fun dayTimeOfWeek(day: DayOfWeek, time: String) = addDayTime(
        LocalDate.now().with(TemporalAdjusters.next(day)),
        LocalTime.parse(time)
    )

    private fun addDayTime(day: LocalDate, time: LocalTime) {
        add(LocalDateTime.of(day, time))
    }

    private fun add(dateTime: LocalDateTime) {
        availabilities.add(dateTime)
    }
}
