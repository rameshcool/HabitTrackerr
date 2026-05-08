package ramesh.developer.habittrackerr.data.repository

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ramesh.developer.habittrackerr.data.local.dao.HabitCompletionDao
import ramesh.developer.habittrackerr.data.local.dao.HabitDao
import ramesh.developer.habittrackerr.data.local.entity.HabitCompletionEntity
import ramesh.developer.habittrackerr.data.local.entity.toDaysOfWeek
import ramesh.developer.habittrackerr.data.local.entity.toDomain
import ramesh.developer.habittrackerr.data.local.entity.toEntity
import ramesh.developer.habittrackerr.domain.model.ActivityDay
import ramesh.developer.habittrackerr.domain.model.Habit
import ramesh.developer.habittrackerr.domain.model.HabitWithCompletion
import ramesh.developer.habittrackerr.domain.model.HabitWithStreaks
import ramesh.developer.habittrackerr.domain.model.StatsOverview
import ramesh.developer.habittrackerr.domain.repository.HabitRepository
import ramesh.developer.habittrackerr.domain.util.StreakCalculator

class HabitRepositoryImpl(
    private val habitDao: HabitDao,
    private val completionDao: HabitCompletionDao
) : HabitRepository {

    override fun getHabitsWithCompletionForDate(date: ZonedDateTime): Flow<List<HabitWithCompletion>> {
        val epochDay = date.toLocalDate().toEpochDay()
        return combine(
            habitDao.getAllHabits(),
            completionDao.getCompletionsForDate(epochDay)
        ) { habits, completions ->
            val completedIds = completions.map { it.habitId }.toSet()
            habits
                .filter { habit ->
                    habit.createdDate <= epochDay && habit.toDaysOfWeek().isScheduledOn(date.dayOfWeek)
                }
                .map { habit ->
                    HabitWithCompletion(
                        habit = habit.toDomain(),
                        isCompletedToday = habit.id in completedIds
                    )
                }
        }
    }

    override suspend fun getHabitById(id: Long): Habit? =
        habitDao.getHabitById(id)?.toDomain()

    override suspend fun insertHabit(habit: Habit): Long =
        habitDao.insertHabit(habit.toEntity())

    override suspend fun updateHabit(habit: Habit) =
        habitDao.updateHabit(habit.toEntity())

    override suspend fun deleteHabit(habitId: Long) =
        habitDao.deleteHabitById(habitId)

    override suspend fun toggleCompletion(habitId: Long, date: ZonedDateTime) {
        val epochDay = date.toLocalDate().toEpochDay()
        val existing = completionDao.getCompletionForHabitOnDate(habitId, epochDay)
        if (existing != null) {
            completionDao.deleteCompletion(habitId, epochDay)
        } else {
            completionDao.insertCompletion(HabitCompletionEntity(habitId = habitId, completedDate = epochDay))
        }
    }

    override fun getHabitsWithStreaks(): Flow<List<HabitWithStreaks>> =
        combine(
            habitDao.getAllHabits(),
            completionDao.getAllCompletions()
        ) { habits, allCompletions ->
            val today = ZonedDateTime.now(ZoneId.systemDefault())
            val completionsByHabit = allCompletions.groupBy { it.habitId }
            habits.map { habitEntity ->
                val habit = habitEntity.toDomain()
                val completionDates = (completionsByHabit[habit.id] ?: emptyList())
                    .map { LocalDate.ofEpochDay(it.completedDate).atStartOfDay(ZoneId.systemDefault()) }
                    .toSet()
                HabitWithStreaks(
                    habit = habit,
                    currentStreak = StreakCalculator.currentStreak(
                        completionDates, habit.daysOfWeek, habit.createdDate, today
                    ),
                    bestStreak = StreakCalculator.bestStreak(
                        completionDates, habit.daysOfWeek, habit.createdDate, today
                    )
                )
            }
        }

    override fun getActivityHeatmap(startDate: ZonedDateTime, endDate: ZonedDateTime): Flow<List<ActivityDay>> {
        val startEpochDay = startDate.toLocalDate().toEpochDay()
        val endEpochDay = endDate.toLocalDate().toEpochDay()
        return combine(
            habitDao.getAllHabits(),
            completionDao.getCompletionsByDateRange(startEpochDay, endEpochDay)
        ) { habits, completions ->
            val completionsByDate = completions.groupBy { it.completedDate }
            val result = mutableListOf<ActivityDay>()
            var date = startDate.toLocalDate()
            val endLocal = endDate.toLocalDate()
            while (!date.isAfter(endLocal)) {
                val epochDay = date.toEpochDay()
                val scheduledCount = habits.count { habit ->
                    habit.createdDate <= epochDay && habit.toDaysOfWeek().isScheduledOn(date.dayOfWeek)
                }
                val completedCount = completionsByDate[epochDay]?.size ?: 0
                val ratio = if (scheduledCount > 0) completedCount.toFloat() / scheduledCount else 0f
                result.add(ActivityDay(
                    date = date.atStartOfDay(ZoneId.systemDefault()),
                    completionRatio = ratio.coerceIn(0f, 1f)
                ))
                date = date.plusDays(1)
            }
            result
        }
    }

    override fun getStatsOverview(weekStart: ZonedDateTime, today: ZonedDateTime): Flow<StatsOverview> {
        val todayEpochDay = today.toLocalDate().toEpochDay()
        val weekStartEpochDay = weekStart.toLocalDate().toEpochDay()
        return combine(
            habitDao.getAllHabits(),
            completionDao.getAllCompletions()
        ) { habits, allCompletions ->
            val weekCompletions = allCompletions.filter {
                it.completedDate in weekStartEpochDay..todayEpochDay
            }

            var scheduledThisWeek = 0
            var completedThisWeek = 0
            var currentDay = weekStart.toLocalDate()
            val todayLocal = today.toLocalDate()
            while (!currentDay.isAfter(todayLocal)) {
                val dayEpochDay = currentDay.toEpochDay()
                habits.forEach { habit ->
                    if (habit.createdDate <= dayEpochDay && habit.toDaysOfWeek().isScheduledOn(currentDay.dayOfWeek)) {
                        scheduledThisWeek++
                        if (weekCompletions.any { it.habitId == habit.id && it.completedDate == dayEpochDay }) {
                            completedThisWeek++
                        }
                    }
                }
                currentDay = currentDay.plusDays(1)
            }

            val thisWeekPercent = if (scheduledThisWeek > 0) {
                (completedThisWeek * 100) / scheduledThisWeek
            } else 0

            val completionsByHabit = allCompletions.groupBy { it.habitId }
            val nowZdt = ZonedDateTime.now(ZoneId.systemDefault())
            val bestStreak = habits.maxOfOrNull { habitEntity ->
                val habit = habitEntity.toDomain()
                val completionDates = (completionsByHabit[habitEntity.id] ?: emptyList())
                    .map { LocalDate.ofEpochDay(it.completedDate).atStartOfDay(ZoneId.systemDefault()) }
                    .toSet()
                StreakCalculator.bestStreak(completionDates, habit.daysOfWeek, habit.createdDate, nowZdt)
            } ?: 0

            StatsOverview(
                thisWeekPercent = thisWeekPercent,
                bestStreak = bestStreak,
                activeCount = habits.size
            )
        }
    }
}
