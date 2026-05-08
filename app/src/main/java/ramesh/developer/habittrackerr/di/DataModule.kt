package ramesh.developer.habittrackerr.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ramesh.developer.habittrackerr.data.local.db.HabitDatabase
import ramesh.developer.habittrackerr.data.repository.HabitRepositoryImpl
import ramesh.developer.habittrackerr.domain.repository.HabitRepository
import java.time.DayOfWeek
import java.time.LocalDate

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            HabitDatabase::class.java,
            "habit_tracker.db"
        )
        .fallbackToDestructiveMigration(true)
        .addCallback(SeedDataCallback())
        .build()
    }
    single { get<HabitDatabase>().habitDao() }
    single { get<HabitDatabase>().habitCompletionDao() }
    single<HabitRepository> { HabitRepositoryImpl(get(), get()) }
}

private class SeedDataCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        val today = LocalDate.now()
        val e = today.toEpochDay()
        val createdDate = e - 49 // 7 weeks ago

        // 4 habits with varied schedules
        db.execSQL(
            "INSERT INTO habits (name,icon_name,monday,tuesday,wednesday,thursday,friday,saturday,sunday,created_date) " +
            "VALUES ('Morning Run','run',1,1,1,1,1,1,1,$createdDate)"
        )
        db.execSQL(
            "INSERT INTO habits (name,icon_name,monday,tuesday,wednesday,thursday,friday,saturday,sunday,created_date) " +
            "VALUES ('Read 30min','read',1,1,1,1,1,0,0,$createdDate)"
        )
        db.execSQL(
            "INSERT INTO habits (name,icon_name,monday,tuesday,wednesday,thursday,friday,saturday,sunday,created_date) " +
            "VALUES ('Meditate','meditate',1,1,1,1,1,1,1,$createdDate)"
        )
        db.execSQL(
            "INSERT INTO habits (name,icon_name,monday,tuesday,wednesday,thursday,friday,saturday,sunday,created_date) " +
            "VALUES ('Journal','journal',1,0,1,0,1,0,0,$createdDate)"
        )

        // Habit 1 — Morning Run (daily): complete days 1-42, skip i=37,35,14,12
        // Produces best streak=20, current streak=11
        val skipH1 = setOf(37L, 35L, 14L, 12L)
        for (i in 1L..42L) {
            if (i !in skipH1) {
                db.execSQL("INSERT INTO habit_completions (habit_id,completed_date) VALUES (1,${e - i})")
            }
        }

        // Habit 2 — Read 30min (Mon-Fri): complete all weekdays i=1-42,
        // skip Mon-Fri of the week 3 weeks before this week (i=13..17)
        // Produces best streak=16, current streak=8
        val skipH2 = (13L..17L).toSet()
        for (i in 1L..42L) {
            val date = today.minusDays(i)
            val dow = date.dayOfWeek
            val isWeekday = dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY
            if (isWeekday && i !in skipH2) {
                db.execSQL("INSERT INTO habit_completions (habit_id,completed_date) VALUES (2,${e - i})")
            }
        }

        // Habit 3 — Meditate (daily): ~60% scattered completions, miss yesterday and 2 days ago
        // so current streak=0; 5 consecutive days around i=20-24 give best streak=5
        val meditateDays = setOf(
            3L, 5L, 6L, 8L, 10L, 13L, 15L, 17L,
            20L, 21L, 22L, 23L, 24L,
            26L, 29L, 31L, 33L, 36L, 38L, 40L, 42L
        )
        for (day in meditateDays) {
            db.execSQL("INSERT INTO habit_completions (habit_id,completed_date) VALUES (3,${e - day})")
        }

        // Habit 4 — Journal (Mon/Wed/Fri): complete all scheduled days i=1-42 except i=8 (Wed 4/30),
        // so current streak=3, best streak=13
        for (i in 1L..42L) {
            val date = today.minusDays(i)
            val dow = date.dayOfWeek
            val isScheduled = dow == DayOfWeek.MONDAY || dow == DayOfWeek.WEDNESDAY || dow == DayOfWeek.FRIDAY
            if (isScheduled && i != 8L) {
                db.execSQL("INSERT INTO habit_completions (habit_id,completed_date) VALUES (4,${e - i})")
            }
        }
    }
}
