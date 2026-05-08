package ramesh.developer.habittrackerr.domain.model

import java.time.ZonedDateTime

data class Habit(
    val id: Long = 0,
    val name: String,
    val icon: HabitIcon,
    val daysOfWeek: DaysOfWeek,
    val createdDate: ZonedDateTime
)

enum class HabitIcon(val id: String) {
    RUN("run"),
    READ("read"),
    WATER("water"),
    MEDITATE("meditate"),
    SLEEP("sleep"),
    CODE("code"),
    MUSIC("music"),
    COOK("cook"),
    JOURNAL("journal"),
    GYM("gym"),
    YOGA("yoga"),
    WALK("walk"),
    CYCLE("cycle"),
    STUDY("study"),
    NO_PHONE("no_phone"),
    VITAMINS("vitamins"),
    LANGUAGE("language"),
    GRATITUDE("gratitude"),
    HEALTH("health"),
    ORGANIZE("organize");

    companion object {
        fun fromId(id: String): HabitIcon = entries.first { it.id == id }
    }
}
