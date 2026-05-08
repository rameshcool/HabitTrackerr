package ramesh.developer.habittrackerr.domain.model

import java.time.DayOfWeek

data class DaysOfWeek(
    val monday: Boolean = false,
    val tuesday: Boolean = false,
    val wednesday: Boolean = false,
    val thursday: Boolean = false,
    val friday: Boolean = false,
    val saturday: Boolean = false,
    val sunday: Boolean = false
) {
    fun isScheduledOn(day: DayOfWeek): Boolean = when (day) {
        DayOfWeek.MONDAY    -> monday
        DayOfWeek.TUESDAY   -> tuesday
        DayOfWeek.WEDNESDAY -> wednesday
        DayOfWeek.THURSDAY  -> thursday
        DayOfWeek.FRIDAY    -> friday
        DayOfWeek.SATURDAY  -> saturday
        DayOfWeek.SUNDAY    -> sunday
    }
}
