package ramesh.developer.habittrackerr.ui.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ramesh.developer.habittrackerr.domain.model.HabitWithStreaks
import ramesh.developer.habittrackerr.ui.designsystem.components.BackIconButton
import ramesh.developer.habittrackerr.ui.designsystem.components.HabitIconContainer
import ramesh.developer.habittrackerr.ui.designsystem.components.HeatmapGrid
import ramesh.developer.habittrackerr.ui.designsystem.components.StatCard
import ramesh.developer.habittrackerr.ui.theme.ColorAccent
import ramesh.developer.habittrackerr.ui.theme.ColorBackground
import ramesh.developer.habittrackerr.ui.theme.ColorPrimary
import ramesh.developer.habittrackerr.ui.theme.ColorSecondary
import ramesh.developer.habittrackerr.ui.theme.ColorSuccess
import ramesh.developer.habittrackerr.ui.theme.ColorSurface
import ramesh.developer.habittrackerr.ui.theme.ColorTextPrimary
import ramesh.developer.habittrackerr.ui.theme.ColorTextTertiary
import ramesh.developer.habittrackerr.ui.theme.InterFamily

@Composable
fun StatsScreen(
    onNavigateBack: () -> Unit,
    viewModel: StatsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                StatsUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    StatsContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun StatsContent(
    state: StatsUiState,
    onAction: (StatsUiAction) -> Unit
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
            BackIconButton(onClick = { onAction(StatsUiAction.NavigateBack) })
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ColorTextPrimary,
                modifier = Modifier.padding(start = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (state) {
            StatsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressIndicator(color = ColorPrimary)
                }
            }

            is StatsUiState.Content -> {
                // Summary cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        label = "This Week",
                        value = "${state.overview.thisWeekPercent}%",
                        valueColor = ColorSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Best Streak",
                        value = "${state.overview.bestStreak}",
                        valueColor = ColorSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Active",
                        value = "${state.overview.activeCount}",
                        valueColor = ColorAccent,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Activity heatmap
                Text(
                    text = "Activity",
                    style = MaterialTheme.typography.headlineLarge,
                    color = ColorTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = ColorSurface)
                ) {
                    HeatmapGrid(
                        activityDays = state.activityDays,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Streaks list
                Text(
                    text = "Streaks",
                    style = MaterialTheme.typography.headlineLarge,
                    color = ColorTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (state.habitsWithStreaks.isEmpty()) {
                    Text(
                        text = "No habits yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ColorTextTertiary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        state.habitsWithStreaks.forEach { item ->
                            StreakItem(item = item)
                        }
                    }
                }
            }
        }
    }
    } // end Scaffold
}

@Composable
private fun StreakItem(item: HabitWithStreaks) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HabitIconContainer(icon = item.habit.icon, size = 36.dp)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.habit.name,
                style = MaterialTheme.typography.titleLarge,
                color = ColorTextPrimary
            )
            Text(
                text = "Best: ${item.bestStreak}",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = ColorTextTertiary
            )
        }

        Text(
            text = "${item.currentStreak}",
            fontFamily = InterFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            color = ColorSuccess
        )
    }
}
