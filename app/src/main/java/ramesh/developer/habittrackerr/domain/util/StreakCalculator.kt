package ramesh.developer.habittrackerr.domain.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import ramesh.developer.habittrackerr.domain.model.DaysOfWeek

object StreakCalculator {

    private fun ZonedDateTime.toSystemLocalDate(): LocalDate =
        withZoneSameInstant(ZoneId.systemDefault()).toLocalDate()

    /**
     * Consecutive scheduled days completed, walking backward from today.
     * If today is scheduled but not yet completed, counting starts from yesterday.
     * Stops at createdDate — days before creation never count as misses.
     */
    fun currentStreak(
        completions: Set<ZonedDateTime>,
        daysOfWeek: DaysOfWeek,
        createdDate: ZonedDateTime,
        today: ZonedDateTime
    ): Int {
        val completionDates = completions.map { it.toSystemLocalDate() }.toSet()
        val createdLocalDate = createdDate.toSystemLocalDate()
        var date = today.toSystemLocalDate()

        if (daysOfWeek.isScheduledOn(date.dayOfWeek) && date !in completionDates) {
            date = date.minusDays(1)
        }

        var streak = 0
        while (!date.isBefore(createdLocalDate)) {
            if (daysOfWeek.isScheduledOn(date.dayOfWeek)) {
                if (date in completionDates) {
                    streak++
                } else {
                    break
                }
            }
            date = date.minusDays(1)
        }
        return streak
    }

    /**
     * Longest consecutive completed-scheduled-day run ever recorded.
     * Walks forward from createdDate. Today is not penalised if not yet completed.
     */
    fun bestStreak(
        completions: Set<ZonedDateTime>,
        daysOfWeek: DaysOfWeek,
        createdDate: ZonedDateTime,
        today: ZonedDateTime
    ): Int {
        val completionDates = completions.map { it.toSystemLocalDate() }.toSet()
        val todayLocalDate = today.toSystemLocalDate()
        var best = 0
        var current = 0
        var date = createdDate.toSystemLocalDate()

        while (!date.isAfter(todayLocalDate)) {
            if (daysOfWeek.isScheduledOn(date.dayOfWeek)) {
                if (date in completionDates) {
                    current++
                    if (current > best) best = current
                } else if (date.isBefore(todayLocalDate)) {
                    // Only a missed day if it's strictly in the past
                    current = 0
                }
            }
            date = date.plusDays(1)
        }
        return best
    }
}
