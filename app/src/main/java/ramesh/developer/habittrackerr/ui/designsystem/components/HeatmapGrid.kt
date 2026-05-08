package ramesh.developer.habittrackerr.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ramesh.developer.habittrackerr.domain.model.ActivityDay
import ramesh.developer.habittrackerr.ui.theme.ColorBorder
import ramesh.developer.habittrackerr.ui.theme.ColorPrimary
import ramesh.developer.habittrackerr.ui.theme.ColorSecondary
import ramesh.developer.habittrackerr.ui.theme.ColorSurfaceElevated
import ramesh.developer.habittrackerr.ui.theme.ColorTextTertiary
import ramesh.developer.habittrackerr.ui.theme.InterFamily
import java.time.LocalDate

private const val COLUMNS = 7
private const val ROWS = 6
private val CELL_GAP = 4.dp
private val WEEK_LABEL_WIDTH = 28.dp
private val DAY_LABELS = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
fun HeatmapGrid(
    activityDays: List<ActivityDay>,
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }

    BoxWithConstraints(modifier = modifier) {
        // Compute a square cell size that fills the available width evenly.
        // Layout: [week-label] [gap] [7 cells with 6 gaps between them]
        // totalWidth = WEEK_LABEL_WIDTH + CELL_GAP + 7*cellSize + 6*CELL_GAP
        //            = WEEK_LABEL_WIDTH + 7*CELL_GAP + 7*cellSize
        val cellSize: Dp = ((maxWidth - WEEK_LABEL_WIDTH - CELL_GAP * COLUMNS) / COLUMNS)
            .coerceAtLeast(8.dp)

        Column {
            // Grid rows with week labels
            Row(horizontalArrangement = Arrangement.spacedBy(CELL_GAP)) {
                // Week labels column
                Column(verticalArrangement = Arrangement.spacedBy(CELL_GAP)) {
                    repeat(ROWS) { rowIndex ->
                        Box(
                            modifier = Modifier.size(cellSize),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "W${rowIndex + 1}",
                                fontFamily = InterFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                color = ColorTextTertiary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Grid cells
                Column(verticalArrangement = Arrangement.spacedBy(CELL_GAP)) {
                    repeat(ROWS) { rowIndex ->
                        Row(horizontalArrangement = Arrangement.spacedBy(CELL_GAP)) {
                            repeat(COLUMNS) { colIndex ->
                                val dayIndex = rowIndex * COLUMNS + colIndex
                                val activityDay = activityDays.getOrNull(dayIndex)
                                val date = activityDay?.date?.toLocalDate()
                                val isFuture = date?.isAfter(today) ?: false
                                val isToday = date == today

                                HeatmapCell(
                                    ratio = activityDay?.completionRatio ?: 0f,
                                    isFuture = isFuture,
                                    isToday = isToday,
                                    size = cellSize
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(CELL_GAP))

            // Day-of-week labels
            Row(
                modifier = Modifier.padding(start = cellSize + CELL_GAP),
                horizontalArrangement = Arrangement.spacedBy(CELL_GAP)
            ) {
                DAY_LABELS.forEach { label ->
                    Box(
                        modifier = Modifier.size(cellSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontFamily = InterFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = ColorTextTertiary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeatmapCell(
    ratio: Float,
    isFuture: Boolean,
    isToday: Boolean,
    size: Dp
) {
    val shape = MaterialTheme.shapes.extraSmall
    val borderColor = ColorBorder
    val secondaryColor = ColorSecondary

    val cellColor = when {
        isFuture -> ColorSurfaceElevated.copy(alpha = 0f)
        ratio == 0f -> ColorSurfaceElevated
        ratio <= 0.25f -> ColorPrimary.copy(alpha = 0.3f)
        ratio <= 0.50f -> ColorPrimary.copy(alpha = 0.6f)
        ratio <= 0.75f -> ColorPrimary.copy(alpha = 0.85f)
        else -> ColorPrimary
    }

    val cellModifier = when {
        isFuture -> Modifier
            .size(size)
            .clip(shape)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = CornerRadius(4.dp.toPx()),
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f))
                    )
                )
            }
        isToday -> Modifier
            .size(size)
            .clip(shape)
            .background(cellColor)
            .border(1.5.dp, secondaryColor, shape)
        else -> Modifier
            .size(size)
            .clip(shape)
            .background(cellColor)
    }

    Box(modifier = cellModifier)
}
