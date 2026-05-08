package ramesh.developer.habittrackerr.ui.today

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
import ramesh.developer.habittrackerr.domain.model.HabitWithCompletion
import ramesh.developer.habittrackerr.domain.repository.HabitRepository
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

sealed interface TodayUiState {
    data object Loading : TodayUiState
    data class Empty(val dateLabel: String) : TodayUiState
    data class Content(
        val dateLabel: String,
        val completed: Int,
        val total: Int,
        val habits: List<HabitWithCompletion>,
        val streakMap: Map<Long, Int>
    ) : TodayUiState
}

sealed interface TodayUiAction {
    data class ToggleCompletion(val habitId: Long) : TodayUiAction
    data object NavigateToStats : TodayUiAction
    data object NavigateToCreate : TodayUiAction
    data class NavigateToEdit(val habitId: Long) : TodayUiAction
}

sealed interface TodayUiEffect {
    data object NavigateToStats : TodayUiEffect
    data object NavigateToCreate : TodayUiEffect
    data class NavigateToEdit(val habitId: Long) : TodayUiEffect
}

class TodayViewModel(private val repository: HabitRepository) : ViewModel() {

    // Captured once at init — prevents date shifting mid-session past midnight
    private val today: ZonedDateTime = ZonedDateTime.now()
    val dateLabel: String = DateTimeFormatter.ofPattern("EEEE, MMMM d").format(today)

    private val _effect = MutableSharedFlow<TodayUiEffect>()
    val effect: SharedFlow<TodayUiEffect> = _effect.asSharedFlow()

    val uiState: StateFlow<TodayUiState> = combine(
        repository.getHabitsWithCompletionForDate(today),
        repository.getHabitsWithStreaks()
    ) { withCompletion, withStreaks ->
        if (withCompletion.isEmpty()) {
            TodayUiState.Empty(dateLabel)
        } else {
            TodayUiState.Content(
                dateLabel = dateLabel,
                completed = withCompletion.count { it.isCompletedToday },
                total = withCompletion.size,
                habits = withCompletion,
                streakMap = withStreaks.associate { it.habit.id to it.currentStreak }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TodayUiState.Loading
    )

    fun onAction(action: TodayUiAction) {
        when (action) {
            is TodayUiAction.ToggleCompletion ->
                viewModelScope.launch { repository.toggleCompletion(action.habitId, today) }
            is TodayUiAction.NavigateToStats ->
                viewModelScope.launch { _effect.emit(TodayUiEffect.NavigateToStats) }
            is TodayUiAction.NavigateToCreate ->
                viewModelScope.launch { _effect.emit(TodayUiEffect.NavigateToCreate) }
            is TodayUiAction.NavigateToEdit ->
                viewModelScope.launch { _effect.emit(TodayUiEffect.NavigateToEdit(action.habitId)) }
        }
    }
}
