package ramesh.developer.habittrackerr.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object TodayRoute

@Serializable
object StatsRoute

@Serializable
data class HabitFormRoute(val habitId: Long = -1L)  // -1 = create mode
