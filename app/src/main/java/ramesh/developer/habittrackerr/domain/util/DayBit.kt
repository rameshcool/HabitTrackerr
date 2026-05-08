package ramesh.developer.habittrackerr.domain.util

import java.time.DayOfWeek

object DayBit {
    const val MON = 1 shl 0
    const val TUE = 1 shl 1
    const val WED = 1 shl 2
    const val THU = 1 shl 3
    const val FRI = 1 shl 4
    const val SAT = 1 shl 5
    const val SUN = 1 shl 6

    fun forDayOfWeek(d: DayOfWeek): Int = when (d) {
        DayOfWeek.MONDAY    -> MON
        DayOfWeek.TUESDAY   -> TUE
        DayOfWeek.WEDNESDAY -> WED
        DayOfWeek.THURSDAY  -> THU
        DayOfWeek.FRIDAY    -> FRI
        DayOfWeek.SATURDAY  -> SAT
        DayOfWeek.SUNDAY    -> SUN
    }
}

fun Int.isScheduledOn(day: DayOfWeek): Boolean = (this and DayBit.forDayOfWeek(day)) != 0
