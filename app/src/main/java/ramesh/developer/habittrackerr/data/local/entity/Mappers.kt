package ramesh.developer.habittrackerr.data.local.entity

import androidx.annotation.DrawableRes
import java.time.LocalDate
import java.time.ZoneId
import ramesh.developer.habittrackerr.R
import ramesh.developer.habittrackerr.domain.model.DaysOfWeek
import ramesh.developer.habittrackerr.domain.model.Habit
import ramesh.developer.habittrackerr.domain.model.HabitIcon

fun HabitEntity.toDomain() = Habit(
    id = id,
    name = name,
    icon = enumValueOf<HabitIcon>(iconName),
    daysOfWeek = toDaysOfWeek(),
    createdDate = LocalDate.ofEpochDay(createdDate).atStartOfDay(ZoneId.systemDefault())
)

fun HabitEntity.toDaysOfWeek() = DaysOfWeek(
    monday    = monday,
    tuesday   = tuesday,
    wednesday = wednesday,
    thursday  = thursday,
    friday    = friday,
    saturday  = saturday,
    sunday    = sunday
)

fun Habit.toEntity() = HabitEntity(
    id          = id,
    name        = name,
    iconName    = icon.name,
    monday      = daysOfWeek.monday,
    tuesday     = daysOfWeek.tuesday,
    wednesday   = daysOfWeek.wednesday,
    thursday    = daysOfWeek.thursday,
    friday      = daysOfWeek.friday,
    saturday    = daysOfWeek.saturday,
    sunday      = daysOfWeek.sunday,
    createdDate = createdDate.toLocalDate().toEpochDay()
)

fun habitIconFromDrawable(@DrawableRes drawableResId: Int): HabitIcon = when (drawableResId) {
    R.drawable.ic_habit_run       -> HabitIcon.RUN
    R.drawable.ic_habit_read      -> HabitIcon.READ
    R.drawable.ic_habit_water     -> HabitIcon.WATER
    R.drawable.ic_habit_meditate  -> HabitIcon.MEDITATE
    R.drawable.ic_habit_sleep     -> HabitIcon.SLEEP
    R.drawable.ic_habit_code      -> HabitIcon.CODE
    R.drawable.ic_habit_music     -> HabitIcon.MUSIC
    R.drawable.ic_habit_cook      -> HabitIcon.COOK
    R.drawable.ic_habit_journal   -> HabitIcon.JOURNAL
    R.drawable.ic_habit_gym       -> HabitIcon.GYM
    R.drawable.ic_habit_yoga      -> HabitIcon.YOGA
    R.drawable.ic_habit_walk      -> HabitIcon.WALK
    R.drawable.ic_habit_cycle     -> HabitIcon.CYCLE
    R.drawable.ic_habit_study     -> HabitIcon.STUDY
    R.drawable.ic_habit_no_phone  -> HabitIcon.NO_PHONE
    R.drawable.ic_habit_vitamins  -> HabitIcon.VITAMINS
    R.drawable.ic_habit_language  -> HabitIcon.LANGUAGE
    R.drawable.ic_habit_gratitude -> HabitIcon.GRATITUDE
    R.drawable.ic_habit_health    -> HabitIcon.HEALTH
    R.drawable.ic_habit_organize  -> HabitIcon.ORGANIZE
    else -> throw IllegalArgumentException("Unknown drawable resource id: $drawableResId")
}
