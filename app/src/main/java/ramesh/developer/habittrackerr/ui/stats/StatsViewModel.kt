package ramesh.developer.habittrackerr.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ramesh.developer.habittrackerr.domain.model.ActivityDay
import ramesh.developer.habittrackerr.domain.model.HabitWithStreaks
import ramesh.developer.habittrackerr.domain.model.StatsOverview
import ramesh.developer.habittrackerr.domain.repository.HabitRepository
import java.time.DayOfWeek
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

sealed interface StatsUiState {
    data object Loading : StatsUiState
    data class Content(
        val overview: StatsOverview,
        val activityDays: List<ActivityDay>,
        val habitsWithStreaks: List<HabitWithStreaks>
    ) : StatsUiState
}

sealed interface StatsUiAction {
    data object NavigateBack : StatsUiAction
}

sealed interface StatsUiEffect {
    data object NavigateBack : StatsUiEffect
}

class StatsViewModel(private val repository: HabitRepository) : ViewModel() {

    private val today: ZonedDateTime = ZonedDateTime.now()
    private val weekStart: ZonedDateTime = today
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .truncatedTo(ChronoUnit.DAYS)
    private val heatmapStart: ZonedDateTime = weekStart.minusWeeks(5)
    private val heatmapEnd: ZonedDateTime = weekStart.plusDays(6)

    private val _effect = MutableSharedFlow<StatsUiEffect>()
    val effect: SharedFlow<StatsUiEffect> = _effect.asSharedFlow()

    val uiState: StateFlow<StatsUiState> = combine(
        repository.getStatsOverview(weekStart, today),
        repository.getActivityHeatmap(heatmapStart, heatmapEnd),
        repository.getHabitsWithStreaks()
    ) { overview, activityDays, streaks ->
        StatsUiState.Content(overview, activityDays, streaks)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StatsUiState.Loading
    )

    fun onAction(action: StatsUiAction) {
        when (action) {
            StatsUiAction.NavigateBack ->
                viewModelScope.launch { _effect.emit(StatsUiEffect.NavigateBack) }
        }
    }
}
