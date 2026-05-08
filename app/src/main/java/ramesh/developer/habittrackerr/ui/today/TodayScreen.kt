package ramesh.developer.habittrackerr.ui.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ramesh.developer.habittrackerr.domain.model.HabitWithCompletion
import ramesh.developer.habittrackerr.ui.designsystem.components.CompletionToggle
import ramesh.developer.habittrackerr.ui.designsystem.components.HabitIconContainer
import ramesh.developer.habittrackerr.ui.theme.ColorBackground
import ramesh.developer.habittrackerr.ui.theme.ColorPrimary
import ramesh.developer.habittrackerr.ui.theme.ColorSecondary
import ramesh.developer.habittrackerr.ui.theme.ColorStreak
import ramesh.developer.habittrackerr.ui.theme.ColorSurface
import ramesh.developer.habittrackerr.ui.theme.ColorTextPrimary
import ramesh.developer.habittrackerr.ui.theme.ColorTextSecondary
import ramesh.developer.habittrackerr.ui.theme.ColorTextTertiary
import ramesh.developer.habittrackerr.ui.theme.ManropeFamily

@Composable
fun TodayScreen(
    onNavigateToStats: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    viewModel: TodayViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TodayUiEffect.NavigateToStats -> onNavigateToStats()
                TodayUiEffect.NavigateToCreate -> onNavigateToCreate()
                is TodayUiEffect.NavigateToEdit -> onNavigateToEdit(effect.habitId)
            }
        }
    }

    TodayContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun TodayContent(
    state: TodayUiState,
    onAction: (TodayUiAction) -> Unit
) {
    Scaffold(
        containerColor = ColorBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(TodayUiAction.NavigateToCreate) },
                shape = MaterialTheme.shapes.large,
                containerColor = ColorPrimary,
                contentColor = ColorTextPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add habit"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    val dateLabel = when (state) {
                        is TodayUiState.Content -> state.dateLabel
                        is TodayUiState.Empty -> state.dateLabel
                        TodayUiState.Loading -> ""
                    }
                    if (dateLabel.isNotEmpty()) {
                        Text(
                            text = dateLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ColorTextSecondary
                        )
                    }
                    Text(
                        text = "Today",
                        fontFamily = ManropeFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 34.sp,
                        color = ColorTextPrimary
                    )
                }
                IconButton(
                    onClick = { onAction(TodayUiAction.NavigateToStats) },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(MaterialTheme.shapes.medium),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = ColorSurface)
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "Statistics",
                        tint = ColorSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                TodayUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ColorPrimary)
                    }
                }

                is TodayUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No habits yet",
                                style = MaterialTheme.typography.titleLarge,
                                color = ColorTextSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap + to add your first habit",
                                style = MaterialTheme.typography.bodyLarge,
                                color = ColorTextTertiary
                            )
                        }
                    }
                }

                is TodayUiState.Content -> {
                    // Progress section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daily progress",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ColorTextTertiary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${state.completed} / ${state.total}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ColorTextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { state.completed.toFloat() / state.total.coerceAtLeast(1) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(CircleShape),
                        color = ColorPrimary,
                        trackColor = ColorSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(state.habits, key = { it.habit.id }) { item ->
                            HabitCard(
                                item = item,
                                currentStreak = state.streakMap[item.habit.id] ?: 0,
                                onCardClick = { onAction(TodayUiAction.NavigateToEdit(item.habit.id)) },
                                onToggle = { onAction(TodayUiAction.ToggleCompletion(item.habit.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HabitCard(
    item: HabitWithCompletion,
    currentStreak: Int,
    onCardClick: () -> Unit,
    onToggle: () -> Unit
) {
    Card(
        onClick = onCardClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ColorSurface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HabitIconContainer(icon = item.habit.icon, size = 42.dp)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.habit.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = ColorTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                if (currentStreak > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = ColorStreak,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "$currentStreak day streak",
                            fontFamily = ManropeFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = ColorStreak
                        )
                    }
                } else {
                    Text(
                        text = "No streak yet",
                        fontFamily = ManropeFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = ColorTextTertiary
                    )
                }
            }

            CompletionToggle(
                isCompleted = item.isCompletedToday,
                onToggle = onToggle
            )
        }
    }
}
