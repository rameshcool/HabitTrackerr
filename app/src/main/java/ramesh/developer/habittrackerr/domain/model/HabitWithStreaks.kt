package ramesh.developer.habittrackerr.domain.model

data class HabitWithStreaks(
    val habit: Habit,
    val currentStreak: Int,
    val bestStreak: Int
)
