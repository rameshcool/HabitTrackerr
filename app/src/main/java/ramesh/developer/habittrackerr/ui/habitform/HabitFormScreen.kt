package ramesh.developer.habittrackerr.ui.habitform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ramesh.developer.habittrackerr.ui.designsystem.components.BackIconButton
import ramesh.developer.habittrackerr.ui.designsystem.components.DestructiveTextButton
import ramesh.developer.habittrackerr.ui.designsystem.components.DayPickerRow
import ramesh.developer.habittrackerr.ui.designsystem.components.HabitTextField
import ramesh.developer.habittrackerr.ui.designsystem.components.IconPickerGrid
import ramesh.developer.habittrackerr.ui.designsystem.components.IconPreviewBox
import ramesh.developer.habittrackerr.ui.designsystem.components.PrimaryButton
import ramesh.developer.habittrackerr.ui.designsystem.components.SectionLabel
import ramesh.developer.habittrackerr.ui.theme.ColorBackground
import ramesh.developer.habittrackerr.ui.theme.ColorDestructive
import ramesh.developer.habittrackerr.ui.theme.ColorSurface
import ramesh.developer.habittrackerr.ui.theme.ColorTextPrimary
import ramesh.developer.habittrackerr.ui.theme.ColorTextTertiary

@Composable
fun HabitFormScreen(
    onNavigateBack: () -> Unit,
    viewModel: HabitFormViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HabitFormUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    HabitFormContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun HabitFormContent(
    state: HabitFormUiState,
    onAction: (HabitFormUiAction) -> Unit
) {
    Scaffold(containerColor = ColorBackground) { innerPadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIconButton(onClick = { onAction(HabitFormUiAction.NavigateBack) })
            Text(
                text = if (state.isEditMode) "Edit Habit" else "New Habit",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ColorTextPrimary,
                modifier = Modifier.padding(start = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Icon preview
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconPreviewBox(
                    icon = state.selectedIcon,
                    isEditMode = state.isEditMode,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tap to change icon",
                    style = MaterialTheme.typography.labelSmall,
                    color = ColorTextTertiary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Icon picker grid
        SectionLabel(text = "Choose Icon")
        Spacer(modifier = Modifier.height(8.dp))
        IconPickerGrid(
            selectedIcon = state.selectedIcon,
            onSelectIcon = { onAction(HabitFormUiAction.SelectIcon(it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Name field
        SectionLabel(text = "Name")
        Spacer(modifier = Modifier.height(8.dp))
        HabitTextField(
            value = state.name,
            onValueChange = { onAction(HabitFormUiAction.UpdateName(it)) },
            placeholder = "e.g. Morning Run",
            errorMessage = state.nameError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Day picker
        SectionLabel(text = "Repeat On")
        Spacer(modifier = Modifier.height(8.dp))
        DayPickerRow(
            daysOfWeek = state.daysOfWeek,
            onToggleDay = { onAction(HabitFormUiAction.ToggleDay(it)) },
            errorMessage = state.daysError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Save button
        PrimaryButton(
            text = if (state.isEditMode) "Save Changes" else "Save Habit",
            onClick = { onAction(HabitFormUiAction.Save) },
            enabled = !state.isSaving
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Discard / Delete button
        DestructiveTextButton(
            text = if (state.isEditMode) "Delete Habit" else "Discard Habit",
            onClick = {
                if (state.isEditMode) onAction(HabitFormUiAction.RequestDelete)
                else onAction(HabitFormUiAction.NavigateBack)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
    } // end Scaffold

    // Delete confirmation dialog
    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { onAction(HabitFormUiAction.DismissDeleteDialog) },
            containerColor = ColorSurface,
            title = {
                Text(
                    text = "Delete Habit",
                    color = ColorTextPrimary
                )
            },
            text = {
                Text(
                    text = "This habit and all its history will be permanently deleted.",
                    color = ColorTextTertiary
                )
            },
            confirmButton = {
                TextButton(onClick = { onAction(HabitFormUiAction.ConfirmDelete) }) {
                    Text(text = "Delete", color = ColorDestructive)
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(HabitFormUiAction.DismissDeleteDialog) }) {
                    Text(text = "Cancel", color = ColorTextTertiary)
                }
            }
        )
    }
}
