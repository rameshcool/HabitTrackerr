package ramesh.developer.habittrackerr.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ramesh.developer.habittrackerr.domain.model.DaysOfWeek
import ramesh.developer.habittrackerr.ui.theme.ColorBorder
import ramesh.developer.habittrackerr.ui.theme.ColorDestructive
import ramesh.developer.habittrackerr.ui.theme.ColorPrimary
import ramesh.developer.habittrackerr.ui.theme.ColorSecondary
import ramesh.developer.habittrackerr.ui.theme.ColorSurface
import ramesh.developer.habittrackerr.ui.theme.ColorSurfaceBright
import ramesh.developer.habittrackerr.ui.theme.ColorTextTertiary
import ramesh.developer.habittrackerr.ui.theme.InterFamily

private val DAY_LABELS = listOf("M", "T", "W", "T", "F", "S", "S")

private fun DaysOfWeek.toList() = listOf(
    monday, tuesday, wednesday, thursday, friday, saturday, sunday
)

@Composable
fun DayPickerRow(
    daysOfWeek: DaysOfWeek,
    onToggleDay: (Int) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    val dayList = daysOfWeek.toList()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            DAY_LABELS.forEachIndexed { index, label ->
                val selected = dayList[index]
                val shape = MaterialTheme.shapes.small
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(shape)
                        .background(if (selected) ColorSurfaceBright else ColorSurface)
                        .border(
                            width = if (selected) 1.5.dp else 1.dp,
                            color = if (selected) ColorPrimary else ColorBorder,
                            shape = shape
                        )
                        .clickable { onToggleDay(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = if (selected) ColorSecondary else ColorTextTertiary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = ColorDestructive,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}
