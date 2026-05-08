package ramesh.developer.habittrackerr.ui.habitform

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ramesh.developer.habittrackerr.domain.model.DaysOfWeek
import ramesh.developer.habittrackerr.domain.model.Habit
import ramesh.developer.habittrackerr.domain.model.HabitIcon
import ramesh.developer.habittrackerr.domain.repository.HabitRepository
import ramesh.developer.habittrackerr.ui.navigation.HabitFormRoute
import java.time.ZonedDateTime

data class HabitFormUiState(
    val isEditMode: Boolean = false,
    val selectedIcon: HabitIcon = HabitIcon.RUN,
    val name: String = "",
    val nameError: String? = null,
    val daysOfWeek: DaysOfWeek = DaysOfWeek(
        monday = true, tuesday = true, wednesday = true, thursday = true,
        friday = true, saturday = true, sunday = true
    ),
    val daysError: String? = null,
    val showDeleteDialog: Boolean = false,
    val isSaving: Boolean = false
)

sealed interface HabitFormUiAction {
    data class SelectIcon(val icon: HabitIcon) : HabitFormUiAction
    data class UpdateName(val name: String) : HabitFormUiAction
    data class ToggleDay(val index: Int) : HabitFormUiAction
    data object Save : HabitFormUiAction
    data object RequestDelete : HabitFormUiAction
    data object ConfirmDelete : HabitFormUiAction
    data object DismissDeleteDialog : HabitFormUiAction
    data object NavigateBack : HabitFormUiAction
}

sealed interface HabitFormUiEffect {
    data object NavigateBack : HabitFormUiEffect
}

class HabitFormViewModel(
    private val repository: HabitRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val habitId: Long = savedStateHandle.toRoute<HabitFormRoute>().habitId
    private val isEditMode: Boolean = habitId != -1L
    private var existingHabit: Habit? = null

    private val _uiState = MutableStateFlow(HabitFormUiState(isEditMode = isEditMode))
    val uiState: StateFlow<HabitFormUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<HabitFormUiEffect>()
    val effect: SharedFlow<HabitFormUiEffect> = _effect.asSharedFlow()

    init {
        if (isEditMode) {
            viewModelScope.launch {
                repository.getHabitById(habitId)?.let { habit ->
                    existingHabit = habit
                    _uiState.update {
                        it.copy(
                            selectedIcon = habit.icon,
                            name = habit.name,
                            daysOfWeek = habit.daysOfWeek
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: HabitFormUiAction) {
        when (action) {
            is HabitFormUiAction.SelectIcon ->
                _uiState.update { it.copy(selectedIcon = action.icon) }

            is HabitFormUiAction.UpdateName ->
                _uiState.update { it.copy(name = action.name, nameError = null) }

            is HabitFormUiAction.ToggleDay ->
                _uiState.update { it.copy(daysOfWeek = it.daysOfWeek.toggle(action.index), daysError = null) }

            HabitFormUiAction.Save -> save()

            HabitFormUiAction.RequestDelete ->
                _uiState.update { it.copy(showDeleteDialog = true) }

            HabitFormUiAction.ConfirmDelete -> {
                _uiState.update { it.copy(showDeleteDialog = false) }
                viewModelScope.launch {
                    repository.deleteHabit(habitId)
                    _effect.emit(HabitFormUiEffect.NavigateBack)
                }
            }

            HabitFormUiAction.DismissDeleteDialog ->
                _uiState.update { it.copy(showDeleteDialog = false) }

            HabitFormUiAction.NavigateBack ->
                viewModelScope.launch { _effect.emit(HabitFormUiEffect.NavigateBack) }
        }
    }

    private fun save() {
        val state = _uiState.value
        val nameError = if (state.name.isBlank()) "Name cannot be empty" else null
        val days = state.daysOfWeek
        val daysError = if (!days.hasAnySelected()) "Select at least one day" else null

        if (nameError != null || daysError != null) {
            _uiState.update { it.copy(nameError = nameError, daysError = daysError) }
            return
        }

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            if (isEditMode) {
                repository.updateHabit(
                    existingHabit!!.copy(
                        name = state.name.trim(),
                        icon = state.selectedIcon,
                        daysOfWeek = state.daysOfWeek
                    )
                )
            } else {
                repository.insertHabit(
                    Habit(
                        id = 0,
                        name = state.name.trim(),
                        icon = state.selectedIcon,
                        daysOfWeek = state.daysOfWeek,
                        createdDate = ZonedDateTime.now()
                    )
                )
            }
            _effect.emit(HabitFormUiEffect.NavigateBack)
        }
    }
}

private fun DaysOfWeek.toggle(index: Int): DaysOfWeek = when (index) {
    0 -> copy(monday = !monday)
    1 -> copy(tuesday = !tuesday)
    2 -> copy(wednesday = !wednesday)
    3 -> copy(thursday = !thursday)
    4 -> copy(friday = !friday)
    5 -> copy(saturday = !saturday)
    6 -> copy(sunday = !sunday)
    else -> this
}

private fun DaysOfWeek.hasAnySelected(): Boolean =
    monday || tuesday || wednesday || thursday || friday || saturday || sunday
