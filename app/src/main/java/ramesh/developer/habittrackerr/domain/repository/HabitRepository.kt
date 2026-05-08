package ramesh.developer.habittrackerr.domain.repository

import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow
import ramesh.developer.habittrackerr.domain.model.ActivityDay
import ramesh.developer.habittrackerr.domain.model.Habit
import ramesh.developer.habittrackerr.domain.model.HabitWithCompletion
import ramesh.developer.habittrackerr.domain.model.HabitWithStreaks
import ramesh.developer.habittrackerr.domain.model.StatsOverview

interface HabitRepository {

    // Today screen
    fun getHabitsWithCompletionForDate(date: ZonedDateTime): Flow<List<HabitWithCompletion>>

    // Create / Edit screen
    suspend fun getHabitById(id: Long): Habit?
    suspend fun insertHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habitId: Long)

    // Today screen — checkbox tap
    suspend fun toggleCompletion(habitId: Long, date: ZonedDateTime)

    // Stats screen
    fun getHabitsWithStreaks(): Flow<List<HabitWithStreaks>>
    fun getActivityHeatmap(startDate: ZonedDateTime, endDate: ZonedDateTime): Flow<List<ActivityDay>>
    fun getStatsOverview(weekStart: ZonedDateTime, today: ZonedDateTime): Flow<StatsOverview>
}
