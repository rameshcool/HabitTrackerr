package ramesh.developer.habittrackerr.domain.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import ramesh.developer.habittrackerr.domain.model.DaysOfWeek

class StreakCalculatorTest {

    private fun date(s: String): ZonedDateTime =
        LocalDate.parse(s).atStartOfDay(ZoneId.systemDefault())

    private val allDays = DaysOfWeek(
        monday = true, tuesday = true, wednesday = true, thursday = true,
        friday = true, saturday = true, sunday = true
    )
    private val weekdays = DaysOfWeek(
        monday = true, tuesday = true, wednesday = true, thursday = true, friday = true
    )
    private val monOnly = DaysOfWeek(monday = true)
    private val satSun = DaysOfWeek(saturday = true, sunday = true)

    // ── currentStreak ──────────────────────────────────────────────────────────

    @Test
    fun `currentStreak is 0 when no completions`() {
        assertEquals(0, StreakCalculator.currentStreak(
            completions = emptySet(),
            daysOfWeek = allDays,
            createdDate = date("2025-01-01"),
            today = date("2025-01-05")
        ))
    }

    @Test
    fun `currentStreak counts consecutive completed days up to today`() {
        val completions = setOf(
            date("2025-04-28"), date("2025-04-29"), date("2025-04-30"),
            date("2025-05-01"), date("2025-05-02")
        )
        assertEquals(5, StreakCalculator.currentStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-04-01"),
            today = date("2025-05-02")
        ))
    }

    @Test
    fun `currentStreak is 0 when today and yesterday are both missed`() {
        val completions = setOf(date("2025-04-28"))
        assertEquals(0, StreakCalculator.currentStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-04-01"),
            today = date("2025-05-02")
        ))
    }

    @Test
    fun `currentStreak continues from yesterday when today is scheduled but not done yet`() {
        val completions = setOf(
            date("2025-04-28"), date("2025-04-29"), date("2025-04-30")
        )
        assertEquals(3, StreakCalculator.currentStreak(
            completions = completions,
            daysOfWeek = weekdays,
            createdDate = date("2025-04-28"),
            today = date("2025-05-01")
        ))
    }

    @Test
    fun `currentStreak ignores non-scheduled weekend days for weekday habit`() {
        val completions = setOf(
            date("2025-04-28"), date("2025-04-29"), date("2025-04-30"),
            date("2025-05-01"), date("2025-05-02")
        )
        // today is Sunday May 4 — weekend, not a weekday scheduled day
        assertEquals(5, StreakCalculator.currentStreak(
            completions = completions,
            daysOfWeek = weekdays,
            createdDate = date("2025-04-01"),
            today = date("2025-05-04")
        ))
    }

    @Test
    fun `currentStreak does not count days before createdDate`() {
        val completions = setOf(
            date("2025-04-28"), date("2025-04-29"), date("2025-04-30")
        )
        // habit created on Apr 30 — Apr 28 and 29 must not count
        assertEquals(1, StreakCalculator.currentStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-04-30"),
            today = date("2025-04-30")
        ))
    }

    @Test
    fun `currentStreak is 0 for brand new habit with no completions`() {
        assertEquals(0, StreakCalculator.currentStreak(
            completions = emptySet(),
            daysOfWeek = allDays,
            createdDate = date("2025-05-01"),
            today = date("2025-05-01")
        ))
    }

    @Test
    fun `currentStreak single completion yesterday is 1`() {
        // today = Saturday 2025-05-03 (scheduled all days); completed only yesterday (Friday)
        assertEquals(1, StreakCalculator.currentStreak(
            completions = setOf(date("2025-05-02")),
            daysOfWeek = allDays,
            createdDate = date("2025-05-01"),
            today = date("2025-05-03")
        ))
    }

    @Test
    fun `currentStreak single completion today is 1`() {
        assertEquals(1, StreakCalculator.currentStreak(
            completions = setOf(date("2025-05-03")),
            daysOfWeek = allDays,
            createdDate = date("2025-05-01"),
            today = date("2025-05-03")
        ))
    }

    @Test
    fun `currentStreak habit created today completed today is 1`() {
        assertEquals(1, StreakCalculator.currentStreak(
            completions = setOf(date("2025-05-03")),
            daysOfWeek = allDays,
            createdDate = date("2025-05-03"),
            today = date("2025-05-03")
        ))
    }

    @Test
    fun `currentStreak 2 consecutive days done but missed 3 days ago is 2`() {
        // today = Wednesday 2025-05-07, not done
        // Mon 05 and Tue 06 completed; Fri 02, Sat 03, Sun 04 missed
        val completions = setOf(date("2025-05-05"), date("2025-05-06"))
        assertEquals(2, StreakCalculator.currentStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-04-01"),
            today = date("2025-05-07")
        ))
    }

    @Test
    fun `currentStreak monday-only last Monday done today is Tuesday is 1`() {
        // today = Tuesday 2025-04-29; last Monday 2025-04-28 was completed
        // today is not a scheduled day → look back to yesterday (Mon) → completed → streak=1
        assertEquals(1, StreakCalculator.currentStreak(
            completions = setOf(date("2025-04-28")),
            daysOfWeek = monOnly,
            createdDate = date("2025-04-01"),
            today = date("2025-04-29")
        ))
    }

    @Test
    fun `currentStreak monday-only missed last Monday today is Wednesday is 0`() {
        // today = Wednesday 2025-04-30; last Mon 28 not completed
        assertEquals(0, StreakCalculator.currentStreak(
            completions = emptySet(),
            daysOfWeek = monOnly,
            createdDate = date("2025-04-01"),
            today = date("2025-04-30")
        ))
    }

    @Test
    fun `currentStreak completions only before createdDate produces 0`() {
        // completions exist Apr 28-29 but habit created Apr 30 with no Apr 30 completion
        val completions = setOf(date("2025-04-28"), date("2025-04-29"))
        assertEquals(0, StreakCalculator.currentStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-04-30"),
            today = date("2025-04-30")
        ))
    }

    @Test
    fun `currentStreak non-scheduled days between completed weekends do not break streak`() {
        // Sat-Sun habit; completed Sat Apr 26, Sun Apr 27, Sat May 3, Sun May 4
        // Mon-Fri (Apr 28-May 2) are non-scheduled and must not break the streak
        val completions = setOf(
            date("2025-04-26"), date("2025-04-27"),
            date("2025-05-03"), date("2025-05-04")
        )
        assertEquals(4, StreakCalculator.currentStreak(
            completions = completions,
            daysOfWeek = satSun,
            createdDate = date("2025-04-26"),
            today = date("2025-05-04")
        ))
    }

    @Test
    fun `currentStreak 30 consecutive days all-days habit is 30`() {
        val start = LocalDate.parse("2025-04-01")
        val completions = (0L until 30L)
            .map { start.plusDays(it).atStartOfDay(ZoneId.systemDefault()) }
            .toSet()
        assertEquals(30, StreakCalculator.currentStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-04-01"),
            today = date("2025-04-30")
        ))
    }

    // ── bestStreak ─────────────────────────────────────────────────────────────

    @Test
    fun `bestStreak is 0 when no completions`() {
        assertEquals(0, StreakCalculator.bestStreak(
            completions = emptySet(),
            daysOfWeek = allDays,
            createdDate = date("2025-01-01"),
            today = date("2025-01-05")
        ))
    }

    @Test
    fun `bestStreak returns longest run when there are gaps`() {
        val completions = setOf(
            date("2025-04-21"),
            date("2025-04-23"),
            date("2025-04-28"), date("2025-04-29")
        )
        assertEquals(2, StreakCalculator.bestStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-04-21"),
            today = date("2025-04-29")
        ))
    }

    @Test
    fun `bestStreak weekend habit is not broken by weekday non-scheduled days`() {
        val completions = setOf(
            date("2025-04-26"), date("2025-04-27"), // Sat+Sun week 1
            date("2025-05-03"), date("2025-05-04")  // Sat+Sun week 2
        )
        assertEquals(4, StreakCalculator.bestStreak(
            completions = completions,
            daysOfWeek = satSun,
            createdDate = date("2025-04-26"),
            today = date("2025-05-04")
        ))
    }

    @Test
    fun `bestStreak today not completed does not reset current run`() {
        val completions = setOf(
            date("2025-04-28"), date("2025-04-29"), date("2025-04-30"),
            date("2025-05-01"), date("2025-05-02")
        )
        // today = May 3, weekday habit, today not completed yet
        assertEquals(5, StreakCalculator.bestStreak(
            completions = completions,
            daysOfWeek = weekdays,
            createdDate = date("2025-04-28"),
            today = date("2025-05-03")
        ))
    }

    @Test
    fun `bestStreak monday-only habit across four consecutive weeks`() {
        val completions = setOf(
            date("2025-04-07"), date("2025-04-14"),
            date("2025-04-21"), date("2025-04-28")
        )
        assertEquals(4, StreakCalculator.bestStreak(
            completions = completions,
            daysOfWeek = monOnly,
            createdDate = date("2025-04-07"),
            today = date("2025-04-28")
        ))
    }

    @Test
    fun `bestStreak single completion is 1`() {
        assertEquals(1, StreakCalculator.bestStreak(
            completions = setOf(date("2025-04-15")),
            daysOfWeek = allDays,
            createdDate = date("2025-04-15"),
            today = date("2025-04-15")
        ))
    }

    @Test
    fun `bestStreak two separate runs returns the longer one`() {
        // run of 2 (Apr 21-22), gap Apr 23-27, run of 5 (Apr 28-May 2)
        val completions = setOf(
            date("2025-04-21"), date("2025-04-22"),
            date("2025-04-28"), date("2025-04-29"), date("2025-04-30"),
            date("2025-05-01"), date("2025-05-02")
        )
        assertEquals(5, StreakCalculator.bestStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-04-21"),
            today = date("2025-05-02")
        ))
    }

    @Test
    fun `bestStreak all days completed consecutively equals total count`() {
        val start = LocalDate.parse("2025-04-01")
        val completions = (0L until 10L)
            .map { start.plusDays(it).atStartOfDay(ZoneId.systemDefault()) }
            .toSet()
        assertEquals(10, StreakCalculator.bestStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-04-01"),
            today = date("2025-04-10")
        ))
    }

    @Test
    fun `bestStreak missing only today does not reduce best streak from history`() {
        // completed Mon-Fri last week; today is Monday of this week, not done yet
        val completions = setOf(
            date("2025-04-28"), date("2025-04-29"), date("2025-04-30"),
            date("2025-05-01"), date("2025-05-02")
        )
        assertEquals(5, StreakCalculator.bestStreak(
            completions = completions,
            daysOfWeek = weekdays,
            createdDate = date("2025-04-28"),
            today = date("2025-05-05")  // Monday, not done
        ))
    }

    @Test
    fun `bestStreak monday-only missed one Monday mid-history then 5 more gives best of 5`() {
        // Apr 7 done, Apr 14 missed, Apr 21 through May 19 = 5 consecutive Mondays
        val completions = setOf(
            date("2025-04-07"),
            // Apr 14 deliberately absent
            date("2025-04-21"), date("2025-04-28"),
            date("2025-05-05"), date("2025-05-12"), date("2025-05-19")
        )
        assertEquals(5, StreakCalculator.bestStreak(
            completions = completions,
            daysOfWeek = monOnly,
            createdDate = date("2025-04-07"),
            today = date("2025-05-19")
        ))
    }

    @Test
    fun `bestStreak equals currentStreak when there are no breaks in history`() {
        val completions = setOf(
            date("2025-05-01"), date("2025-05-02"), date("2025-05-03")
        )
        val current = StreakCalculator.currentStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-05-01"),
            today = date("2025-05-03")
        )
        val best = StreakCalculator.bestStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-05-01"),
            today = date("2025-05-03")
        )
        assertEquals(current, best)
    }

    @Test
    fun `bestStreak completions only before createdDate gives 0`() {
        // completions exist Apr 28-29 but habit created Apr 30 with no completions after
        val completions = setOf(date("2025-04-28"), date("2025-04-29"))
        assertEquals(0, StreakCalculator.bestStreak(
            completions = completions,
            daysOfWeek = allDays,
            createdDate = date("2025-04-30"),
            today = date("2025-04-30")
        ))
    }
}
