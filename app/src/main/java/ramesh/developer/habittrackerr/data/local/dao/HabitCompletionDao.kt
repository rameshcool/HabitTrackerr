package ramesh.developer.habittrackerr.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ramesh.developer.habittrackerr.data.local.entity.HabitCompletionEntity

@Dao
interface HabitCompletionDao {

    // Reactive — all completions for a specific date (Today screen)
    @Query("SELECT * FROM habit_completions WHERE completed_date = :epochDay")
    fun getCompletionsForDate(epochDay: Long): Flow<List<HabitCompletionEntity>>

    // Reactive — all completions ever (Stats screen: streaks + overview)
    @Query("SELECT * FROM habit_completions ORDER BY habit_id, completed_date ASC")
    fun getAllCompletions(): Flow<List<HabitCompletionEntity>>

    // Reactive — completions within a date range (heatmap)
    @Query("SELECT * FROM habit_completions WHERE completed_date >= :startEpochDay AND completed_date <= :endEpochDay")
    fun getCompletionsByDateRange(startEpochDay: Long, endEpochDay: Long): Flow<List<HabitCompletionEntity>>

    // Suspend — used by toggleCompletion
    @Query("SELECT * FROM habit_completions WHERE habit_id = :habitId AND completed_date = :epochDay LIMIT 1")
    suspend fun getCompletionForHabitOnDate(habitId: Long, epochDay: Long): HabitCompletionEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompletion(completion: HabitCompletionEntity)

    @Query("DELETE FROM habit_completions WHERE habit_id = :habitId AND completed_date = :epochDay")
    suspend fun deleteCompletion(habitId: Long, epochDay: Long)
}
