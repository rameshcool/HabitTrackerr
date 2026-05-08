package ramesh.developer.habittrackerr.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ramesh.developer.habittrackerr.data.local.dao.HabitCompletionDao
import ramesh.developer.habittrackerr.data.local.dao.HabitDao
import ramesh.developer.habittrackerr.data.local.entity.HabitCompletionEntity
import ramesh.developer.habittrackerr.data.local.entity.HabitEntity

@Database(
    entities = [HabitEntity::class, HabitCompletionEntity::class],
    version = 2,
    exportSchema = true
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao
}
